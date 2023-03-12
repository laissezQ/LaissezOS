package com.wisneskey.los.service;

import com.wisneskey.los.state.State;

/**
 * Abstract base class for LBOS services that handles common functionality.
 * 
 * @param <T>
 *          Type of state object used by the service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public abstract class AbstractService<T extends State> implements Service<T> {

	/**
	 * Id for the service.
	 */
	private ServiceId serviceId;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	protected AbstractService(ServiceId serviceId) {
		this.serviceId = serviceId;
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public ServiceId getServiceId() {
		return serviceId;
	}
}
