package com.wisneskey.los.service.script;

/**
 * Enumerated type defining the scripts that are available to be executed.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ScriptId {

	BOOT_SEQUENCE_2001("Test boot sequence using 2001 references.","Boot2001");

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
