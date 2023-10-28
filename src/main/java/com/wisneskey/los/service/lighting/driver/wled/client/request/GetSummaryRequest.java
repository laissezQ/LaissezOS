package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.service.lighting.driver.wled.client.model.Summary;

/**
 * Request for getting the summary object from a WLED controller.  The summary object contains
 * all information that can be obtained from the controller.  It is consided a read only object.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class GetSummaryRequest extends Request<Summary> {

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public GetSummaryRequest() {
		super(RequestType.GET, "/json", Summary.class);
	}
}
