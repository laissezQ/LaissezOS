package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.lighting.driver.wled.client.model.UpdateStateResult;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;
import com.wisneskey.los.util.JsonUtils;

/**
 * Request for sending updated state information to a WLED controller. The state
 * object should only have values for the properties to change in the state.
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