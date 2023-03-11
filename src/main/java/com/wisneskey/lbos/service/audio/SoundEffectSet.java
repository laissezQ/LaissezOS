package com.wisneskey.lbos.service.audio;

/**
 * Enumerated type designating the various sets of sound effects that are
 * available to be played.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum SoundEffectSet {

	DEV("Initial sound effect set for LBOS development.");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Brief description of the set.
	 */
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffectSet(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}
}
