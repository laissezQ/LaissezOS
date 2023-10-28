package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the information about the configuration and state of the
 * LEDs connected to the controller. Considered to be a read only object from
 * the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LedInfo {

	@JsonProperty("cct")
	private Integer cct;

	@JsonProperty("count")
	private Integer count;

	@JsonProperty("fps")
	private Integer framesPerSecond;

	@JsonProperty("rgbw")
	private Boolean isRGBW;

	@JsonProperty("maxpwr")
	private Integer maxPower;

	@JsonProperty("maxseg")
	private Integer maxSegments;

	@JsonProperty("lc")
	private Integer lc;

	@JsonProperty("pwr")
	private Integer power;

	@JsonProperty("seglc")
	private Integer[] segmentLC;

	@JsonProperty("wv")
	private Integer wv;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public Integer getCct() {
		return cct;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getFramesPerSecond() {
		return framesPerSecond;
	}

	public Boolean getIsRGBW() {
		return isRGBW;
	}

	public Integer getMaxPower() {
		return maxPower;
	}

	public Integer getMaxSegments() {
		return maxSegments;
	}

	public Integer getLc() {
		return lc;
	}

	public Integer getPower() {
		return power;
	}

	public Integer[] getSegmentLC() {
		return segmentLC;
	}

	public Integer getWv() {
		return wv;
	}
}