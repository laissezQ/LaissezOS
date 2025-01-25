package com.wisneskey.los.service.script.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * Interface for commands that can be run from a script.
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
@JsonSubTypes({

		@JsonSubTypes.Type(value = Message.class, name = "message"),
		@JsonSubTypes.Type(value = Pause.class, name = "pause"),
		@JsonSubTypes.Type(value = PlaySoundEffect.class, name = "playSoundEffect"),
		@JsonSubTypes.Type(value = PlayTrack.class, name = "playTrack"),
		@JsonSubTypes.Type(value = RelayOff.class, name = "relayOff"),
		@JsonSubTypes.Type(value = RelayOn.class, name = "relayOn"),
		@JsonSubTypes.Type(value = RestoreLightingState.class, name = "restoreLightingState"),
		@JsonSubTypes.Type(value = RunLightingEffect.class, name = "runLightingEffect"),
		@JsonSubTypes.Type(value = SetBarState.class, name = "setBarState"),
		@JsonSubTypes.Type(value = SetChairState.class, name = "setChairState"),
		@JsonSubTypes.Type(value = ShowScene.class, name = "showScene"),
		@JsonSubTypes.Type(value = Shutdown.class, name = "shutdown"),
		@JsonSubTypes.Type(value = StoreLightingState.class, name = "storeLightingState") })
public interface ScriptCommand {

	/**
	 * Number of seconds to pause after executing the command.
	 * 
	 * @return Number of seconds to pause after executing the command.
	 */
	double getPostCommandPause();

	/**
	 * Method called to perform the script command.
	 */
	void perform();
}