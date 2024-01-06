package com.wisneskey.los.service.display.controller.cp;

import java.util.EnumMap;
import java.util.Map;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.relay.RelayWhilePressedListener;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.state.RelayState;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controller for the effects screen.
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
public class EffectScreen extends AbstractController {

	/**
	 * Width to make the buttons for toggling effects.
	 */
	private static final double EFFECTS_BUTTON_WIDTH = 364.0;

	/**
	 * Font to use for the buttons for toggling effects.
	 */
	private static final Font EFFECTS_BUTTON_FONT = new Font("System Bold", 14.0);

	/**
	 * Color to use for button labels for enabled effects.
	 */
	private static final Color TEXT_ENABLED = Color.LIGHTGREEN;

	/**
	 * Color to use for button labels for disabled effects.
	 */
	private static final Color TEXT_DISABLED = Color.WHITE;
	
	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Button to return to main screen.
	 */
	@FXML
	private Button resumeButton;

	/**
	 * Vertical box the non-toggleable effects will be in.
	 */
	@FXML
	private VBox effectsBox;

	/**
	 * Vertical box the toggleable effects will be in.
	 */
	@FXML
	private VBox checkedEffectsBox;

	/**
	 * Map of togglable relay ids to their buttons.
	 */
	private Map<RelayId, Button> toggleableMap = new EnumMap<>(RelayId.class);

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		for (RelayId relayId : RelayId.values()) {

			if (relayId.isTogglable()) {

				Button effectButton = new Button(relayId.getDescription());
				effectButton.setFont(EFFECTS_BUTTON_FONT);
				effectButton.setEffect(new DropShadow());
				effectButton.setMinWidth(EFFECTS_BUTTON_WIDTH);
				effectButton.setPadding(new Insets(10, 0, 10, 0));
				effectButton.setOnAction(e -> toggleEffect(relayId, effectButton));

				boolean energized = isEnergized(relayId);
				effectButton.setTextFill( energized ? TEXT_ENABLED : TEXT_DISABLED);

				checkedEffectsBox.getChildren().add(effectButton);
				toggleableMap.put(relayId, effectButton);

			} else {

				Button effectButton = new Button(relayId.getDescription());
				effectButton.setFont(EFFECTS_BUTTON_FONT);
				effectButton.setEffect(new DropShadow());
				effectButton.setMinWidth(EFFECTS_BUTTON_WIDTH);
				effectButton.setPadding(new Insets(10, 0, 10, 0));
				effectButton.setTextFill(Color.WHITE);
				
				RelayWhilePressedListener.add(effectButton, relayId, null);

				effectsBox.getChildren().add(effectButton);
			}
		}

		logo.setOnMouseClicked(new LogoClickHandler());
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.AUDIO_SCREEN_CLOSE);
	}

	/**
	 * Refresh the state of the checkboxes.
	 */
	public void refreshCheckboxes() {

		for (Map.Entry<RelayId, Button> entry : toggleableMap.entrySet()) {

			boolean energized = isEnergized(entry.getKey());
			entry.getValue().setTextFill( energized ? TEXT_ENABLED : TEXT_DISABLED);
		}
	}

	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void sceneShown() {
		refreshCheckboxes();
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private boolean isEnergized(RelayId relayId) {
		RelayState state = kernel().chairState().getServiceState(ServiceId.RELAY);
		return state.getState(relayId).getValue();

	}

	private void toggleEffect(RelayId relayId, Button effectButton) {

		if (isEnergized(relayId)) {
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(relayId);
		} else {
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(relayId);
		}

		refreshCheckboxes();
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Mouse event handler that exits the secret system menu if the logo is
	 * double-clicked.
	 */
	private class LogoClickHandler implements EventHandler<MouseEvent> {

		public void handle(MouseEvent mouseEvent) {

			if ((mouseEvent.getButton().equals(MouseButton.PRIMARY)) && (mouseEvent.getClickCount() == 2)) {
				resumePressed();
			}
		}
	}

}