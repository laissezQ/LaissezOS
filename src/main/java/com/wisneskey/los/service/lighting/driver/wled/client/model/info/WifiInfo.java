package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the WIFI connection information. Considered to be a read
 * only object from the controller.
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