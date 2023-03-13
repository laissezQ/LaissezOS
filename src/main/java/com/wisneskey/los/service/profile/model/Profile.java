package com.wisneskey.los.service.profile.model;

import java.util.Set;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.audio.SoundEffectSet;
import com.wisneskey.los.service.display.DisplayStyle;

/**
 * Class defining the root profile object used to manage an individual
 * configuration for the operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Profile {

	/**
	 * Default sound effect set to use.
	 */
	public static final SoundEffectSet DEFAULT_SOUND_EFFECT_SET = SoundEffectSet.DEV;

	/**
	 * Default style for the displays.
	 */
	public static final DisplayStyle DEFAULT_DISPLAY_STYLE = DisplayStyle.NORD_DARK;

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
	 * Id of the sound effect set to start with.
	 */
	private SoundEffectSet soundEffectSet = DEFAULT_SOUND_EFFECT_SET;

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
	 * Set the display style for the UI.
	 * 
	 * @param displayStyle Display style to use for the UI.
	 */
	public void setDisplayStyle(DisplayStyle displayStyle) {
		this.displayStyle = displayStyle;
	}

	/**
	 * Returns the sound effect set to use.
	 * 
	 * @return Id of the sound effect set to use.
	 */
	public SoundEffectSet getSoundEffectSet() {
		return soundEffectSet;
	}

	/**
	 * Sets the sound effect set to use.
	 * 
	 * @param soundEffectSet
	 *          Id of the sound effect set to use.
	 */
	public void setSoundEffectSet(SoundEffectSet soundEffectSet) {
		this.soundEffectSet = soundEffectSet;
	}
}
