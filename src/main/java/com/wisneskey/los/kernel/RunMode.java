package com.wisneskey.los.kernel;

/**
 * Enumerated type used to indicate where the application is being run so that
 * it can configuration itself appropriately.
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
 */
public enum RunMode {

	DEV(Platform.OS_X, "Off chair development where chair system interfaces are simulated."),
	CHAIR(Platform.RASPBERRY_PI, "Raspberry PI4B on chair (both displays).");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Underlying platform for the run mode.
	 */
	private Platform platform;

	/**
	 * Shot description of the run mode.
	 */
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private RunMode(Platform platform, String description) {
		this.platform = platform;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the underlying platform for the run mode.
	 * 
	 * @return Platform for the run mode.
	 */
	public Platform getPlatform() {
		return platform;
	}

	/**
	 * Returns the short description of the run mode.
	 * 
	 * @return Run mode's description.
	 */
	public String getDescription() {
		return description;
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Enumerated type for the underlying platform the OS is being run on in the
	 * run mode.
	 * 
	 * @author paul.wisneskey@gmail.com
	 *
	 */
	public enum Platform {
		RASPBERRY_PI,
		OS_X
	}
}