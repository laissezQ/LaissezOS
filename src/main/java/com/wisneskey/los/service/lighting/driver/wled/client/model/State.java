package com.wisneskey.los.service.lighting.driver.wled.client.model;

/**
 * Model object representing all of the information returned from a WLED controller.  The
 * sub-objects in this class may not be replaced - it is considered to be a read only 
 * response from the controller.  However, the state object may be taken and modified
 * for sending back to the controller though this is not recommended.  It is better to
 * create a new state object with just the updates that need to be applied.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class State {

	private Effects effects;
	private Info info;
	private Palettes palettes;
	private State state;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public Effects getEffects() {
		return effects;
	}

	public Info getInfo() {
		return info;
	}

	public Palettes getPalettes() {
		return palettes;
	}

	public State getState() {
		return state;
	}
}
