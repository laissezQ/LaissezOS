package com.wisneskey.los.state;

import com.wisneskey.los.service.relay.RelayId;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * State object for the state of the relays.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public interface RelayState extends State {

	/**
	 * Returns the state property for the relay with the given if.
	 * 
	 * @param  relayId Id of the relay to return the state property for.
	 * 
	 * @return         Boolean state property for the specified relay.
	 */
	ReadOnlyBooleanProperty getState(RelayId relayId);
}
