package com.wisneskey.los.service.script.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * Interface for commands that can be run from a script.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonSubTypes({

		@JsonSubTypes.Type(value = Pause.class, name = "pause"),
		@JsonSubTypes.Type(value = PlaySoundEffect.class, name = "playSoundEffect")
		})
public interface ScriptCommand {

	/**
	 * Method called to perform the script command.
	 */
	void perform();
}
