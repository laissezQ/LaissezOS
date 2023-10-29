package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.state.MapState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel boot screen.
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
public class SystemScreen extends AbstractController {

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Button to exit LaissezOS.
	 */
	@FXML
	private Button exitButton;

	/**
	 * Button to return to normal chair operation.
	 */
	@FXML
	private Button resumeButton;

	/**
	 * Check box that controls if online map download is allowed.
	 */
	@FXML
	private CheckBox onlineMapCheckBox;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		// Bind the online map download check box to the map state property.
		MapState mapState = chairState().getServiceState(ServiceId.MAP);
		onlineMapCheckBox.selectedProperty().bindBidirectional(mapState.getOnline());
	}

	/**
	 * Method invoked by the exit LaissezOS button.
	 */
	public void exitPressed() {

		boolean confirmed = ((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)) //
				.showConfirmation(DisplayId.CP, //
						"Confirm Exit", //
						"LaissezOS exit has been requested!", "Do you want to exit LaissezOS?");

		if (confirmed) {
			((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.SHUTDOWN);
		}
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {

		Kernel.kernel().message("Chair operation resumed.\n");
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.CP_MAIN_SCREEN);
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.HUD_MAIN_SCREEN);
	}
}