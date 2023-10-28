package com.wisneskey.los.service.lighting.driver.wled.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the result of request updates to the state of a
 * WLED controller. This object is consider readonly.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class UpdateStateResult {

	@JsonProperty("success")
	private Boolean success;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Boolean getSuccess() {
		return success;
	}
}
