package com.wisneskey.los.service.relay.driver;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Dummy relay driver used in development environments with no actual relay
 * access.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
public class DummyRelayDriver implements RelayDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyRelayDriver.class);

	// ----------------------------------------------------------------------------------------
	// RelayDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public Map<RelayId, Boolean> initialize(Profile profile) {

		LOGGER.debug("Initialized dummy relay driver.");

		Map<RelayId, Boolean> stateMap = new EnumMap<>(RelayId.class);
		for (RelayId id : RelayId.values()) {
			stateMap.put(id, Boolean.FALSE);
		}

		return stateMap;
	}

	@Override
	public void turnOn(RelayId relayId) {
		// Nothing to do in dummy driver.
	}

	@Override
	public void turnOff(RelayId relayid) {
		// Nothing to do in dummy driver.
	}
}
