package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;

/**
 * Request for getting the state object from a WLED controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class GetStateRequest extends Request<State> {

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public GetStateRequest() {
		super(RequestType.GET, "/json/state", State.class);
	}
}
