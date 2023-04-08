package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToTextAreaListener;
import com.wisneskey.los.service.display.listener.relay.RelayWhilePressedListener;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.security.SecurityService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel boot screen.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class MainScreen extends AbstractController {

	/**
	 * Maximum number of lines to keep in the messages text area.
	 */
	private static final int MAX_LINE_COUNT = 10;

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;
	
	/**
	 * Text area for displaying messages.
	 */
	@FXML
	private TextArea messages;

	@FXML
	private Button barDownButton;

	@FXML
	private Button barUpButton;

	@FXML
	private Button backrestDownButton;

	@FXML
	private Button backrestUpButton;

	@FXML
	private Button footrestDownButton;

	@FXML
	private Button footrestUpButton;

	
	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		chairState().message().addListener(new MessagesToTextAreaListener(messages, MAX_LINE_COUNT));

		RelayWhilePressedListener.add(barDownButton, RelayId.BAR_LOWER, "Lowering bar...\n");
		RelayWhilePressedListener.add(barUpButton, RelayId.BAR_RAISE, "Raising bar...\n");
		
		RelayWhilePressedListener.add(backrestDownButton, RelayId.BACKREST_RAISE, "Raising backrest...\n");
		RelayWhilePressedListener.add(backrestUpButton, RelayId.BACKREST_LOWER, "Lowering backrest...\n");

		RelayWhilePressedListener.add(footrestDownButton, RelayId.FOOTREST_LOWER, "Lowering footrest...\n");
		RelayWhilePressedListener.add(footrestUpButton, RelayId.FOOTREST_RAISE, "Raising footrest...\n");
	}
	
	/**
	 * Method invoked by lock chair button.
	 */
	public void lockChair() {
		
		((SecurityService) Kernel.kernel().getService(ServiceId.SECURITY)).lockChair();
	}
}
