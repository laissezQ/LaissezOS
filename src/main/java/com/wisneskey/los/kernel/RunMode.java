package com.wisneskey.los.kernel;

/**
 * Enumerated type used to indicate where the application is being run so that
 * it can configuration itself appropriately.
 */
public enum RunMode {
	DEV("Off chair development where chair system interfaces are simulated."),
	PI2B_CP("Raspberry PI2B prototyping (control panel display)."),
	PI2B_HUD("Raspberry PI2B prototyping (heads up display).");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Shot description of the run mode.
	 */
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private RunMode(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the short description of the run mode.
	 * 
	 * @return Run mode's description.
	 */
	public String getDescription() {
		return description;
	}
}