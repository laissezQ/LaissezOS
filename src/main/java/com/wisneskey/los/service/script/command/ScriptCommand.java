package com.wisneskey.los.service.script.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * Interface for commands that can be run from a script.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonSubTypes({

		// Acquisition Service commands:
		@JsonSubTypes.Type(value = Pause.class, name = "pause") })
public interface ScriptCommand {

}
