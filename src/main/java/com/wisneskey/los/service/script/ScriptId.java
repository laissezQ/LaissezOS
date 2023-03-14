package com.wisneskey.los.service.script;

/**
 * Enumerated type defining the scripts that are available to be executed.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ScriptId {

	BOOT_FAST("Fasted possible boot sequence.", "Boot-Fast"),
	BOOT_DEV("Test boot sequence for development.", "Boot-DEV");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Description of the script.
	 */
	private String description;

	/**
	 * Name of the script file to load from the resources.
	 */
	private String name;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ScriptId(String description, String name) {
		this.description = description;
		this.name = name;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
}
