package com.wisneskey.los.service.audio;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum SoundEffectId {

	MOVIE_HAL_OPERATIONAL("hal_good_morning.wav", "Hal's greeting from 2001 Space Oddysey."),
	MOVIE_MACHINE_GOES_PING("monty_python_machine_goes_ping.wav", "Machine goes ping from Monty Python's Meaning of Life.");

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
