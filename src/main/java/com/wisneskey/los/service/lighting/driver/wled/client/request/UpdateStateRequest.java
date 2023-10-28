package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.lighting.driver.wled.client.model.UpdateStateResult;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;
import com.wisneskey.los.util.JsonUtils;

/**
 * Request for sending updated state information to a WLED controller. The state
 * object should only have values for the properties to change in the state.
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
 * @author paul.wisneskey@gmail.com
 */
public class UpdateStateRequest extends Request<UpdateStateResult> {

	/**
	 * State object containing only the updates to be applied to the state.
	 */
	private State stateUpdates;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public UpdateStateRequest(State stateUpdates) {
		super(RequestType.POST, "/json/state", UpdateStateResult.class);
		this.stateUpdates = stateUpdates;
	}

	// ----------------------------------------------------------------------------------------
	// Request methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String getPostBody() {

		if (stateUpdates == null) {
			throw new LaissezException("No state updates supplied to update state request.");
		}

		return JsonUtils.toJSONString(stateUpdates);
	}
}