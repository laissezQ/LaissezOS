package com.wisneskey.los.service.script.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * Interface for commands that can be run from a script.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonSubTypes({

	@JsonSubTypes.Type(value = LockChair.class, name = "lockChair"),
		@JsonSubTypes.Type(value = Message.class, name = "message"),
		@JsonSubTypes.Type(value = Pause.class, name = "pause"),
		@JsonSubTypes.Type(value = PlaySoundEffect.class, name = "playSoundEffect"),
		@JsonSubTypes.Type(value = ShowScene.class, name = "showScene") })
public interface ScriptCommand {

	/**
	 * Number of seconds to pause after executing the command.
	 * 
	 * @return Number of seconds to pause after executing the command.
	 */
	double getPostCommandPause();

	/**
	 * Method called to perform the script command.
	 */
	void perform();
}
