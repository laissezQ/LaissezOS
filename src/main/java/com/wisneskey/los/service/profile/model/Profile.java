package com.wisneskey.los.service.profile.model;

import java.util.Set;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.display.DisplayStyle;
import com.wisneskey.los.service.script.ScriptId;

/**
 * Class defining the root profile object used to manage an individual
 * configuration for the operating system.
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
public class Profile {

	// ----------------------------------------------------------------------------------------
	// General defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default to a fast boot.
	 */
	public static final ScriptId DEFAULT_BOOT_SCRIPT = ScriptId.BOOT_FAST;

	// ----------------------------------------------------------------------------------------
	// Audio service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default volume for the audio service in normal chair operation.
	 */
	private static final int DEFAULT_CHAIR_VOLUME = 8;

	/**
	 * Default volume for the audio service when in chap mode.
	 */
	private static final int DEFAULT_CHAP_MODE_VOLUME = 8;

	// ----------------------------------------------------------------------------------------
	// Display service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default style for the displays.
	 */
	public static final DisplayStyle DEFAULT_DISPLAY_STYLE = DisplayStyle.LAISSEZ_OS;

	// ----------------------------------------------------------------------------------------
	// Location service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default latitude for initial location.
	 */
	private static final double DEFAULT_LATITUDE = 35.6420206;

	/**
	 * Default longitude for initial location.
	 */
	private static final double DEFAULT_LONGITUDE = -105.9889903;

	/**
	 * Default altitude for initial location.
	 */
	private static final double DEFAULT_ALTITUDE = 2044.982;

	/**
	 * Default value for flag indicating map service can fetch online map tiles.
	 */
	private static final boolean DEFAULT_MAP_ONLINE = true;

	// ----------------------------------------------------------------------------------------
	// Map service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default location for the map tile store directory.
	 */
	private static final String DEFAULT_TILE_STORE_PATH = "./.map_tile_store";

	// ----------------------------------------------------------------------------------------
	// General settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Unique id for the profile.
	 */
	private String profileId;

	/**
	 * Description of the profile.
	 */
	private String description;

	/**
	 * Run modes the profile is compatible with.
	 */
	private Set<RunMode> supportedRunModes;

	/**
	 * Script to run at boot.
	 */
	private ScriptId bootScript = DEFAULT_BOOT_SCRIPT;

	// ----------------------------------------------------------------------------------------
	// Audio service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Volume of the audio in normal chair operation.
	 */
	private int volume = DEFAULT_CHAIR_VOLUME;

	/**
	 * Volume of the chair in chap mode.
	 */
	private int chapModeVolume = DEFAULT_CHAP_MODE_VOLUME;

	// ----------------------------------------------------------------------------------------
	// Display service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Style for the UI on the displays.
	 */
	private DisplayStyle displayStyle = DEFAULT_DISPLAY_STYLE;

	// ----------------------------------------------------------------------------------------
	// Lighting service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Address of the WLED host for controlling the lighting.
	 */
	private String wledHostAddress;

	/**
	 * Flag to force the user of the real lighting driver even in development.
	 * This is possible since the ESP32 can run independently of the rest of the
	 * system.
	 */
	private boolean useRealLighting;

	// ----------------------------------------------------------------------------------------
	// Location service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Latitude for initial chair location.
	 */
	private double initialLatitude = DEFAULT_LATITUDE;

	/**
	 * Longitude for initial chair location.
	 */
	private double initialLongitude = DEFAULT_LONGITUDE;

	/**
	 * Altitude for initial chair location.
	 */
	private double initialAltitude = DEFAULT_ALTITUDE;

	// ----------------------------------------------------------------------------------------
	// Map service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Flag indicating if the map service can fetch tiles online or can only use
	 * the local cache because it is offline.
	 */
	private boolean mapOnline = DEFAULT_MAP_ONLINE;

	/**
	 * Directory for the local map cache.
	 */
	private String tileStorePath = DEFAULT_TILE_STORE_PATH;

	// ----------------------------------------------------------------------------------------
	// Security service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * 4 digit PIN code to unlock chair.
	 */
	private String pinCode;

	// ----------------------------------------------------------------------------------------
	// General property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the unique id of the profile.
	 * 
	 * @return Unique id for the profile.
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Returns a basic description of the profile.
	 * 
	 * @return Short description of the profile.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the set of run modes that the profile is compatible with.
	 * 
	 * @return Set of run modes the profile is compatible with.
	 */
	public Set<RunMode> getSupportedRunModes() {
		return supportedRunModes;
	}

	/**
	 * Returns the script to run for the boot sequence.
	 * 
	 * @return Id of the script to run for boot sequence.
	 */
	public ScriptId getBootScript() {
		return bootScript;
	}

	// ----------------------------------------------------------------------------------------
	// Audio service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the volume of chair in normal operating mode.
	 * 
	 * @return Volume of chair from 0 to 11.
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * Returns the volume of the chair in chap mode.
	 * 
	 * @return Volume of chair from 0 to 11.
	 */
	public int getChapModeVolume() {
		return chapModeVolume;
	}

	// ----------------------------------------------------------------------------------------
	// Display service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the style to use for the UI displays.
	 * 
	 * @return Display style for the UI.
	 */
	public DisplayStyle getDisplayStyle() {
		return displayStyle;
	}

	// ----------------------------------------------------------------------------------------
	// Lighting service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the name or address of the host for the WLED lighting controller.
	 * 
	 * @return Host name or IP address for the WLED lighting controller.
	 */
	public String getWledHostAddress() {
		return wledHostAddress;
	}

	/**
	 * Returns a flag indicating if the real lighting driver should be used
	 * instead of the dummy lighting driver in the development mode.
	 * 
	 * @return True if real lighting driver should be used regardless of chair
	 *         mode.
	 */
	public boolean getUseRealLighting() {
		return useRealLighting;
	}

	// ----------------------------------------------------------------------------------------
	// Location service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the initial latitude to be reported from the location service.
	 * 
	 * @return Latitude of the initial chair position.
	 */
	public double getInitialLatitude() {
		return initialLatitude;
	}

	/**
	 * Returns the initial longitude to be reported from the location service.
	 * 
	 * @return Longitude of the initial chair position.
	 */
	public double getInitialLongitude() {
		return initialLongitude;
	}

	/**
	 * Returns the initial altitude to be reported from the location service.
	 * 
	 * @return Altitude of the initial chair position.
	 */
	public double getInitialAltitude() {
		return initialAltitude;
	}

	// ----------------------------------------------------------------------------------------
	// Map service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns if map service is allowed to fetch tiles online.
	 * 
	 * @return True if map service can fetch tiles online; false otherwise.
	 */
	public boolean getMapOnline() {
		return mapOnline;
	}

	/**
	 * Return the directory to use for the local file system persistence of the
	 * map cache.
	 * 
	 * @return Name to use to select the map cache configuration to use.
	 */
	public String getTileStorePath() {
		return tileStorePath;
	}

	// ----------------------------------------------------------------------------------------
	// Security service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * 4 digit numeric code required to unlock chair.
	 * 
	 * @return Code required to unlock chair.
	 */
	public String getPinCode() {
		return pinCode;
	}
}
