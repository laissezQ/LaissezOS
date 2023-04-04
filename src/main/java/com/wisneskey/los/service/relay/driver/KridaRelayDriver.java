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
	
	@Override
	public Map<RelayId, Boolean> initialize(Profile profile) {

		LOGGER.debug("Initializing KRIDA I2C Board relay driver...");
		
		int address = DEFAULT_I2C_ADDRESS;
		if( profile.getRelayAddress() != null ) {
			address = Integer.decode(profile.getRelayAddress());
		}
		
		LOGGER.trace("Using I2C address: " + Integer.toHexString(address));
		
		Context pi4jContext = Pi4J.newAutoContext();
		I2CProvider i2cProvider = pi4jContext.provider("linuxfs-i2c");
		I2CConfig i2cConfig = I2C.newConfigBuilder(pi4jContext).id("krida").bus(I2C_BUS).device(address).build();
		
		try {
			kridaI2C = i2cProvider.create(i2cConfig);
		} catch( Exception e ) {
			throw new LaissezException("Failed to connect to I2C relay driver board.");
		}
		
		// kridaI2C.write((byte) 254);
		
		int state = kridaI2C.read();
				
		Map<RelayId, Boolean> stateMap = new HashMap<>();
		for( RelayId id : RelayId.values()) {
			stateMap.put(id, (state & (2 >> id.getIndex())) != 0);
		}
		
		LOGGER.info("Initial state: " + state + " map=" + stateMap);
		return stateMap;
	}
}
