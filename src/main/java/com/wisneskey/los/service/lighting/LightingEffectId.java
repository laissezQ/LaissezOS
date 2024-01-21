package com.wisneskey.los.service.lighting;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumerated type designating a particular effect to plan on the Chair's
 * lighting controller.
 *
 * Copyright (C) 2023 Paul Wisneskey
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
public enum LightingEffectId {
	ALL_OFF("All Off", "All Lights Off", false),
	BLENDS("Blends", "Colors Blended", true),
	CHASE2("Chase 2", "Two Color Chase", true),
	CHASE3("Chase 3", "Three Color Chase", true),
	COLORFUL("Colorful", "Slow Color Circle", true),
	FLOW("Flow", "Rainbow Flow", true),
	GLITTER("Glitter", "Solid Glitter", true),
	HEARTBEAT("Heartbeat", "Heart Pulse", true),
	JUGGLE("Juggle", "Juggling Balls", true),
	LOADING("Loading", "Loading Sweep", true),
	MARQUEE("Marquee", "Marquee", true),
	NOISE("Noise", "Phased Noise", true),
	OCEAN("Ocean", "Ocean Waves", true),
	PHASED("Phases", "Phased Colors", true),
	PIXELS("Pixels", "Pixel Cycle", true),
	PLASMOID("Plasmoid", "Plasmoid", true),
	PRIDE("Pride", "Circling Rainbow", true),
	RAINBOW("Rainbow", "Rainbow", true),
	RUNNING("Running", "Running Color", true),
	SOLID("Solid", "Solid Color", true),
	TETRIX("Tetrix", "Tetrix", true),
	TWINKLE_CAT("Twinkle Cat", "Twinkle Cat", true),
	TWINKLES("Twinkles", "Twinkling Colors", true),
	WAVE("Wave", "Color Wave", true);

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String shortName;
	private String description;
	private boolean allowInChapMode;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private LightingEffectId(String shortName, String description, boolean allowInChapMode) {
		this.shortName = shortName;
		this.description = description;
		this.allowInChapMode = allowInChapMode;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getShortName() {
		return shortName;
	}

	public String getDescription() {
		return description;
	}

	public boolean getAllowInChapMode() {
		return allowInChapMode;
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the set of all effects allowed in chap mode.
	 * 
	 * @return Set of effect id's for all effects allowed in chap mode.
	 */
	public static Set<LightingEffectId> chapModeEffects() {

		return Arrays.stream(LightingEffectId.values()).filter(LightingEffectId::getAllowInChapMode)
				.collect(Collectors.toSet());
	}
}
