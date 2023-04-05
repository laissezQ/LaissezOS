package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.MessagesToTextAreaListener;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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
	 * Text area for displaying messages.
	 */
	@FXML
	private TextArea messages;

	@FXML
	private Button relayOne;

	@FXML
	private Button relayTwo;

	@FXML
	private Button relayThree;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		chairState().message().addListener(new MessagesToTextAreaListener(messages, MAX_LINE_COUNT));

		relayOne.pressedProperty().addListener(new RelayButtonListener(RelayId.RELAY_1));
		relayTwo.pressedProperty().addListener(new RelayButtonListener(RelayId.RELAY_2));
		relayThree.pressedProperty().addListener(new RelayButtonListener(RelayId.RELAY_3));
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private static class RelayButtonListener implements ChangeListener<Boolean> {

		private RelayId relayId;

		private RelayButtonListener(RelayId relayId) {
			this.relayId = relayId;
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

			if (pressed) {
				((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(relayId);
			} else {
				((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(relayId);
			}
		}
	}
}
