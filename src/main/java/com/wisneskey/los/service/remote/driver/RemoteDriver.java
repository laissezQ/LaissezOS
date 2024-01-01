package com.wisneskey.los.service.remote.driver;

import com.wisneskey.los.service.remote.RemoteEventHandler;

/**
 * Interface defining a driver for watching for the activities of a remote control.
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
public interface RemoteDriver {

	/**
	 * Initializes the event handler with a supplied handler that it can call
	 * to let the remote service know about remote events.
	 * 
	 * @param eventHandler Event handler to use for reporting remote events.
	 */
	void initialize(RemoteEventHandler eventHandler);
	
	/**
	 * Terminates the event handler.
	 */
	void terminate();
}
