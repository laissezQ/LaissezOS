package com.wisneskey.los.service;

import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.location.LocationService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.service.security.SecurityService;
import com.wisneskey.los.state.AudioState;
import com.wisneskey.los.state.DisplayState;
import com.wisneskey.los.state.LocationState;
import com.wisneskey.los.state.ProfileState;
import com.wisneskey.los.state.RelayState;
import com.wisneskey.los.state.ScriptState;
import com.wisneskey.los.state.SecurityState;
import com.wisneskey.los.state.State;

/**
 * Enumerated type for all of the ids for the services supported in LBOS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ServiceId {

	AUDIO(AudioService.class, AudioState.class, "This service goes to 11."),
	DISPLAY(DisplayService.class, DisplayState.class, "Looking good there!"),
	LOCATION(LocationService.class, LocationState.class, "Where the hell am I?"),
	PROFILE(ProfileService.class, ProfileState.class, "Keep it low."),
	RELAY(RelayService.class, RelayState.class, "Have you tried turning it off and on again?"),
	SCRIPT(ScriptService.class, ScriptState.class, "Don't tell me what to do!"),
	SECURITY(SecurityService.class, SecurityState.class, "1000 times no!");

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