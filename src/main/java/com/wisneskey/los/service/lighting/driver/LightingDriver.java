package com.wisneskey.los.service.lighting.driver;

import com.wisneskey.los.service.profile.model.Profile;

/**
 * Interface defining an implementation of a lighting system based on a
 * particular technology.
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
public interface LightingDriver {

	/**
	 * Initialize the lighting driver based on the profile.
	 * 
	 * @param profile Profile with settings for the lighting driver.
	 */
	void initialize(Profile profile);

	/**
	 * Shutdown the driver when terminating the service.
	 */
	void terminate();

	/**
	 * Initial test implementation.
	 */
	void runTest();
}
