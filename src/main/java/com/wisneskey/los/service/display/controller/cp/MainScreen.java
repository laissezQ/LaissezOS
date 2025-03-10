package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.bar.BarButtonListener;
import com.wisneskey.los.service.display.listener.bar.BarStateListener;
import com.wisneskey.los.service.display.listener.bar.TapButtonListener;
import com.wisneskey.los.service.display.listener.message.MessagesToTextAreaListener;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.lighting.LightingService;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.LightingState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel boot screen.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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

	/**
	 * Button for controlling the pop up bar.
	 */
	@FXML
	private Button barButton;

	/**
	 * Button for controlling the water tap in the bar.
	 */
	@FXML
	private Button tapButton;

	/**
	 * Button for showing current lighting effect and selecting new one at random.
	 */
	@FXML
	private Button buttonCurrentLighting;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		chairState().message().addListener(new MessagesToTextAreaListener(messages, MAX_LINE_COUNT));
		logo.setOnMouseClicked(new DoubleClickListener(e -> runScript(ScriptId.SYSTEM_SCREEN_OPEN)));

		// Listeners for controlling the bar and its controls.
		BarButtonListener.add(barButton);
		TapButtonListener.add(tapButton);
		chairState().barState().addListener(new BarStateListener(barButton, tapButton));

		// Listeners for audio and lighting effects
		LightingState lightingState = chairState().getServiceState(ServiceId.LIGHTING);
		lightingState.currentEffect().addListener(new LightingListener());
		buttonCurrentLighting.setText(lightingState.currentEffect().getValue().getDescription());
	}

	/**
	 * Method invoked by lock chair button.
	 */
	public void lockChair() {
		runScript(ScriptId.SECURITY_LOCK);
	}

	/**
	 * Method invoked when the about button is pressed.
	 */
	public void aboutPressed() {
		runScript(ScriptId.ABOUT_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the audio button is pressed.
	 */
	public void audioPressed() {
		runScript(ScriptId.AUDIO_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the music button is pressed.
	 */
	public void musicPressed() {
		runScript(ScriptId.MUSIC_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the chap mode button is pressed.
	 */
	public void chapPressed() {
		runScript(ScriptId.CHAP_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the effects button is pressed.
	 */
	public void effectPressed() {
		runScript(ScriptId.EFFECT_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the lighting button is pressed.
	 */
	public void lightingPressed() {
		runScript(ScriptId.LIGHTING_SCREEN_OPEN);
	}

	/**
	 * Method invoked when the current lighting button is pressed.
	 */
	public void randomLighting() {
		((LightingService) kernel().getService(ServiceId.LIGHTING)).playRandomEffect(false);
	}

	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// Allow remote button A to lock the chair.
		if (buttonId == RemoteButtonId.REMOTE_BUTTON_A) {
			runScript(ScriptId.REMOTE_LOCK);
		} else {
			super.remoteButtonPressed(buttonId);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Listener that monitors the which lighting effect is playing and updates the
	 * label accordingly.
	 */
	private class LightingListener implements ChangeListener<LightingEffectId> {

		@Override
		public void changed(ObservableValue<? extends LightingEffectId> observable, LightingEffectId oldValue,
				LightingEffectId newValue) {
			buttonCurrentLighting.setText(newValue == null ? "None" : newValue.getDescription());
		}
	}
}
