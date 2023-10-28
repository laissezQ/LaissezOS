package com.wisneskey.los.service.display;

/**
 * Enumerated type for the displays used by the operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum DisplayId {

	CP("Control Panel"),
	HUD("Heads Up Display");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Short description for the display.
	 */
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private DisplayId(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}
}
