package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the NL state. I have not figured out what this is yet. This
 * object is read/write and can be used to update the state of the controller.
 * It is designed so that only state properties to change need to be set in a
 * state object that is being sent back to the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NlState {

	@JsonProperty("dur")
	private Integer duration;

	@JsonProperty("mode")
	private Integer mode;

	@JsonProperty("on")
	private Boolean on;

	@JsonProperty("rem")
	private Integer rem;

	@JsonProperty("tbri")
	private Integer tbri;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

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

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Integer getRem() {
		return rem;
	}

	public void setRem(Integer rem) {
		this.rem = rem;
	}

	public Integer getTbri() {
		return tbri;
	}

	public void setTbri(Integer tbri) {
		this.tbri = tbri;
	}
}