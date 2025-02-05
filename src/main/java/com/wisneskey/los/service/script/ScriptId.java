package com.wisneskey.los.service.script;

/**
 * Enumerated type defining the scripts that are available to be executed.
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
public enum ScriptId {

	ABOUT_SCREEN_OPEN("About screen: open", "AboutScreen-Open"),
	ABOUT_SCREEN_CLOSE("About screen: close", "AboutScreen-Close"),
	AUDIO_SCREEN_OPEN("Audio screen: open", "AudioScreen-Open"),
	AUDIO_SCREEN_CLOSE("Audio screen: close", "AudioScreen-Close"),
	BAR_LOWER("Bar: Lower", "Bar-Lower"),
	BAR_RAISE("Bar: Raise", "Bar-Raise"),
	BOOT_CHAIR("Boot sequence: chair", "Boot-Chair"),
	BOOT_DEV("Boot sequence: development", "Boot-DEV"),
	CHAP_SCREEN_OPEN("Chap screen: open", "ChapScreen-Open"),
	CHAP_SCREEN_CLOSE("Chap screen: close", "ChapScreen-Close"),
	EFFECT_SCREEN_OPEN("Effect screen: open", "EffectScreen-Open"),
	EFFECT_SCREEN_CLOSE("Effect screen: close", "EffectScreen-Close"),
	GAME_SCREEN_OPEN("Game screen: open", "GameScreen-Open"),
	LIGHTING_SCREEN_OPEN("Lighting screen: open", "LightingScreen-Open"),
	LIGHTING_SCREEN_CLOSE("Lighting screen: close", "LightingScreen-Close"),
	MUSIC_SCREEN_OPEN("Music screen: open", "MusicScreen-Open"),
	MUSIC_SCREEN_CLOSE("Music screen: close", "MusicScreen-Close"),
	REMOTE_LOCK("Remote: lock chair", "Remote-Lock"),
	REMOTE_UNLOCK("Remote: unlock chair", "Remote-Unlock"),
	SECURITY_LOCK("Security screen: open", "Security-Lock"),
	SECURITY_UNLOCK("Security screen: PIN correct", "Security-Unlock"),
	SECURITY_UNLOCK_FAILED("Security screen: PIN invalid", "Security-UnlockFailed"),
	SCRIPT_SCREEN_OPEN("Script screen: open", "ScriptScreen-Open"),
	SCRIPT_SCREEN_CLOSE("Script screen: close", "ScriptScreen-Close"),
	SKETCH_OPEN("Sketch: open", "Sketch-Open"),
	SYSTEM_SCREEN_OPEN("System screen: open", "SystemScreen-Open"),
	SYSTEM_SCREEN_CLOSE("System screen: close", "SystemScreen-Close"),
	SYSTEM_EXIT("System screen: exit OS", "System-Exit");
	;

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Description of the script.
	 */
	private String description;

	/**
	 * Name of the script file to load from the resources.
	 */
	private String name;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ScriptId(String description, String name) {
		this.description = description;
		this.name = name;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
}
