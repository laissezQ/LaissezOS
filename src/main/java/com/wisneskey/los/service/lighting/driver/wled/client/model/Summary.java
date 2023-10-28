package com.wisneskey.los.service.lighting.driver.wled.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisneskey.los.service.lighting.driver.wled.client.model.info.Info;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;

/**
 * Model object representing all of the information returned from a WLED
 * controller. The sub-objects in this class may not be replaced - it is
 * considered to be a read only response from the controller. However, the state
 * object may be taken and modified for sending back to the controller though
 * this is not recommended. It is better to create a new state object with just
 * the updates that need to be applied.
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
public class Summary {

	@JsonProperty("effects")
	private Effects effects;

	@JsonProperty("info")
	private Info info;

	@JsonProperty("palettes")
	private Palettes palettes;

	@JsonProperty("state")
	private State state;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public Effects getEffects() {
		return effects;
	}

	public Info getInfo() {
		return info;
	}

	public Palettes getPalettes() {
		return palettes;
	}

	public State getState() {
		return state;
	}
}