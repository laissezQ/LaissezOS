package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.service.lighting.driver.wled.client.model.Effects;

/**
 * Request for getting the list of preset effects from a WLED controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class GetEffectsRequest extends Request<Effects> {

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public GetEffectsRequest() {
		super(RequestType.GET, "/json/eff", Effects.class);
	}
}
