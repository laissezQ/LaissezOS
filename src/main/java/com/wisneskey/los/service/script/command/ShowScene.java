package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;

import javafx.application.Platform;

/**
 * Script command to show a scene on one of the chair displays.
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
}
