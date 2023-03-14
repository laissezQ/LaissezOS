package com.wisneskey.los.service.script.command;

/**
 * Abstract base class for script commands.
 * 
 * @author paul.wisneskey@gmail.com
 */
public abstract class AbstractScriptCommand implements ScriptCommand {

	/**
	 * Number of seconds to pause after executing a command.
	 */
	private double postCommandPause = 0.0;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	@Override
	public double getPostCommandPause() {
		return postCommandPause;
	}

	public void setPostCommandPause(double postCommandPause) {
		this.postCommandPause = postCommandPause;
	}
}
