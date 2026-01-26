package com.wisneskey.los.service.lighting.driver.wled.config;

/**
 * Class containing the configuration necessary to have WLED perform a designated
 * lighting effect.
 *
 * Copyright (C) 2026 Paul Wisneskey
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
public class WledEffectConfig {

	/**
	 * Default effect to keeping light on.  Just used by all off to turn off the lights.
	 */
	public static final Boolean DEFAULT_ON = Boolean.TRUE;
	
	/**
	 * Flag indicating if lights should be on or off.
	 */
	private Boolean on = DEFAULT_ON;
	
	/**
	 * WLED effect id.
	 */
	private Integer effectId;
	
	/**
	 * Speed of the effect (0 - 255).
	 */
	private Integer speed;
	
	/**
	 * Intensity of the effect(0 - 255).
	 */
	private Integer intensity;
	
	/**
	 * Slider one for the effect (0 - 255).
	 */
	private Integer slider1;
	
	/**
	 * Slider two for the effect (0 - 255).
	 */
	private Integer slider2;
	
	/**
	 * Slider three for the effect (0 - 31).
	 */
	private Integer slider3;
	
	/**
	 * Option one for the effect.
	 */
	private Boolean option1;

	/**
	 * Option two for the effect.
	 */
	private Boolean option2;
	
	/**
	 * Option three for the effect.
	 */
	private Boolean option3;

	/**
	 * First color for the effect.
	 */
	private String color1;
	
	/**
	 * Second color for the effect.
	 */
	private String color2;
	
	/**
	 * Third color for the effect.
	 */
	private String color3;
	
	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Integer getEffectId() {
		return effectId;
	}

	public void setEffectId(Integer effectId) {
		this.effectId = effectId;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getIntensity() {
		return intensity;
	}

	public void setIntensity(Integer intensity) {
		this.intensity = intensity;
	}

	public Integer getSlider1() {
		return slider1;
	}

	public void setSlider1(Integer slider1) {
		this.slider1 = slider1;
	}

	public Integer getSlider2() {
		return slider2;
	}

	public void setSlider2(Integer slider2) {
		this.slider2 = slider2;
	}

	public Integer getSlider3() {
		return slider3;
	}

	public void setSlider3(Integer slider3) {
		this.slider3 = slider3;
	}

	public Boolean getOption1() {
		return option1;
	}

	public void setOption1(Boolean option1) {
		this.option1 = option1;
	}

	public Boolean getOption2() {
		return option2;
	}

	public void setOption2(Boolean option2) {
		this.option2 = option2;
	}

	public Boolean getOption3() {
		return option3;
	}

	public void setOption3(Boolean option3) {
		this.option3 = option3;
	}

	public static Boolean getDefaultOn() {
		return DEFAULT_ON;
	}

	public String getColor1() {
		return color1;
	}

	public void setColor1(String color1) {
		this.color1 = color1;
	}

	public String getColor2() {
		return color2;
	}

	public void setColor2(String color2) {
		this.color2 = color2;
	}

	public String getColor3() {
		return color3;
	}

	public void setColor3(String color3) {
		this.color3 = color3;
	}
}