package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the state of the UDP network. This object is
 * read/write and can be used to update the state of the controller. It is
 * designed so that only state properties to change need to be set in a state
 * object that is being sent back to the controller.
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