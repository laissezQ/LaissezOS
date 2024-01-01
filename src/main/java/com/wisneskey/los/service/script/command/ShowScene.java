package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;

import javafx.application.Platform;

/**
 * Script command to show a scene on one of the chair displays.
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
public class ShowScene extends AbstractScriptCommand {

	/**
	 * Id of the scene to show.
	 */
	private SceneId sceneId;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public SceneId getSceneId() {
		return sceneId;
	}

	public void setSceneId(SceneId sceneId) {
		this.sceneId = sceneId;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommands methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		Platform.runLater(() -> ((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(sceneId));
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ShowScene[" + getSceneId() + "]";
	}

}
