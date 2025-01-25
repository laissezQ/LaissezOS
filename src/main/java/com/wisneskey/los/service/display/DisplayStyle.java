package com.wisneskey.los.service.display;

/**
 * Enumerated type defining the available styles for the displays.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
