package com.wisneskey.los.boot;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.profile.ProfileService;

/**
 * Initial configuration that drive the boot process.
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
 * 
 * @author paul.wisneskey@gmail.com
 */
public class BootConfiguration {

	/**
	 * Default run mode.
	 */
	public static final RunMode DEFAULT_RUN_MODE = RunMode.DEV;

	/**
	 * Mode the LBOS is running under.
	 */
	private RunMode runMode = DEFAULT_RUN_MODE;

	/**
	 * Optional name of the profile to use to in place of default profile.
	 */
	private String profileName = ProfileService.DEFAULT_PROFILE_ID;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the selected run mode.
	 * 
	 * @return Run mode specified for the operating system launch.
	 */
	public RunMode getRunMode() {
		return runMode;
	}

	/**
	 * Sets the run mode to be used for the kernel.
	 * 
	 * @param runMode Run mode to use for operating system run.
	 */
	public void setRunMode(RunMode runMode) {
		this.runMode = runMode;
	}

	/**
	 * Returns the desired profile to be used for the configuration of the
	 * operating system.
	 * 
	 * @return Name of the profile to be used for configuring the operating
	 *         system.
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * Sets the name of the profile to use to configure the operating system.
	 * 
	 * @param profileName Name of profile to use for configuring the operating
	 *                      system.
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
}
