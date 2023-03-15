package com.wisneskey.los.service.display;

/**
 * Enumerated type defining the available styles for the displays.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum DisplayStyle {
	
	NONE("Base JavaFX Mode", null),
	LAISSEZ_OS("Laissex Boy Mode", "laissez-os.css"),
	PRIMER_LIGHT("Primer Light Mode", "primer-light.css"),
	PRIMER_DARK("Primer Dark Mode", "primer-dark.css"),
	NORD_LIGHT("Nord Light Mode", "nord-light.css"),
	NORD_DARK("Nord Dark Mode", "nord-dark.css");
	
	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Name of the style.
	 */
	private String name;

	/**
	 * Name of the CSS file for the stylesheet.
	 */
	private String cssFile;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private DisplayStyle(String name, String cssFile) {
		this.name = name;
		this.cssFile = cssFile;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public String getCSSFile() {
		return cssFile;
	}
}
