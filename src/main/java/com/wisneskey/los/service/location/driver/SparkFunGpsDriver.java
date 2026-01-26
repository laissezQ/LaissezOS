package com.wisneskey.los.service.location.driver;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

import net.sf.marineapi.nmea.parser.DataNotAvailableException;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.GSVSentence;
import net.sf.marineapi.nmea.util.GpsFixQuality;
import net.sf.marineapi.nmea.util.Position;

/**
 * GPS driver for getting data from the SparkFun GPS board.
 * 
 * Copyright (C) 2026 Paul Wisneskey
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author paul.wisneskey@gmail.com
 */
public class SparkFunGpsDriver implements GpsDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkFunGpsDriver.class);

	/**
	 * Number of milliseconds to sleep before trying another if an empty read is
	 * detected from the GPS. This is set to be slightly shorter than the
	 * reporting interval of the GPS.
	 */
	private static final int GPS_SLEEP_MS = 800;

	/**
	 * Number of last GPS position samples to use for calculating the latest
	 * position. Used to smooth the jitter from the GPS.
	 */
	private static final int SMOOTHING_SAMPLES = 90;

	/**
	 * I2C bus number the board is on.
	 */
	private static final int I2C_BUS = 1;

	/**
	 * I2C address for the GPS driver board.
	 */
	private static final int I2C_ADDRESS = 0x42;

	/**
	 * I2C connection to the board.
	 */
	private I2C board;

	/**
	 * Flag indicating if we have a fix from the last GPS sample.
	 */
	private boolean haveFix = false;

	/**
	 * Circular list of historical samples for location smoothing.
	 */
	private Location[] sampleHistory = new Location[SMOOTHING_SAMPLES];

	/**
	 * Current index to write the next sample to.
	 */
	private int sampleIndex = 0;

	/**
	 * Object to use for synchronizing access to sample history.
	 */
	private Object sampleLock = new Object();

	/**
	 * Number of satellites that were used to obtain the last position.
	 */
	private AtomicInteger satellitesInFix = new AtomicInteger(0);

	/**
	 * Number of satellites that are in view of the GPS.
	 */
	private AtomicInteger satellitesInView = new AtomicInteger(0);

	// ----------------------------------------------------------------------------------------
	// GpsDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(Profile profile) {

		LOGGER.debug("Initializing SparkFun GPS board driver...");

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Board I2C address: {}", Integer.toHexString(I2C_ADDRESS));
		}

		Context pi4jContext = Kernel.kernel().getPi4jContext();
		I2CProvider i2cProvider = pi4jContext.provider("linuxfs-i2c");

		I2CConfig i2cConfig = I2C.newConfigBuilder(pi4jContext).id("gpsBoard").bus(I2C_BUS).device(I2C_ADDRESS).build();

		try {
			board = i2cProvider.create(i2cConfig);
		} catch (Exception e) {
			throw new LaissezException("Failed to create I2C connection to GPS board.", e);
		}

		new GpsReader().start();
	}

	@Override
	public Location getCurrentLocation() {

		return getSmoothedLocation();
	}

	@Override
	public int getSatellitesInFix() {
		return satellitesInFix.get();
	}

	@Override
	public int getSatellitesInView() {
		return satellitesInView.get();
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Processes the latest sample and updates the sample history as appropriate
	 * based on the sample and fix status in it.
	 * 
	 * @param latestSample Latest sample from the GPS hardware.
	 */
	private void updateSampleHistory(GGASentence latestSample, boolean dataUnavailableError) {

		synchronized (sampleLock) {

			boolean latestHasFix;
			if (dataUnavailableError) {
				latestHasFix = false;
			} else {
				latestHasFix = latestSample.getFixQuality() != GpsFixQuality.INVALID;
			}

			if (latestHasFix != haveFix) {

				// If the fix status has changed we have special processing.
				if (latestHasFix) {

					LOGGER.debug("Fix acquired; starting history.");

					// For the first fix, we will the history with its location.
					sampleIndex = 0;
					Arrays.fill(sampleHistory, locationFrom(latestSample));

					haveFix = true;

				} else {

					LOGGER.debug("Fix lost; clearing history.");

					sampleIndex = 0;
					Arrays.fill(sampleHistory, null);

					haveFix = false;
				}

			} else {
				// Fix status not changed.
				if (haveFix) {

					LOGGER.debug("Received new fix data: updating history.");

					// Added latest fix to history.
					sampleHistory[sampleIndex] = locationFrom(latestSample);

					// Increment our index but roll over for the circular buffer.
					sampleIndex = (sampleIndex + 1) % SMOOTHING_SAMPLES;
				} else {
					LOGGER.debug("No fix data; nothing to record.");
				}
			}
		}
	}

	/**
	 * Returns a location created from averaging the history of location samples.
	 * 
	 * @return Location object whose values are the average all the retained
	 *         historical values for the recently sampled locations.
	 */
	private Location getSmoothedLocation() {

		synchronized (sampleLock) {

			if (!haveFix) {
				return null;
			}

			// Smooth the location by averaging all of our samples.
			double totalLatitude = 0.0d;
			double totalLongitude = 0.0d;
			double totalAltitude = 0.0d;

			for (int index = 0; index < SMOOTHING_SAMPLES; index++) {
				Location sample = sampleHistory[index];

				totalLatitude += sample.getLatitude();
				totalLongitude += sample.getLongitude();
				totalAltitude += sample.getAltitude();
			}

			return Location.of(//
					totalLatitude / SMOOTHING_SAMPLES, //
					totalLongitude / SMOOTHING_SAMPLES, //
					totalAltitude / SMOOTHING_SAMPLES);
		}
	}

	/**
	 * Returns a location object populate from a supplied GPS GGA sentence.
	 * 
	 * @param  sentence GGA sentence from the GPS.
	 * @return          Location object with same position as the GGS sentence.
	 */
	private Location locationFrom(GGASentence sentence) {

		Position position = sentence.getPosition();
		return Location.of(position.getLatitude(), position.getLongitude(), position.getAltitude());
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Thread for polling the GPS unit for data via I2C and invoking the process
	 * line callback when there is a whole line read.
	 */
	private class GpsReader extends Thread {

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private GpsReader() {
			setName("gpsReader");
			setDaemon(true);
		}

		// ----------------------------------------------------------------------------------------
		// Thread methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public void run() {

			LOGGER.info("GPS reader thread started.");

			StringBuilder line = new StringBuilder();
			byte[] buffer = new byte[255];

			while (!isInterrupted()) {

				// Fill with garbage byte
				Arrays.fill(buffer, (byte) 0x0a);

				// Read up to the maximum packet size.
				board.read(buffer);
				int linefeedCount = 0;
				for (int i = 0; i < 255; i++) {

					byte current = buffer[i];

					if (current == 0x0a || current < 0) {
						// Ignore linefeeds and negative bytes
						linefeedCount += 1;
						continue;
					}

					// Stop and build line at a carriage return or a $
					if (current == 0x0d || ((current == '$') && (line.length() > 0)) ) {

						// We have a carriage return so if the line is non-empty, submit it.
						if (line.length() > 0) {

							LOGGER.debug("Found carriage return or $; processing: line={}", line);
							processGpsLine(line.toString());

							// Start a new line.
							line.setLength(0);

							// If we processed a line due to a $ we need to start next line with it.
							if (current == '$') {
								line.append('$');
							}
						}
					} else {
						// Append character read to our buffer.
						line.append((char) current);
					}
				}

				if (linefeedCount >= 255) {
					// There was no data in last read so sleep for a while.
					try {
						LOGGER.trace("Empty response from GPS module: sleeping...");
						Thread.sleep(GPS_SLEEP_MS);
					} catch (InterruptedException e) {
						LOGGER.warn("Interrupted during GPS sleep.");
						Thread.currentThread().interrupt();
						break;
					}
				}
			}

			LOGGER.info("GPS reader thread shutdown.");
		}

		/**
		 * Receives a line from the GPS reader thread.
		 * 
		 * @param line Line from the GPS to process.
		 */
		private void processGpsLine(String line) {

			if (line.startsWith("$GNGGA")) {
				// Pull out position reports.
				processPositionLine(line);
			} else if (line.startsWith("$GPGSV")) {
				// Capture details from the System Fix Data
				processSatellitesInViewLine(line);
			} else {
				LOGGER.trace("GPS: Ignoring {}", line);
			}
		}

		/**
		 * Process a GNGGA position line from the GPS.
		 * 
		 * @param line Line to process.
		 */
		private void processPositionLine(String line) {

			try {
				SentenceFactory sf = SentenceFactory.getInstance();
				GGASentence gsa = (GGASentence) sf.createParser(line);

				LOGGER.debug("GPS Location: {} quality={} numSatellites={}", gsa.getPosition(), gsa.getFixQuality(),
						gsa.getSatelliteCount());
				updateSampleHistory(gsa, false);
				satellitesInFix.set(gsa.getSatelliteCount());

			} catch (DataNotAvailableException e) {

				// GPS is online but not returning a location yet.
				LOGGER.debug("Data not available error from GPS.");
				updateSampleHistory(null, true);

			} catch (Exception e) {
				// This line is occasionally corrupt and we just ignore it if so.
				// It would be nice to figure out why at some point.
			}
		}

		/**
		 * Process a GNGSV satellites in view line from the GPS.
		 * 
		 * @param line Line to process.
		 */
		private void processSatellitesInViewLine(String line) {

			try {
				SentenceFactory sf = SentenceFactory.getInstance();
				GSVSentence gsv = (GSVSentence) sf.createParser(line);

				LOGGER.debug("GPS Satellite: inView={}", gsv.getSatelliteCount());
				satellitesInView.set(gsv.getSatelliteCount());

			} catch (DataNotAvailableException e) {

				// GPS is online but not returning data yet.
				LOGGER.debug("Data not available error from GPS.");

			} catch (Exception e) {
				// This line is occasionally corrupt and we just ignore it if so.
				// It would be nice to figure out why at some point.
			}
		}
	}
}
