package com.wisneskey.los.state;

import com.wisneskey.los.service.ServiceId;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

/**
 * State object for the overall state of the chair.
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
public interface ChairState extends State {

	// ----------------------------------------------------------------------------------------
	// Top level state properties.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the master state of the chair.
	 * 
	 * @return Master state property of the chair.
	 */
	ReadOnlyObjectProperty<MasterState> masterState();

	/**
	 * Message property that is used to feed messages into the chair display
	 * system. Display scenes can listen to the property to receive messages as
	 * they are set. It is up to them to decide how to display them.
	 * 
	 * @return Message property for the chair.
	 */
	ReadOnlyStringProperty message();

	// ----------------------------------------------------------------------------------------
	// Service state properties.
	// ----------------------------------------------------------------------------------------

	<T extends State> T getServiceState(ServiceId serviceId);

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	public enum MasterState {
		BOOTING,
		RUNNING,
		LOCKED,
		CHAP_MODE
	}
}
