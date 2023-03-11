package com.wisneskey.los.boot;

import com.wisneskey.los.kernel.LBOSKernel.RunMode;

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
	 * Optional name of the profile to use to in place of currently active profile.
	 */
	private String profileName;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public RunMode getRunMode() {
		return runMode;
	}

	public void setRunMode(RunMode runMode) {
		this.runMode = runMode;
	}
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
}
