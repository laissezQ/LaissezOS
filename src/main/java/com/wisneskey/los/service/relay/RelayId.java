package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
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
public enum RelayId {

	// Driver board A relays:
	BAR_LOWER(0, "Lower armrest bar", false),
	BAR_RAISE(1, "Raise armest bar", false),
	POLICE_POWER(3, "Police Lights", true),
	MARQUEE(4, "LED Marquee", true),
	BAR_PUMP(5, "Activate Water Tap", false),
	BAR_LIGHT(6, "Bar Lighting", true),
	FLUX_CAPACITOR(7, "Flux Capacitor", true),

	// Driver board B relays:
	AMPLIFIER(8, "Audio amplifier ", true),
	POLICE_MODE(13, "Change Police Strobe", false),
	SIDE_LIGHTING(14, "Side Panel Lighting.", true),
	UNDER_LIGHTING(15, "Under Chair Lighting.", true);

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private int index;
	private String description;
	private boolean togglable;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private RelayId(int index, String description, boolean togglable) {
		this.index = index;
		this.description = description;
		this.togglable = togglable;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public int getIndex() {
		return index;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isTogglable() {
		return togglable;
	}
}