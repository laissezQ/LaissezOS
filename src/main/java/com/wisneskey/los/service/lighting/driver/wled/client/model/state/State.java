package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the state of a WLED controller. This object is
 * read/write and can be used to update the state of the controller. It is
 * designed so that only state properties to change need to be set in a state
 * object that is being sent back to the controller.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
public class State {

	@JsonProperty("bri")
	private Integer brightness;

	@JsonProperty("lor")
	private Integer lor;

	@JsonProperty("mainseg")
	private Integer mainSegment;

	@JsonProperty("nl")
	private NlState nl;

	@JsonProperty("on")
	private Boolean on;

	@JsonProperty("ps")
	private Integer ps;

	@JsonProperty("pl")
	private Integer pl;

	@JsonProperty("seg")
	private List<SegmentState> segments;

	@JsonProperty("transition")
	private Integer transition;

	@JsonProperty("udpn")
	private UdpNetworkState udpNetworkState;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}

	public Integer getLor() {
		return lor;
	}

	public void setLor(Integer lor) {
		this.lor = lor;
	}

	public Integer getMainSegment() {
		return mainSegment;
	}

	public void setMainSegment(Integer mainSegment) {
		this.mainSegment = mainSegment;
	}

	public NlState getNl() {
		return nl;
	}

	public void setNl(NlState nl) {
		this.nl = nl;
	}

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Integer getPs() {
		return ps;
	}

	public void setPs(Integer ps) {
		this.ps = ps;
	}

	public Integer getPl() {
		return pl;
	}

	public void setPl(Integer pl) {
		this.pl = pl;
	}

	public List<SegmentState> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentState> segments) {
		this.segments = segments;
	}

	public Integer getTransition() {
		return transition;
	}

	public void setTransition(Integer transition) {
		this.transition = transition;
	}

	public UdpNetworkState getUdpNetworkState() {
		return udpNetworkState;
	}

	public void setUdpNetworkState(UdpNetworkState udpNetworkState) {
		this.udpNetworkState = udpNetworkState;
	}
}