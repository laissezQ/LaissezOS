package com.wisneskey.los.boot;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.profile.ProfileService;

/**
 * Initial configuration that drive the boot process.
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
	 * @param runMode
	 *          Run mode to use for operating system run.
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
	 * @param profileName
	 *          Name of profile to use for configuring the operating system.
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
}
