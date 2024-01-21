package com.wisneskey.los.service.lighting.driver.wled.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;

/**
 * Model object representing the result of request updates to the state of a
 * WLED controller. This object is considered read only.
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
public class UpdateStateResult extends State {

	@JsonProperty("success")
	private Boolean success;
	
	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Boolean getSuccess() {
		return success;
	}	
}