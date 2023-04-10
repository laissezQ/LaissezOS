package com.wisneskey.los.kernel;

/**
 * Enumerated type used to indicate where the application is being run so that
 * it can configuration itself appropriately.
 */
public enum RunMode {

	DEV(Platform.OsX, "Off chair development where chair system interfaces are simulated."),
	PI2B_CP(Platform.RaspberryPi, "Raspberry PI2B prototyping (control panel display only)."),
	PI2B_HUD(Platform.RaspberryPi, "Raspberry PI2B prototyping (heads up display only).");

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
		RaspberryPi,
		OsX
	}
}