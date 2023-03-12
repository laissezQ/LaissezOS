package com.wisneskey.los.service.profile.model;

import java.util.Set;

import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffectSet;

/**
 * Class defining the root profile object used to manage an individual
 * configuration for the operating sytstem.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Profile {

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
	 * Id of the sound effect set to start with.
	 */
	private SoundEffectSet soundEffectSet = AudioService.DEFAULT_SOUND_EFFECT_SET;

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
