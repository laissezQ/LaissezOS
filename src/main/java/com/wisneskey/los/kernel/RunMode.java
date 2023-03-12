package com.wisneskey.los.kernel;

/**
 * Enumerated type used to indicate where the application is being run so that
 * it can configuration itself appropriately.
 */
public enum RunMode {
	DEV("Off chair development where chair system interfaces are simulated."),
	PI2B("Raspberry PI2B prototyping (limited display support).");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

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

	public String getDescription() {
		return description;
	}
}