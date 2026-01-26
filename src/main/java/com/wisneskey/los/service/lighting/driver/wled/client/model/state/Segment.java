package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the state of a single lighting segment. This object
 * is read/write and can be used to update the state of the controller. It is
 * designed so that only state properties to change need to be set in a state
 * object that is being sent back to the controller.
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Segment {

	/**
	 * Id of the segment. If not specified, id is inferred from position in state
	 * segment array.
	 */
	@JsonProperty("id")
	private Integer id;

	/**
	 * Start of the segment (0 to LED count - 1).
	 */
	@JsonProperty("start")
	private Integer start;

	/**
	 * End of the segment (0 to LED count - 1).
	 */
	@JsonProperty("stop")
	private Integer stop;

	/**
	 * Starting row of matrix (0 to matrix width).
	 */
	private Integer startY;

	/**
	 * Stop row of matrix (0 to matrix heigh).
	 */
	private Integer stopY;

	/**
	 * Length of segment (stop takes precedence).
	 */
	@JsonProperty("len")
	private Integer length;

	/**
	 * Grouping of segment (how many consecutive LEDs to treat as same color): 0
	 * to 255.
	 */
	@JsonProperty("grp")
	private Integer grouping;

	/**
	 * Spacing of segment (how many LEDs are turned off and skipped between each
	 * group: 0 to 255.
	 */
	@JsonProperty("spc")
	private Integer spacing;

	/**
	 * Offset (how many LEDs to rotate the virtual start of the segments): -len+1
	 * to len.
	 */
	@JsonProperty("of")
	private Integer offset;

	/**
	 * Array of colors (primary, secondary (background), and tertiary.
	 */
	@JsonProperty("col")
	private List<List<Integer>> colors;

	/**
	 * Id of the effect for the segment (0 to info.fxcount - 1).
	 */
	@JsonProperty("fx")
	private Integer effectId;

	/**
	 * Relative effect speed (0 to 255).
	 */
	@JsonProperty("sx")
	private Integer effectSpeed;

	/**
	 * Relative effect intensity (0 to 255).
	 */
	@JsonProperty("ix")
	private Integer effectIntensity;

	/**
	 * Custom slider 1 for effect (0 to 255).
	 */
	@JsonProperty("c1")
	private Integer effectSlider1;

	/**
	 * Custom slider 2 for effect (0 to 255).
	 */
	@JsonProperty("c2")
	private Integer effectSlider2;

	/**
	 * Custom slider 3 for effect (0 to 255).
	 */
	@JsonProperty("c3")
	private Integer effectSlider3;

	/**
	 * Effect option 1.
	 */
	@JsonProperty("o1")
	private Boolean effectOption1;

	/**
	 * Effect option 2.
	 */
	@JsonProperty("o2")
	private Boolean effectOption2;

	/**
	 * Effect option 3.
	 */
	@JsonProperty("o3")
	private Boolean effectOption3;

	/**
	 * Id of the color palette.
	 */
	@JsonProperty("pal")
	private Integer paletteId;

	/**
	 * Flag indicating if segment is selected for update (HTTP and UDP).
	 */
	@JsonProperty("sel")
	private Boolean selected;

	/**
	 * Flag to flip the segment effect.
	 */
	@JsonProperty("rev")
	private Boolean reverse;

	/**
	 * Flag to flip a 2D segment in vertical dimension.
	 */
	@JsonProperty("rY")
	private Boolean reverseY;

	/**
	 * Turn on or off the segment.
	 */
	@JsonProperty("on")
	private Boolean on;

	/**
	 * Sets the segment brightness.
	 */
	@JsonProperty("bri")
	private Integer brightness;

	/**
	 * Flag to mirror the segment.
	 */
	@JsonProperty("mi")
	private Boolean mirror;

	/**
	 * Mirrors a 2D segment.
	 */
	@JsonProperty("mY")
	private Boolean mirrorY;

	/**
	 * Transpose a 2D segment.
	 */
	@JsonProperty("tp")
	private Boolean transpose;

	/**
	 * White spectrum color temperature (0 to 255 or 1900 to 10091).
	 */
	@JsonProperty("cct")
	private Integer colorTemperature;

	/**
	 * Freezes the current effect.
	 */
	@JsonProperty("frz")
	private Boolean freeze;

	/**
	 * Setting of segment field expand: 0=pixels, 1=bar, 2=arc, 3=corner.
	 */
	@JsonProperty("m12")
	private Integer exand1dFx;

	/**
	 * Setting of sound simulation type for audito effects (0 - 4).
	 */
	@JsonProperty("si")
	private Integer soundSimulation;

	/**
	 * Flag to load the defaults for an effect.
	 */
	@JsonProperty("fxdef")
	private boolean loadEffectDefaults;

	/**
	 * Assign set id to segment (UI use).
	 */
	@JsonProperty("set")
	private Integer setId;

	/**
	 * Flag to repeat current segment settings for all remaining LEDs.
	 */
	@JsonProperty("rpt")
	private boolean repeat;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getStop() {
		return stop;
	}

	public void setStop(Integer stop) {
		this.stop = stop;
	}

	public Integer getStartY() {
		return startY;
	}

	public void setStartY(Integer startY) {
		this.startY = startY;
	}

	public Integer getStopY() {
		return stopY;
	}

	public void setStopY(Integer stopY) {
		this.stopY = stopY;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getGrouping() {
		return grouping;
	}

	public void setGrouping(Integer grouping) {
		this.grouping = grouping;
	}

	public Integer getSpacing() {
		return spacing;
	}

	public void setSpacing(Integer spacing) {
		this.spacing = spacing;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<List<Integer>> getColors() {
		return colors;
	}

	public void setColors(List<List<Integer>> colors) {
		this.colors = colors;
	}

	public Integer getEffectId() {
		return effectId;
	}

	public void setEffectId(Integer effectId) {
		this.effectId = effectId;
	}

	public Integer getEffectSpeed() {
		return effectSpeed;
	}

	public void setEffectSpeed(Integer effectSpeed) {
		this.effectSpeed = effectSpeed;
	}

	public Integer getEffectIntensity() {
		return effectIntensity;
	}

	public void setEffectIntensity(Integer effectIntensity) {
		this.effectIntensity = effectIntensity;
	}

	public Integer getEffectSlider1() {
		return effectSlider1;
	}

	public void setEffectSlider1(Integer effectSlider1) {
		this.effectSlider1 = effectSlider1;
	}

	public Integer getEffectSlider2() {
		return effectSlider2;
	}

	public void setEffectSlider2(Integer effectSlider2) {
		this.effectSlider2 = effectSlider2;
	}

	public Integer getEffectSlider3() {
		return effectSlider3;
	}

	public void setEffectSlider3(Integer effectSlider3) {
		this.effectSlider3 = effectSlider3;
	}

	public Boolean getEffectOption1() {
		return effectOption1;
	}

	public void setEffectOption1(Boolean effectOption1) {
		this.effectOption1 = effectOption1;
	}

	public Boolean getEffectOption2() {
		return effectOption2;
	}

	public void setEffectOption2(Boolean effectOption2) {
		this.effectOption2 = effectOption2;
	}

	public Boolean getEffectOption3() {
		return effectOption3;
	}

	public void setEffectOption3(Boolean effectOption3) {
		this.effectOption3 = effectOption3;
	}

	public Integer getPaletteId() {
		return paletteId;
	}

	public void setPaletteId(Integer paletteId) {
		this.paletteId = paletteId;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Boolean getReverse() {
		return reverse;
	}

	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

	public Boolean getReverseY() {
		return reverseY;
	}

	public void setReverseY(Boolean reverseY) {
		this.reverseY = reverseY;
	}

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}

	public Boolean getMirror() {
		return mirror;
	}

	public void setMirror(Boolean mirror) {
		this.mirror = mirror;
	}

	public Boolean getMirrorY() {
		return mirrorY;
	}

	public void setMirrorY(Boolean mirrorY) {
		this.mirrorY = mirrorY;
	}

	public Boolean getTranspose() {
		return transpose;
	}

	public void setTranspose(Boolean transpose) {
		this.transpose = transpose;
	}

	public Integer getColorTemperature() {
		return colorTemperature;
	}

	public void setColorTemperature(Integer colorTemperature) {
		this.colorTemperature = colorTemperature;
	}

	public Boolean getFreeze() {
		return freeze;
	}

	public void setFreeze(Boolean freeze) {
		this.freeze = freeze;
	}

	public Integer getExand1dFx() {
		return exand1dFx;
	}

	public void setExand1dFx(Integer exand1dFx) {
		this.exand1dFx = exand1dFx;
	}

	public Integer getSoundSimulation() {
		return soundSimulation;
	}

	public void setSoundSimulation(Integer soundSimulation) {
		this.soundSimulation = soundSimulation;
	}

	public boolean isLoadEffectDefaults() {
		return loadEffectDefaults;
	}

	public void setLoadEffectDefaults(boolean loadEffectDefaults) {
		this.loadEffectDefaults = loadEffectDefaults;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
}