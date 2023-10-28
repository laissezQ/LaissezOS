package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the WIFI connection information. Considered to be a read
 * only object from the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class WifiInfo {

	@JsonProperty("channel")
	private Integer channel;

	@JsonProperty("bssid")
	private String bssid;

	@JsonProperty("rssi")
	private Integer rssi;

	@JsonProperty("signal")
	private Integer signal;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public Integer getChannel() {
		return channel;
	}

	public String getBssid() {
		return bssid;
	}

	public Integer getRssi() {
		return rssi;
	}

	public Integer getSignal() {
		return signal;
	}
}