package com.wisneskey.los.service.display.controller.cp;

import java.util.EnumMap;
import java.util.Map;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.display.listener.relay.RelayWhilePressedListener;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.RelayState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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

				Button effectButton = createListButton(relayId.getDescription());
				effectButton.setOnAction(e -> toggleEffect(relayId, effectButton));
				effectButton.setTextFill( isEnergized(relayId) ? TEXT_ENABLED : TEXT_DISABLED);
				checkedEffectsBox.getChildren().add(effectButton);
				toggleableMap.put(relayId, effectButton);

			} else {

				Button effectButton = createListButton(relayId.getDescription());
				effectButton.setTextFill(Color.WHITE);
				RelayWhilePressedListener.add(effectButton, relayId, null);
				effectsBox.getChildren().add(effectButton);
			}
		}

		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.EFFECT_SCREEN_CLOSE);
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
}