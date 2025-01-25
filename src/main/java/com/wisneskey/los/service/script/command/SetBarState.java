package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.state.ChairState.BarState;

/**
 * Command to set the current state of the pop up bar for the chair.
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
public class SetBarState extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetBarState.class);

	/**
	 * State to set for the bar.
	 */
	private BarState state;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public BarState getState() {
		return state;
	}

	public void setState(BarState state) {
		this.state = state;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {
		if( state != null ) {
			LOGGER.info("Setting bar state: {}", state);
			Kernel.kernel().chairState().barState().set(state);
		}
	}
}
