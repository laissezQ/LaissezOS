package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the file system information. Considered to be a read only
 * object from the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class FileSystemInfo {

	@JsonProperty("pmt")
	private Integer pmt;

	@JsonProperty("t")
	private Integer total;

	@JsonProperty("u")
	private Integer used;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public Integer getPmt() {
		return pmt;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getUsed() {
		return used;
	}
}