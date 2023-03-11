package com.wisneskey.lbos.service.audio;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum SoundEffect {

	BOOT_COMPLETE("hal_good_morning.wav", "Succesful completion of Laissez Boy OS boot sequence.");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String resourcePath;
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffect(String resourcePath, String description) {
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
