package com.wisneskey.los.service.profile.model;

import java.util.Set;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.display.DisplayStyle;
import com.wisneskey.los.service.script.ScriptId;

/**
 * Class defining the root profile object used to manage an individual
 * configuration for the operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Profile {

	/**
	 * Default style for the displays.
	 */
	public static final DisplayStyle DEFAULT_DISPLAY_STYLE = DisplayStyle.LAISSEZ_OS;

	/**
	 * Default to a fast boot.
	 */
	public static final ScriptId DEFAULT_BOOT_SCRIPT = ScriptId.BOOT_FAST;

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
	 * Style for the UI on the displays.
	 */
	private DisplayStyle displayStyle = DEFAULT_DISPLAY_STYLE;

	/**
	 * 4 digit PIN code to unlock chair.
	 */
	private String pinCode;

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

	/**
	 * Script to run at boot.
	 */
	private ScriptId bootScript = DEFAULT_BOOT_SCRIPT;

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

	/**
	 * Returns the script to run for the boot sequence.
	 * 
	 * @return Id of the script to run for boot sequence.
	 */
	public ScriptId getBootScript() {
		return bootScript;
	}
}
