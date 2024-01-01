package com.wisneskey.los.service.display;

/**
 * Enumerated type defining the scenes that are available for display.
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
public enum SceneId {

	//
	// Control Panel scenes:
	//
	CP_ABOUT_SCREEN(DisplayId.CP, "AboutScreen"),
	CP_AUDIO_SCREEN(DisplayId.CP, "AudioScreen"),
	CP_BOOT_SCREEN(DisplayId.CP, "BootScreen"),
	CP_CHAP_SCREEN(DisplayId.CP, "ChapScreen"),
	CP_LOCK_SCREEN(DisplayId.CP, "LockScreen"),
	CP_MAIN_SCREEN(DisplayId.CP, "MainScreen"),
	CP_SCRIPT_SCREEN(DisplayId.CP, "ScriptScreen"),
	CP_SPLASH_SCREEN(DisplayId.CP, "SplashScreen"),
	CP_SYSTEM_SCREEN(DisplayId.CP, "SystemScreen"),

	//
	// Heads Up Display scenes:
	//
	HUD_CHAP_SCREEN(DisplayId.HUD, "ChapScreen"),
	HUD_MAIN_SCREEN(DisplayId.HUD, "MainScreen"),
	HUD_SPLASH_SCREEN(DisplayId.HUD, "SplashScreen"),
	HUD_LOCK_SCREEN(DisplayId.HUD, "LockScreen");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Id of the display the scene is for.
	 */
	private DisplayId displayId;

	/**
	 * Name of the FXML file resource that defines the scene.
	 */
	private String fxmlName;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SceneId(DisplayId displayId, String fxmlName) {
		this.displayId = displayId;
		this.fxmlName = fxmlName;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the id of the display the scene can be shown on.
	 * 
	 * @return Id of display to show scene on.
	 */
	public DisplayId getDisplayId() {
		return displayId;
	}

	/**
	 * Returns the name of the FXML file containing the scene definition.
	 * 
	 * @return Name of scene's FXML file.
	 */
	public String getFxmlName() {
		return fxmlName;
	}
}
