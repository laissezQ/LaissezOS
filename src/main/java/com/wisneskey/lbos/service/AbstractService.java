package com.wisneskey.lbos.service;

/**
 * Abstract base class for LBOS services that handles common functionality.
 * 
 * @author paul.wisneskey@gmail.com
 */
public abstract class AbstractService implements Service {

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
