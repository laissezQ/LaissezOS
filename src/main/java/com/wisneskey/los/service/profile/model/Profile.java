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
	 * Optional script to run when chair is unlocked.
	 */
	private ScriptId unlockedScript;

	/**
	 * Optional script to run when wrong PIN code is entered.
	 */
	private ScriptId unlockFailedScript;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
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
	 * Returns the style to use for the UI displays.
	 * 
	 * @return Display style for the UI.
	 */
	public DisplayStyle getDisplayStyle() {
		return displayStyle;
	}

	/**
	 * 4 digit numeric code required to unlock chair.
	 * 
	 * @return Code required to unlock chair.
	 */
	public String getPinCode() {
		return pinCode;
	}

	public double getInitialLatitude() {
		return initialLatitude;
	}

	public double getInitialLongitude() {
		return initialLongitude;
	}

	public double getInitialAltitude() {
		return initialAltitude;
	}

	/**
	 * Returns the script to run for the boot sequence.
	 * 
	 * @return Id of the script to run for boot sequence.
	 */
	public ScriptId getBootScript() {
		return bootScript;
	}


	public ScriptId getUnlockedScript() {
		return unlockedScript;
	}

	public ScriptId getUnlockFailedScript() {
		return unlockFailedScript;
	}
}
