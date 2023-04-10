package com.wisneskey.los.service.location.driver;

import java.util.Arrays;

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

import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.util.GpsFixQuality;
import net.sf.marineapi.nmea.util.Position;

/**
 * GPS driver for getting data from the SparkFun GPS board.
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
	private static final int I2C_ADDRESS = 0x10;

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

	// ----------------------------------------------------------------------------------------
	// GpsDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(Profile profile) {

		LOGGER.debug("Initializing SparkFun GPS board driver...");
		LOGGER.trace("Board I2C address: " + Integer.toHexString(I2C_ADDRESS));

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

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Receives a line from the GPS reader thread.
	 * 
	 * @param line
	 *          Line from the GPS to process.
	 */
	private void processGpsLine(String line) {

		// Only pull out position reports for parsing.
		if (line.startsWith("$GNGGA")) {

			try {
				SentenceFactory sf = SentenceFactory.getInstance();
				GGASentence gsa = (GGASentence) sf.createParser(line);

				LOGGER.debug("GPS: {} quality={}", gsa.getPosition(), gsa.getFixQuality());
				updateSampleHistory(gsa);

			} catch (Exception e) {

				LOGGER.warn("Failed to parse GPS location.", e);
			}
		}
	}

	/**
	 * Processes the latest sample and updates the sample history as appropriate based on the
	 * sample and fix status in it.
	 * 
	 * @param latestSample Latest sample from the GPS hardware.
	 */
	private void updateSampleHistory(GGASentence latestSample) {

		synchronized (sampleLock) {

			boolean latestHasFix = latestSample.getFixQuality() != GpsFixQuality.INVALID;

			if (latestHasFix != haveFix) {

				// If the fix status has changed we have special processing.
				if (latestHasFix) {

					LOGGER.debug("Fix acquired; starting history.");

					// For the first fix, we will the history with its location.
					sampleIndex = 0;
					Arrays.fill(sampleHistory, locationFrom(latestSample));

				} else {
					LOGGER.debug("Fix lost; clearing history.");

					sampleIndex = 0;
					Arrays.fill(sampleHistory, null);
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
					totalLatitude / (double) SMOOTHING_SAMPLES, //
					totalLongitude / (double) SMOOTHING_SAMPLES, //
					totalAltitude / (double) SMOOTHING_SAMPLES);
		}
	}

	/**
	 * Returns a location object populate from a supplied GPS GGA sentence.
	 * 
	 * @param sentance
	 *          GGA sentence from the GPS.
	 * @return Location object with same position as the GGS sentence.
	 */
	private Location locationFrom(GGASentence sentance) {

		Position position = sentance.getPosition();
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
				for (int i = 0; i < 255; i++) {
					buffer[i] = 0x0a;
				}

				// Read up to the maximum packet size.
				board.read(buffer);
				int linefeedCount = 0;
				for (int i = 0; i < 255; i++) {

					byte current = buffer[i];

					if (current == 0x0a) {
						// Ignore linefeeds
						linefeedCount += 1;
						continue;
					}

					if (current == 0x0d) {

						// We have a carriage return so if the line is non-empty, submit it.
						if (line.length() > 0) {

							processGpsLine(line.toString());

							// Start a new line.
							line.setLength(0);
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
						LOGGER.info("Interrupted during GPS sleep.");
						break;
					}
				}
			}

			LOGGER.info("GPS reader thread shutdown.");
		}
	}
}
