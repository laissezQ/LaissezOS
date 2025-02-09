package com.wisneskey.los.service;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.state.ChairState;
import com.wisneskey.los.state.ChairState.MasterState;
import com.wisneskey.los.state.State;

/**
 * Abstract base class for LBOS services that handles common functionality.
 * 
 * Copyright (C) 2025 Paul Wisneskey
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @param  <T> Type of state object used by the service.
 * 
 * @author     paul.wisneskey@gmail.com
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
	
	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns true if the chair is consider to be booting.  Usually used to suppress messages
	 * that we do not want to interrupt the theatrical boot messages.
	 * 
	 * @return True iff chair is in booting state.
	 */
	protected boolean isBooting() {
		ChairState chairState = Kernel.kernel().chairState();
		return chairState.masterState().getValue() == MasterState.BOOTING;
	}
}
