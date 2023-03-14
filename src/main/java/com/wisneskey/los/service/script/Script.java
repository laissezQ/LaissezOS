package com.wisneskey.los.service.script;

import java.util.List;

import com.wisneskey.los.service.script.command.ScriptCommand;

/**
 * Object representing a script to be run.
 */
public class Script {

	/**
	 * Name of script for display.
	 */
	private String name;

	/**
	 * List of commands to execute.
	 */
	private List<ScriptCommand> commands;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ScriptCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<ScriptCommand> commands) {
		this.commands = commands;
	}
}
