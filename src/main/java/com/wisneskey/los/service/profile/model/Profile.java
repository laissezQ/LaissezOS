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
	 * Script to run at boot.
	 */
	private ScriptId bootScript = DEFAULT_BOOT_SCRIPT;
	
	/**
	 * 4 digit PIN code to unlock chair.
	 */
	private String pinCode;
		
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
	 * Returns the script to run for the boot sequence.
	 * 
	 * @return Id of the script to run for boot sequence.
	 */
	public ScriptId getBootScript() {
		return bootScript;
	}

	public String getPinCode() {
		return pinCode;
	}

	public ScriptId getUnlockedScript() {
		return unlockedScript;
	}

	public ScriptId getUnlockFailedScript() {
		return unlockFailedScript;
	}

}
