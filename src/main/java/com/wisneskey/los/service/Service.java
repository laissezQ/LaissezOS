package com.wisneskey.los.service;

import com.wisneskey.los.state.State;

/**
 * Interface designation a service for LBOS.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public interface Service<T extends State> {

	/**
	 * Return the id of the service.
	 * 
	 * @return Id of the service.
	 */
	ServiceId getServiceId();

	/**
	 * Method invoked by the kernel to terminate the service.
	 */
	void terminate();
}
