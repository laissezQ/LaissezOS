package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
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
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	// Driver board A relays:
	BAR_LOWER(0, "Lower armrest bar.."),
	BAR_RAISE(1, "Raise armest bar."),
	RELAY_3(2, "Third relay."),
	RELAY_4(3, "Fourth relay."),
	BAR_PUMP(4, "Turn on bar water pump."),
	BAR_LIGHT(6, "Turn on bar lighting."),
	FLUX_CAPACITOR(7, "Turn on Flux Capacitor."),

	// Driver board B relays:
	AMPLIFIER(8, "Audio amplifier on."),
	RELAY_9(9, "Tenth relay."),
	RELAY_10(10, "Eleventh relay."),
	RELAY_11(11, "Twelf relay."),
	RELAY_12(12, "Thirteenth relay."),
	RELAY_13(13, "Fourteenth relay."),
	LIGHTING_A(14, "LED Lighting for A side."),
	LIGHTING_B(15, "LED Lighting for B side.");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private int index;
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private RelayId(int index, String description) {
		this.index = index;
		this.description = description;
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
}
