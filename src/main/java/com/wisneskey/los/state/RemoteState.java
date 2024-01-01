package com.wisneskey.los.state;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * State object for the state of the remote control interface.
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
public interface RemoteState extends State {

	/**
	 * Property that is true when button A is pressed.
	 * 
	 * @return True if button A is currently pressed; false otherwise.
	 */
	ReadOnlyBooleanProperty buttonA();
	
	/**
	 * Property that is true when button B is pressed.
	 * 
	 * @return True if button B is currently pressed; false otherwise.
	 */
	ReadOnlyBooleanProperty buttonB();
}
