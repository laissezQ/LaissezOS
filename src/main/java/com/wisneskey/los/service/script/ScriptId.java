package com.wisneskey.los.service.script;

/**
 * Enumerated type defining the scripts that are available to be executed.
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
public enum ScriptId {

	AUDIO_SCREEN_OPEN("But this one goes to eleven!","AudioScreen-Open"),
	AUDIO_SCREEN_CLOSE("The audience is now deaf.","AudioScreen-Close"),
	BOOT_FAST("Fasted possible boot sequence.", "Boot-Fast"),
	BOOT_DEV("Test boot sequence for development.", "Boot-DEV"),
	CHAP_SCREEN_OPEN("You'll shoot your eye out kid!", "ChapScreen-Open"),
	REMOTE_LOCK("Denied!","Remote-Lock"),
	REMOTE_UNLOCK("Your wish is granted...","Remote-Unlock"),
	CHAP_SCREEN_CLOSE("Finally, a responsible adult.","ChapScreen-Close"),
	SECURITY_LOCK("Not for you!", "Security-Lock"),
	SECURITY_UNLOCK("You knew the secret knocK!", "Security-Unlock"),
	SECURITY_UNLOCK_FAILED("Invalid PIN entered for chair unlock.", "Security-UnlockFailed"),
	SYSTEM_SCREEN_OPEN("Is this an easter egg?", "SystemScreen-Open"),
	SYSTEM_SCREEN_CLOSE("Let's get back to work!", "SystemScreen-Close"),
	SHUTDOWN("The party is over.", "Shutdown");

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
