package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the state of the UDP network. This object is
 * read/write and can be used to update the state of the controller. It is
 * designed so that only state properties to change need to be set in a state
 * object that is being sent back to the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UdpNetworkState {

	@JsonProperty("rgrp")
	private Integer receiveGroup;

	@JsonProperty("recv")
	private Boolean receiving;

	@JsonProperty("sgrp")
	private Integer sendGroup;

	@JsonProperty("send")
	private Boolean sending;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Integer getReceiveGroup() {
		return receiveGroup;
	}

	public void setReceiveGroup(Integer receiveGroup) {
		this.receiveGroup = receiveGroup;
	}

	public Boolean getReceiving() {
		return receiving;
	}

	public void setReceiving(Boolean receiving) {
		this.receiving = receiving;
	}

	public Integer getSendGroup() {
		return sendGroup;
	}

	public void setSendGroup(Integer sendGroup) {
		this.sendGroup = sendGroup;
	}

	public Boolean getSending() {
		return sending;
	}

	public void setSending(Boolean sending) {
		this.sending = sending;
	}

}