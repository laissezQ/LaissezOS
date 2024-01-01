package com.wisneskey.los.service.relay.driver;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Driver for the KRIDA 8 channel I2C relaay board.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
public class KridaRelayDriver implements RelayDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KridaRelayDriver.class);

	/**
	 * I2C bus number the board is on.
	 */
	private static final int I2C_BUS = 1;

	/**
	 * Default I2C address for the relay driver board A.
	 */
	private static final int I2C_ADDRESS_BOARD_A = 0x27;

	/**
	 * Default I2C address for the relay driver board A.
	 */
	private static final int I2C_ADDRESS_BOARD_B = 0x23;

	/**
	 * I2C connection to the board A.
	 */
	private I2C boardA;

	/**
	 * I2C connection to the board B.
	 */
	private I2C boardB;

	// ----------------------------------------------------------------------------------------
	// RelayDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public Map<RelayId, Boolean> initialize(Profile profile) {

		LOGGER.debug("Initializing KRIDA I2C Board relay driver...");
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Board A I2C address: {}", Integer.toHexString(I2C_ADDRESS_BOARD_A));
			LOGGER.trace("Board B I2C address: {}", Integer.toHexString(I2C_ADDRESS_BOARD_B));
		}

		Context pi4jContext = Kernel.kernel().getPi4jContext();
		I2CProvider i2cProvider = pi4jContext.provider("linuxfs-i2c");

		I2CConfig i2cConfigA = I2C.newConfigBuilder(pi4jContext).id("relayBoardA").bus(I2C_BUS).device(I2C_ADDRESS_BOARD_A)
				.build();
		I2CConfig i2cConfigB = I2C.newConfigBuilder(pi4jContext).id("relayBoardB").bus(I2C_BUS).device(I2C_ADDRESS_BOARD_B)
				.build();

		try {
			boardA = i2cProvider.create(i2cConfigA);
			boardB = i2cProvider.create(i2cConfigB);
		} catch (Exception e) {
			throw new LaissezException("Failed to create I2C connection to driver board.", e);
		}

		Map<RelayId, Boolean> stateMap = new EnumMap<>(RelayId.class);
		for (RelayId relayId : RelayId.values()) {

			// Make sure everything is off to start.
			turnOff(relayId);
			stateMap.put(relayId, Boolean.FALSE);
		}

		return stateMap;
	}

	@Override
	public void turnOn(RelayId relayId) {

		// Get the state and mask out the bit for the relay being turned on.
		I2C board = getBoard(relayId);
		int state = board.read();

		int enableMask = 0xFF ^ getMask(relayId);
		int newState = state & enableMask;

		LOGGER.debug("Turning relay on: id={} oldState={} enableMask={} newState={}", relayId, state, enableMask, newState);
		board.write((byte) newState);
	}

	@Override
	public void turnOff(RelayId relayId) {

		// Get the state and use the mask for the bit for the relay being turned on.
		I2C board = getBoard(relayId);
		int state = board.read();

		int disableMask = getMask(relayId);
		int newState = state | disableMask;

		LOGGER.debug("Turning relay off: id={} oldState={} disableMask={} newState={}", relayId, state, disableMask,
				newState);
		board.write((byte) newState);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Get the appropriate board connector for the relay id.
	 */
	private I2C getBoard(RelayId relayId) {
		return relayId.getIndex() < 8 ? boardA : boardB;
	}

	/**
	 * Generate the bit mask for the specified relay.
	 * 
	 * @param  relayId Id of the relay to generate the mask for.
	 * @return         Bit mask for designated relay.
	 */
	private int getMask(RelayId relayId) {
		// Modulo the index since they are spread across boards with 8 relays each.
		return 1 << (relayId.getIndex() % 8);
	}
}
