package com.wisneskey.los.service;

import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.state.ProfileState;

/**
 * Enumerated type for all of the ids for the services supported in LBOS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ServiceId {

	AUDIO(AudioService.class, Object.class, "This service goes to 11."),
	DISPLAY(DisplayService.class, Object.class, "Looking good there,"),
	PROFILE(ProfileService.class, ProfileState.class, "Keep it low.");

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
	private Class<?> serviceStateClass;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ServiceId(Class<? extends Service<?>> serviceClass, Class<?> serviceStateClass, String description) {
		this.serviceClass = serviceClass;
		this.serviceStateClass = serviceStateClass;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public <T extends Service<?>> Class<T> getServiceClass() {
		return (Class<T>) serviceClass;
	}

	public Class<?> getServiceStateClass() {
		return serviceStateClass;
	}
	
	public String getDescription() {
		return description;
	}
}