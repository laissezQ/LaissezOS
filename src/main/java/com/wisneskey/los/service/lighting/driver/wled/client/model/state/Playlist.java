package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * State object for a playlist of effects.
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
public class Playlist {

	/**
	 * List of ids of the presets to play.
	 */
	@JsonProperty("ps")
	private List<Integer> presetIds;
	
	/**
	 * List of durations to play each preset (in tenths of seconds).
	 */
	@JsonProperty("dur")
	private List<Integer> durations;
	
	/**
	 * List of times each preset should transition to the next one in tenths of seconds.
	 */
	@JsonProperty("transition")
	private Integer transition;
	
	/**
	 * How many times the playlist should be repeated.
	 */
	@JsonProperty("repeat")
	private Integer repeat;
	
	/**
	 * Id of the preset to play at the end of playlist.
	 */
	@JsonProperty("end")
	private Integer end;

	// ----------------------------------------------------------------------------------------
	// Property setters/getters.
	// ----------------------------------------------------------------------------------------

	public List<Integer> getPresetIds() {
		return presetIds;
	}

	public void setPresetIds(List<Integer> presetIds) {
		this.presetIds = presetIds;
	}

	public List<Integer> getDurations() {
		return durations;
	}

	public void setDurations(List<Integer> durations) {
		this.durations = durations;
	}

	public Integer getTransition() {
		return transition;
	}

	public void setTransition(Integer transition) {
		this.transition = transition;
	}

	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}
	

}
