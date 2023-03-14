package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Script command to pause a script for a given number of seconds.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Pause extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(Pause.class);

	/**
	 * Number of milliseconds in a second.
	 */
	private static final double MILLISECONDS_PER_SECOND = 1000.0;
	
	/**
	 * Number of seconds to pause the script.
	 */
	private double seconds;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public double getSeconds() {
		return seconds;
	}

	public void setSeconds(double seconds) {
		this.seconds = seconds;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		try {
			Thread.sleep((long) (getSeconds() * MILLISECONDS_PER_SECOND));
		} catch( InterruptedException e )
		{
			LOGGER.warn("Interrupted while in pause.");
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Pause[" + seconds + " seconds]";
	}
}
