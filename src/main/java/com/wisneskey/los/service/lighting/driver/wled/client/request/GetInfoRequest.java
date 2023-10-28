package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.service.lighting.driver.wled.client.model.Info;

/**
 * Request for getting the information object from a WLED controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class GetInfoRequest extends Request<Info> {

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public GetInfoRequest() {
		super(RequestType.GET, "/json/info", Info.class);
	}
}
