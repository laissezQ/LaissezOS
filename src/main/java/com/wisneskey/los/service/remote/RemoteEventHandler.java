package com.wisneskey.los.service.remote;

/**
 * Interface for the object supplied to a remote driver so that it can report
 * events from the remote control.
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
public interface RemoteEventHandler {

	/**
	 * Method invoked when a remote button is pressed.
	 * 
	 * @param buttonId Id of the remote button that was pressed.
	 */
	void buttonPressed(RemoteButtonId buttonId);

	/**
	 * Method invoked when a remote button is released.
	 * 
	 * @param buttonId id of the remote button that was released. 
	 */
	void buttonReleased(RemoteButtonId buttonId);
}
