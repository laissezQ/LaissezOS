package com.wisneskey.los.service.script;

/**
 * Enumerated type defining the scripts that are available to be executed.
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
public enum ScriptId {

	ABOUT_SCREEN_OPEN("About screen: open", "AboutScreen-Open"),
	ABOUT_SCREEN_CLOSE("About screen: close", "AboutScreen-Close"),
	AUDIO_SCREEN_OPEN("Audio screen: open","AudioScreen-Open"),
	AUDIO_SCREEN_CLOSE("Audio screen: close","AudioScreen-Close"),
	BOOT_FAST("Boot sequence: fast", "Boot-Fast"),
	BOOT_DEV("Boot sequence: development", "Boot-DEV"),
	CHAP_SCREEN_OPEN("Chap screen: open", "ChapScreen-Open"),
	CHAP_SCREEN_CLOSE("Chap screen: close","ChapScreen-Close"),
	REMOTE_LOCK("Remote: lock chair","Remote-Lock"),
	REMOTE_UNLOCK("Remote: unlock chair","Remote-Unlock"),
	SECURITY_LOCK("Security screen: open", "Security-Lock"),
	SECURITY_UNLOCK("Security screen: PIN correct", "Security-Unlock"),
	SECURITY_UNLOCK_FAILED("Security screen: PIN invalid", "Security-UnlockFailed"),
	SCRIPT_SCREEN_OPEN("Script screen: open", "ScriptScreen-Open"),
	SCRIPT_SCREEN_CLOSE("Script screen: close", "ScriptScreen-Close"),
	SYSTEM_SCREEN_OPEN("System screen: open", "SystemScreen-Open"),
	SYSTEM_SCREEN_CLOSE("System screen: close", "SystemScreen-Close"),
	SYSTEM_SHUTDOWN("System screen: shutdown", "Shutdown");

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
