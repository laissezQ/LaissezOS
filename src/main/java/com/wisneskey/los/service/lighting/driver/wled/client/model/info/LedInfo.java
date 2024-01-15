package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object for the information about the configuration and state of the
 * LEDs connected to the controller. Considered to be a read only object from
 * the controller.
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
public class LedInfo {

	/**
	 * Flag indicating if light supports color temperature control.
	 */
	@JsonProperty("cct")
	private boolean colorTempControl;

	/**
	 * Total number of LEDs.
	 */
	@JsonProperty("count")
	private Integer count;

	/**
	 * Current frames per second.
	 */
	@JsonProperty("fps")
	private Integer framesPerSecond;

	/**
	 * Flag indicating if LEDs are 4 channel (RGP + white).
	 */
	@JsonProperty("rgbw")
	private Boolean isRGBW;

	/**
	 * Flag indicating if white channel slider should be displayed.
	 */
	@JsonProperty("wv")
	private boolean whiteChannelSlider;

	/**
	 * Current LED power usage in milliamps.
	 */
	@JsonProperty("pwr")
	private Integer power;

	/**
	 * Maximum power budget in milliamps.
	 */
	@JsonProperty("maxpwr")
	private Integer maxPower;

	/**
	 * Maximum number of supported segments.
	 */
	@JsonProperty("maxseg")
	private Integer maxSegments;

	/**
	 * Logical AND of all segments light capabilities (e.g. common capabilities
	 * across all segments).
	 */
	@JsonProperty("lc")
	private Integer lightCapabilities;

	/**
	 * Per segment light capabilities.
	 */
	@JsonProperty("seglc")
	private Integer[] segmentLgithCapabilities;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public boolean getColorTempControl() {
		return colorTempControl;
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

	public boolean getWhiteChannelSlider() {
		return whiteChannelSlider;
	}

	public Integer getPower() {
		return power;
	}

	public Integer getMaxPower() {
		return maxPower;
	}

	public Integer getMaxSegments() {
		return maxSegments;
	}

	public Integer getLightCapabilities() {
		return lightCapabilities;
	}

	public Integer[] getSegmentLgithCapabilities() {
		return segmentLgithCapabilities;
	}
}