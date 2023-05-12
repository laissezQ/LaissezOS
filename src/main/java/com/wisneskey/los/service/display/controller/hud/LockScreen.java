package com.wisneskey.los.service.display.controller.hud;

import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToLabelListener;
import com.wisneskey.los.state.SecurityState;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel lock screen.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LockScreen extends AbstractController {

	@FXML
	private Label lockMessage;

	@FXML
	private ImageView logo;


	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		// Set the initial lock message.
		SecurityState state = chairState().getServiceState(ServiceId.SECURITY);
		lockMessage.setText(state.lockMessage().getValue());
		
		state.lockMessage().addListener(new MessagesToLabelListener(lockMessage));
	}
}
