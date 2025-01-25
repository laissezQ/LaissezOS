package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the NL state. I have not figured out what this is yet. This
 * object is read/write and can be used to update the state of the controller.
 * It is designed so that only state properties to change need to be set in a
 * state object that is being sent back to the controller.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
public class Nlghtlight {

	/**
	 * Flag indicating if nightlight currently active.
	 */
	@JsonProperty("on")
	private Boolean on;

	/**
	 * Duration of nightlight in minutes.
	 */
	@JsonProperty("dur")
	private Integer duration;

	/**
	 * Nightlight mode: 0=instance, 1=fade, 2=color fade, 3=sunrise
	 */
	@JsonProperty("mode")
	private Integer mode;

	/**
	 * Target brightness for nightlight feature.
	 */
	@JsonProperty("tbri")
	private Integer targetBrightness;

	/**
	 * Remaining nightlight duration in seconds; read only.
	 */
	@JsonProperty("rem")
	private Integer remaining;


	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getTargetBrightness() {
		return targetBrightness;
	}

	public void setTargetBrightness(Integer targetBrightness) {
		this.targetBrightness = targetBrightness;
	}

	public Integer getRemaining() {
		return remaining;
	}

	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}
}