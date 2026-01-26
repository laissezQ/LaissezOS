package com.wisneskey.los.service.relay.driver;

import java.util.Map;

import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Interface defining an implementation of relay driver interface that can work
 * with a particular relay board.
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
public interface RelayDriver {

	/**
	 * Initialize the relay driver based on the profile.
	 * 
	 * @param profile Profile with settings for the relay driver.
	 */
	Map<RelayId, Boolean> initialize(Profile profile);

	/**
	 * Turn on the specified relay.
	 * 
	 * @param relayId Id of the relay to turn on.
	 */
	void turnOn(RelayId relayId);

	/**
	 * Turn off the specified relay.
	 * 
	 * @param relayid Id of the relay to turn off.
	 */
	void turnOff(RelayId relayid);
}
