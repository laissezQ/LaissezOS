package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for script commands.
 * 
 * @author paul.wisneskey@gmail.com
 */
public abstract class AbstractScriptCommand implements ScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScriptCommand.class);

	/**
	 * Number of milliseconds in a second.
	 */
	protected static final double MILLISECONDS_PER_SECOND = 1000.0;

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

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Lets script commands sleep for any fractional number of seconds.
	 * @param seconds Number of seconds to sleep.
	 */
	protected void sleepForSeconds(double seconds) {
		try {
			Thread.sleep((long) (seconds * MILLISECONDS_PER_SECOND));
		} catch (InterruptedException e) {
			LOGGER.warn("Interrupted while sleeping.");
			Thread.currentThread().interrupt();
		}
	}
}
