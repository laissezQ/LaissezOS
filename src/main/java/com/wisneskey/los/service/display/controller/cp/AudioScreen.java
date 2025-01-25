package com.wisneskey.los.service.display.controller.cp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffectId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.AudioState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Controller for the audio effects screen.
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
public class AudioScreen extends AbstractController {

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Slider for controlling the master volume.
	 */
	@FXML
	private Slider masterVolumeSlider;

	/**
	 * Slider for controlling the chap mode volume.
	 */
	@FXML
	private Slider chapVolumeSlider;

	/**
	 * Vertical box the sound effects clips will be.
	 */
	@FXML
	private VBox soundEffectsBox;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		AudioState audioState = chairState().getServiceState(ServiceId.AUDIO);

		// Bind the volume controls.
		masterVolumeSlider.valueProperty().bindBidirectional(audioState.volume());
		chapVolumeSlider.valueProperty().bindBidirectional(audioState.chapModeVolume());

		// Get all of the audio clips and sort their descriptions alphabetically for
		// display.
		Map<String, SoundEffectId> effects = Arrays.stream(SoundEffectId.values())
				.collect(Collectors.toMap(SoundEffectId::getDescription, e -> e));
		List<String> titles = new ArrayList<>(effects.keySet());
		Collections.sort(titles);
		
		for (String title : titles) {

			Button clipButton = createListButton(title);

			SoundEffectId effectId = effects.get(title);
			clipButton.setOnAction(e -> playEffect(effectId));

			soundEffectsBox.getChildren().add(clipButton);
		}
		
		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// Allow remote button A to leave the audio screen.
		if( buttonId == RemoteButtonId.REMOTE_BUTTON_A) {
			resumePressed();
		} else {
			super.remoteButtonPressed(buttonId);
		}
	}	

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.AUDIO_SCREEN_CLOSE);
	}

	
	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	public void playEffect(SoundEffectId effectId) {
		((AudioService) Kernel.kernel().getService(ServiceId.AUDIO)).playEffect(effectId, false);
	}

}