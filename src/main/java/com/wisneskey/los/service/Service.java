package com.wisneskey.los.service;

import com.wisneskey.los.service.profile.model.Profile;

/**
 * Interface designation a service for LBOS.
 * 
 * @param <T> Type of state object used by the service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface Service<T> {

	/**
	 * Return the id of the service.
	 * 
	 * @return Id of the service.
	 */
	ServiceId getServiceId();
	
	T getInitialState(Profile profile);
}
