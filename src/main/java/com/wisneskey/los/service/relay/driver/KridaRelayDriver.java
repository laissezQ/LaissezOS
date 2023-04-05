package com.wisneskey.los.service.relay.driver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Driver for the KRIDA 8 channel I2C relaay board.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class KridaRelayDriver implements RelayDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KridaRelayDriver.class);

	/**
	 * I2C bus number the board is on.
	 */
	private static final int I2C_BUS = 1;

	/**
	 * Default I2C address for the relay driver board.
	 */
	private static final int DEFAULT_I2C_ADDRESS = 0x27;

	/**
	 * I2C connection to the board.
	 */
	private I2C kridaI2C;

	// ----------------------------------------------------------------------------------------
	// RelayDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public Map<RelayId, Boolean> initialize(Profile profile) {

		LOGGER.debug("Initializing KRIDA I2C Board relay driver...");

		int address = DEFAULT_I2C_ADDRESS;
		if (profile.getRelayAddress() != null) {
			address = Integer.decode(profile.getRelayAddress());
		}

		LOGGER.trace("Using I2C address: " + Integer.toHexString(address));

		Context pi4jContext = Pi4J.newAutoContext();
		I2CProvider i2cProvider = pi4jContext.provider("linuxfs-i2c");
		I2CConfig i2cConfig = I2C.newConfigBuilder(pi4jContext).id("krida").bus(I2C_BUS).device(address).build();

		try {
			kridaI2C = i2cProvider.create(i2cConfig);
		} catch (Exception e) {
			throw new LaissezException("Failed to connect to I2C relay driver board.");
		}

		int state = kridaI2C.read();

		Map<RelayId, Boolean> stateMap = new HashMap<>();
		for (RelayId id : RelayId.values()) {
			stateMap.put(id, (state & getMask(id)) != 0);
		}

		LOGGER.info("Initial state: " + state + " map=" + stateMap);
		return stateMap;
	}

	@Override
	public void turnOn(RelayId relayId) {

		// Get the state and mask out the bit for the relay being turned on.
		int state = kridaI2C.read();

		int enableMask = 0xFF ^ getMask(relayId);
		int newState = state & enableMask;

		LOGGER.debug("Turning relay on: id={} oldState={} enableMask={} newState={}", relayId, state, enableMask, newState);
		kridaI2C.write((byte) newState);
	}

	@Override
	public void turnOff(RelayId relayId) {

		// Get the state and use the mask for the bit for the relay being turned on.
		int state = kridaI2C.read();

		int disableMask = getMask(relayId);
		int newState = state | disableMask;

		LOGGER.debug("Turning relay off: id={} oldState={} disableMask={} newState={}", relayId, state, disableMask,
				newState);
		kridaI2C.write((byte) newState);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Generate the bit mask for the specified relay.
	 * 
	 * @param relayId
	 *          Id of the relay to generate the mask for.
	 * @return Bit mask for designated relay.
	 */
	private int getMask(RelayId relayId) {
		return 1 << relayId.getIndex();
	}
}
