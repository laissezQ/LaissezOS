package com.wisneskey.los.service.display.controller.hud;

import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToLabelListener;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the heads up display main screen.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class MainScreen extends AbstractController {

	/**
	 * Label to use to show last message in.
	 */

	@FXML
	private Label message;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		chairState().message().addListener(new MessagesToLabelListener(message));
	}
}
