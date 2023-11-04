package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToTextAreaListener;
import com.wisneskey.los.service.display.listener.relay.RelayWhilePressedListener;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.service.security.SecurityService;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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

		RelayWhilePressedListener.add(backrestDownButton, RelayId.A_RETRACT, "A retract...\n");
		RelayWhilePressedListener.add(backrestUpButton, RelayId.A_EXTEND, "A extend...\n");

		RelayWhilePressedListener.add(footrestDownButton, RelayId.B_RETRACT, "B retract...\n");
		RelayWhilePressedListener.add(footrestUpButton, RelayId.B_EXTEND, "B extend...\n");

		logo.setOnMouseClicked(new LogoClickHandler());
	}

	/**
	 * Method invoked by lock chair button.
	 */
	public void lockChair() {

		((SecurityService) Kernel.kernel().getService(ServiceId.SECURITY)).lockChair(SecurityService.DEFAULT_LOCK_MESSAGE,
				ScriptId.SECURITY_UNLOCKED, ScriptId.SECURITY_UNLOCK_FAILED, null, null);
	}

	/**
	 * Method invoked when the audio button is pressed.
	 */
	public void audioPressed() {	
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.AUDIO_SCREEN_OPEN);
	}
	
	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Mouse event handler that opens the secret system menu if the logo is
	 * double-clicked.
	 */
	private static class LogoClickHandler implements EventHandler<MouseEvent> {

		public void handle(MouseEvent mouseEvent) {

			if ((mouseEvent.getButton().equals(MouseButton.PRIMARY)) && (mouseEvent.getClickCount() == 2)) {

				((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.SYSTEM_SCREEN_OPEN);
			}
		}
	}
}
