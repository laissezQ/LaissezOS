package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel boot screen.
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

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

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