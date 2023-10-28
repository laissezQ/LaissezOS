package com.wisneskey.los.service.script.command;

/**
 * Script command to pause a script for a given number of seconds.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Pause extends AbstractScriptCommand {

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

		sleepForSeconds(getSeconds());
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Pause[" + seconds + " seconds]";
	}
}
