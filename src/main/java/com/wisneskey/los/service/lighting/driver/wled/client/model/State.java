package com.wisneskey.los.service.lighting.driver.wled.client.model;

/**
 * Model object representing the state of a WLED controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class State {

	private Effects effects;
	private Info info;
	private Palettes palettes;
	private State state;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Effects getEffects() {
		return effects;
	}

	public void setEffects(Effects effects) {
		this.effects = effects;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Palettes getPalettes() {
		return palettes;
	}

	public void setPalettes(Palettes palettes) {
		this.palettes = palettes;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
