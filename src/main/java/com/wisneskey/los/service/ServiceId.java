package com.wisneskey.los.service;

import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.state.AudioState;
import com.wisneskey.los.state.DisplayState;
import com.wisneskey.los.state.ProfileState;
import com.wisneskey.los.state.ScriptState;
import com.wisneskey.los.state.State;

/**
 * Enumerated type for all of the ids for the services supported in LBOS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ServiceId {

	AUDIO(AudioService.class, AudioState.class, "This service goes to 11."),
	DISPLAY(DisplayService.class, DisplayState.class, "Looking good there,"),
	PROFILE(ProfileService.class, ProfileState.class, "Keep it low."),
	SCRIPT(ScriptService.class, ScriptState.class, "Don't tell me what to do!");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Brief description of the service.
	 */
	private String description;

	/**
	 * Class of the service object.
	 */
	private Class<? extends Service<?>> serviceClass;

	/**
	 * Class of the service state object.
	 */
	private Class<? extends State> serviceStateClass;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ServiceId(Class<? extends Service<?>> serviceClass, Class<? extends State> serviceStateClass, String description) {
		this.serviceClass = serviceClass;
		this.serviceStateClass = serviceStateClass;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the class that implements the service.
	 * 
	 * @param <T> Type of service.
	 * @return Class for the service.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service<?>> Class<T> getServiceClass() {
		return (Class<T>) serviceClass;
	}

	/**
	 * Returns the class the provides the service's state.
	 * @return Class providing services state.
	 */
	public Class<? extends State> getServiceStateClass() {
		return serviceStateClass;
	}
	
	public String getDescription() {
		return description;
	}
}