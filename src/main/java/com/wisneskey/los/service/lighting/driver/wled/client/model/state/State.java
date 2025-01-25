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
public class State {

	/**
	 * On or off status of the lights.
	 */
	@JsonProperty("on")
	private Boolean on;

	/**
	 * Brightness (1 to 255, using zero not recommended; turn off instead.
	 */
	@JsonProperty("bri")
	private Integer brightness;

	/**
	 * Standard transition time between color/brightness levels; 1 unit = 100ms.
	 */
	@JsonProperty("transition")
	private Integer transition;

	/**
	 * Transition time for the current call; 1 unit = 100ms.
	 */
	@JsonProperty("tt")
	private Integer transitionTime;

	/**
	 * Currently set preset.
	 */
	@JsonProperty("ps")
	private Integer preset;

	/**
	 * Save current light configuration to specified preset slot. Valid range is 1
	 * to 250.
	 */
	@JsonProperty("psave")
	private Integer presetSave;

	/**
	 * Id of the currently set play list (read only).
	 */
	@JsonProperty("pl")
	private Integer playlistId;
	
	/**
	 * Preset slot to delete.  Write only.
	 */
	@JsonProperty("pdel")
	private Integer presetDelete;
	
	/**
	 * Nightlight state.
	 */
	@JsonProperty("nl")
	private Nlghtlight nightlight;

	/**
	 * UDP sync broadcast state.
	 */
	@JsonProperty("udpn")
	private UdpNetworkSync udpNetworkState;

	/**
	 * Flag indicating if full response state object should be returned.
	 */
	@JsonProperty("v")
	private boolean verbose;
	
	/**
	 * Flag indicating device should reboot immediately; write only.
	 */
	@JsonProperty("rb")
	private boolean reboot;

	/**
	 * Flag to put device in live mode.
	 */
	@JsonProperty("live")
	private boolean live;
	
	/**
	 * Live date override: 0=off, 1=until live ends, 2=until reboot.
	 */
	@JsonProperty("lor")
	private Integer liveOverride;

	/**
	 * Set the device time (unix timestamp); write only.
	 */
	@JsonProperty("time")
	private Integer time;
	
	/**
	 * Main light segment (0 to max segments - 1).
	 */
	@JsonProperty("mainseg")
	private Integer mainSegment;

	/**
	 * Array of segment state information.
	 */
	@JsonProperty("seg")
	private List<Segment> segments;

	@JsonProperty("playlist")
	private Playlist playlist;
	
	/**
	 * Set timebase for effects; write only.
	 */
	@JsonProperty("tb")
	private Integer timebase;
	
	/**
	 * Load specific LED map (0 to 9); write only.
	 */
	@JsonProperty("ledmap")
	private Integer ledMap;
	
	/**
	 * Flag to remove last custom palette.
	 */
	@JsonProperty("rmcpal")
	private boolean removeCustomPalette;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

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

	public Integer getTransition() {
		return transition;
	}

	public void setTransition(Integer transition) {
		this.transition = transition;
	}

	public Integer getTransitionTime() {
		return transitionTime;
	}

	public void setTransitionTime(Integer transitionTime) {
		this.transitionTime = transitionTime;
	}

	public Integer getPreset() {
		return preset;
	}

	public void setPreset(Integer preset) {
		this.preset = preset;
	}

	public Integer getPresetSave() {
		return presetSave;
	}

	public void setPresetSave(Integer presetSave) {
		this.presetSave = presetSave;
	}

	public Integer getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(Integer playlistId) {
		this.playlistId = playlistId;
	}

	public Integer getPresetDelete() {
		return presetDelete;
	}

	public void setPresetDelete(Integer presetDelete) {
		this.presetDelete = presetDelete;
	}

	public Nlghtlight getNightlight() {
		return nightlight;
	}

	public void setNightlight(Nlghtlight nightlight) {
		this.nightlight = nightlight;
	}

	public UdpNetworkSync getUdpNetworkState() {
		return udpNetworkState;
	}

	public void setUdpNetworkState(UdpNetworkSync udpNetworkState) {
		this.udpNetworkState = udpNetworkState;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isReboot() {
		return reboot;
	}

	public void setReboot(boolean reboot) {
		this.reboot = reboot;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Integer getLiveOverride() {
		return liveOverride;
	}

	public void setLiveOverride(Integer liveOverride) {
		this.liveOverride = liveOverride;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getMainSegment() {
		return mainSegment;
	}

	public void setMainSegment(Integer mainSegment) {
		this.mainSegment = mainSegment;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Integer getTimebase() {
		return timebase;
	}

	public void setTimebase(Integer timebase) {
		this.timebase = timebase;
	}

	public Integer getLedMap() {
		return ledMap;
	}

	public void setLedMap(Integer ledMap) {
		this.ledMap = ledMap;
	}

	public boolean isRemoveCustomPalette() {
		return removeCustomPalette;
	}

	public void setRemoveCustomPalette(boolean removeCustomPalette) {
		this.removeCustomPalette = removeCustomPalette;
	}	
}