package com.wisneskey.lbos.service;

import com.wisneskey.lbos.service.audio.AudioService;
import com.wisneskey.lbos.service.display.DisplayService;

/**
 * Enumerated type for all of the ids for the services supported in LBOS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum ServiceId {

	AUDIO(AudioService.class, "This service goes to 11."),
	DISPLAY(DisplayService.class, "Looking good there,");

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
	private Class<? extends Service> serviceClass;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ServiceId(Class<? extends Service> serviceClass, String description) {
		this.serviceClass = serviceClass;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public Class<? extends Service> getServiceClass() {
		return serviceClass;
	}

	public String getDescription() {
		return description;
	}
}