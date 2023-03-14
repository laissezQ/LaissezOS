package com.wisneskey.los.service.audio;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum SoundEffectId {

	MISC_WINDOWS_XP_STARTUP("misc/windows_xp_startup.wav", "Windows XP startup sound."),

	MOVIE_HAL_OPERATIONAL("movie/hal_operational.wav", "I'm completely operational..."),
	MOVIE_MACHINE_GOES_PING("movie/machine_goes_ping.wav", "Machine that goes ping!"),
	MOVIE_WHAT_IS_THY_BIDDING("movie/what_is_thy_bidding_master.wav", "What is thy bidding, Master?");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String resourcePath;
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffectId(String resourcePath, String description) {
		this.resourcePath = resourcePath;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getResourcePath() {
		return resourcePath;
	}

	public String getDescription() {
		return description;
	}
}
