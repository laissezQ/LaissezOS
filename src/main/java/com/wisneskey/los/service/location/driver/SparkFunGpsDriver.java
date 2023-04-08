package com.wisneskey.los.service.location.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.profile.model.Profile;

import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;

public class SparkFunGpsDriver implements GpsDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkFunGpsDriver.class);

	/**
	 * I2C bus number the board is on.
	 */
	private static final int I2C_BUS = 1;

	/**
	 * Default I2C address for the GPS driver board.
	 */
	private static final int I2C_ADDRESS = 0x10;

	/**
	 * I2C connection to the board.
	 */
	private I2C board;

	@Override
	public void initialize(Profile profile) {

		LOGGER.debug("Initializing SparkFun GPS board driver...");
		LOGGER.trace("Board I2C address: " + Integer.toHexString(I2C_ADDRESS));

		Context pi4jContext = Pi4J.newAutoContext();
		I2CProvider i2cProvider = pi4jContext.provider("linuxfs-i2c");

		I2CConfig i2cConfig = I2C.newConfigBuilder(pi4jContext).id("gpsBoard").bus(I2C_BUS).device(I2C_ADDRESS).build();

		try {
			board = i2cProvider.create(i2cConfig);
		} catch (Exception e) {
			throw new LaissezException("Failed to create I2C connection to GPS board.", e);
		}

		new GpsReader().start();
	}

	private void processGpsLine(String line) {

		
		if( line.startsWith("$GNGGA")) {
			
			SentenceFactory sf = SentenceFactory.getInstance();
			GGASentence gsa = (GGASentence) sf.createParser(line);

			LOGGER.debug("GPS: {} quality={}", gsa.getPosition(), gsa.getFixQuality());
		}
	}

	private class GpsReader extends Thread {

		private StringBuilder line = new StringBuilder();

		public void run() {

			byte[] buffer = new byte[255];

			while (true) {

				// Fill with garbage byte
				for (int i = 0; i < 255; i++) {
					buffer[i] = 0x0a;
				}

				// Read up to the maximum packet size.
				board.read(buffer);
				for (int i = 0; i < 255; i++) {

					byte current = buffer[i];

					if (current == 0x0a) {
						// Ignore linefeeds
						continue;
					}

					if (current == 0x0d) {

						// We have a carriage return so if the line is non-empty, submit it.
						if (line.length() > 0) {

							processGpsLine(line.toString());

							// Start a new line.
							line.setLength(0);
						} else {
							// Line was empty so sleep for a while
							try {
								LOGGER.debug("GPS sleep");
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								return;
							}
						}
					} else {
						// Append character read to our buffer.
						line.append((char) current);
					}
				}
			}
		}
	}
}
