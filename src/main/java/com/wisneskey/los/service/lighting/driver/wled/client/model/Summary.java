package com.wisneskey.los.service.lighting.driver.wled.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisneskey.los.service.lighting.driver.wled.client.model.info.Info;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;

/**
 * Model object representing all of the information returned from a WLED
 * controller. The sub-objects in this class may not be replaced - it is
 * considered to be a read only response from the controller. However, the state
 * object may be taken and modified for sending back to the controller though
 * this is not recommended. It is better to create a new state object with just
 * the updates that need to be applied.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Summary {

	@JsonProperty("effects")
	private Effects effects;
	
	@JsonProperty("info")
	private Info info;
	
	@JsonProperty("palettes")
	private Palettes palettes;

	@JsonProperty("state")
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