package com.wisneskey.los.service;

import com.wisneskey.los.state.State;

/**
 * Interface designation a service for LBOS.
 * 
 * @param <T>
 *          Type of state object used by the service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface Service<T extends State> {

	/**
	 * Return the id of the service.
	 * 
	 * @return Id of the service.
	 */
	ServiceId getServiceId();
}
