package com.wisneskey.los.service.display.controller;

import com.wisneskey.los.service.remote.RemoteButtonId;

/**
 * Interface designating a controller for one of the defined scenes for the system
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
public interface SceneController {

	/**
	 * Method invoked when a scene is shown.
	 */
	void sceneShown();
	
	/**
	 * Method invoked by the display service when a remote control button press is
	 * detected.
	 * 
	 * @param buttonId Id of the remote control button that was pressed.
	 */
	void remoteButtonPressed(RemoteButtonId buttonId);
}
