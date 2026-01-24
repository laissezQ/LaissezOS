package com.wisneskey.los.service.profile.model;

import java.util.Map;
import java.util.Set;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.display.DisplayStyle;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.script.ScriptId;

/**
 * Class defining the root profile object used to manage an individual
 * configuration for the operating system.
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
public class Profile {

	// ----------------------------------------------------------------------------------------
	// General defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default to the chair boot.
	 */
	public static final ScriptId DEFAULT_BOOT_SCRIPT = ScriptId.BOOT_CHAIR;

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
	private static final DisplayStyle DEFAULT_DISPLAY_STYLE = DisplayStyle.LAISSEZ_OS;

	// ----------------------------------------------------------------------------------------
	// Lighting service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default brightness for the LED lights in percentage.
	 */
	private static final int DEFAULT_LED_BRIGHTNESS = 50;

	/**
	 * Default brightness value for the actual lighting controller.
	 */
	private static final int DEFAULT_MAX_CONTROLLER_BRIGHTNESS = 128;

	/**
	 * Default first color for LED lights.
	 */
	private static final String DEFAULT_FIRST_COLOR = "0x0000FF";

	/**
	 * Default second color for LED lights.
	 */
	private static final String DEFAULT_SECOND_COLOR = "0x000000";

	/**
	 * Default second color for LED lights.
	 */
	private static final String DEFAULT_THIRD_COLOR = "0x000000";

	// ----------------------------------------------------------------------------------------
	// Location service defaults.
	// ----------------------------------------------------------------------------------------

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
	// Music service defaults.
	// ----------------------------------------------------------------------------------------

	/**
	 * Default value for MP3 playback.
	 */
	private static final int DEFAULT_MUSIC_VOLUME = 4;
	
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

	/**
	 * Starting brightness of the LED lights.
	 */
	private int brightness = DEFAULT_LED_BRIGHTNESS;

	/**
	 * Maximum brightness value for the controller.
	 */
	private int maxControllerBrightness = DEFAULT_MAX_CONTROLLER_BRIGHTNESS;

	/**
	 * Starting first color for LED lights.
	 */
	private String firstColor = DEFAULT_FIRST_COLOR;

	/**
	 * Starting second color for LED lights.
	 */
	private String secondColor = DEFAULT_SECOND_COLOR;

	/**
	 * Starting third color for LED lights.
	 */
	private String thirdColor = DEFAULT_THIRD_COLOR;

	// ----------------------------------------------------------------------------------------
	// Location service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Map of location names to their locations for the preset known locations.
	 */
	private Map<String, Location> presetLocations;

	/**
	 * Default preset location to use.
	 */
	private String defaultLocation;

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
	// Music service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Map of playlist name to directory where its tracks are located.
	 */
	private Map<String, String> playlists;

	/**
	 * Name of the playlist to start with selected.
	 */
	private String startingPlaylist;
	
	/**
	 * Command to use to play an MP3.
	 */
	private String playerCommand;

	/**
	 * Volume for MP3 playblack.
	 */
	private int musicVolume = DEFAULT_MUSIC_VOLUME;
	
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

	/**
	 * Starting brightness of the LED lights as a percentage.
	 * 
	 * @return Brightness for LED lights.
	 */
	public int getBrightness() {
		return brightness;
	}

	/**
	 * Maximum brightness to use for the actual LED controller.
	 * 
	 * @return Max brightness for controller (0 to 255).
	 */
	public int getMaxControllerBrightness() {
		return maxControllerBrightness;
	}

	/**
	 * First color for LED lights.
	 * 
	 * @return Hex string representing first color.
	 */
	public String getFirstColor() {
		return firstColor;
	}

	/**
	 * Background color for LED lights.
	 * 
	 * @return Hex string representing second color.
	 */
	public String getSecondColor() {
		return secondColor;
	}

	/**
	 * Third color for LED lights.
	 * 
	 * @return Hex string representing third color.
	 */
	public String getThirdColor() {
		return thirdColor;
	}

	// ----------------------------------------------------------------------------------------
	// Location service property getters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the preset locations.
	 * 
	 * @return Map of present name to location objects.
	 */
	public Map<String, Location> getPresetLocations() {
		return presetLocations;
	}

	/**
	 * Get the default location to start the chair at until the GPS has a fix.
	 * 
	 * @return Name of the location preset to use as default.
	 */
	public String getDefaultLocation() {
		return defaultLocation;
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

	// ----------------------------------------------------------------------------------------
	// Music service settings.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the map of playlist names to their directories that contains the MP3
	 * representing the tracks in the playlist.
	 * 
	 * @return Map of playlist name to directory names.
	 */
	public Map<String, String> getPlaylists() {
		return playlists;
	}

	/**
	 * Returns the name of the playlist to start with selected.
	 * 
	 * @return Name of playlist that should be selected when music service starts.
	 */
	public String getStartingPlaylist() {
		return startingPlaylist;
	}
	
	/**
	 * Returns the external command to use to play mp3's.
	 * 
	 * @return External mp3 player command.
	 */
	public String getPlayerCommand() {
		return playerCommand;
	}
	
	/**
	 * Returns the volume to play MP3's with.
	 * 
	 * @return Integer value for volume setting for MP3 playback.
	 */
	public int getMusicVolume() {
		return musicVolume;
	}
}