package com.wisneskey.los.service.display.controller.cp;

import java.util.Random;
import java.util.Set;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffectId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel boot screen.
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
public class ChapScreen extends AbstractController {

	/**
	 * Fisher Price logo.
	 */
	@FXML
	private ImageView logo;

	@FXML
	private Button soundOneButton;

	@FXML
	private Button soundTwoButton;

	@FXML
	private Button soundThreeButton;

	@FXML
	private Button soundFourButton;

	@FXML
	private Button soundFiveButton;

	@FXML
	private Button soundSixButton;

	@FXML
	private Button lightingOneButton;
	
	@FXML
	private Button lightingTwoButton;
	
	@FXML
	private Button lightingThreeButton;
	
	@FXML
	private Button lightingFourButton;
	
	@FXML
	private Button lightingFiveButton;
	
	@FXML
	private Button lightingSixButton;

	/**
	 * Random number generator for selecting sound clips to associate with
	 * buttons.
	 */
	private Random random = new Random();

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {
		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
		shuffleSoundButtons();
	}

	@FXML
	public void shufflePressed() {
		shuffleSoundButtons();
	}

	@FXML
	private void soundButtonPressed(ActionEvent event) {
		Button button = (Button) event.getSource();
		SoundEffectId soundId = (SoundEffectId) button.getUserData();
		((AudioService) Kernel.kernel().getService(ServiceId.AUDIO)).playEffect(soundId, false);
	}

	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// Allow remote button A to leave chap mode.
		if (buttonId == RemoteButtonId.REMOTE_BUTTON_A) {
			resumePressed();
		} else
		{
			super.remoteButtonPressed(buttonId);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method invoke to close the chap screen via the resume button, remote
	 * button, or a logo doubleclick.
	 */
	private void resumePressed() {
		runScript(ScriptId.CHAP_SCREEN_CLOSE);
	}

	/**
	 * Assigns random chap mode sound effects to the sound buttons.
	 */
	private void shuffleSoundButtons() {

		// Start with the set of all chap mode sound effects. As they are selected
		// for a button, they will be remove from the set so that no duplicates are
		// assigned.
		Set<SoundEffectId> chapSounds = SoundEffectId.chapModeEffects();

		assignSoundToButton(soundOneButton, chapSounds);
		assignSoundToButton(soundTwoButton, chapSounds);
		assignSoundToButton(soundThreeButton, chapSounds);
		assignSoundToButton(soundFourButton, chapSounds);
		assignSoundToButton(soundFiveButton, chapSounds);
		assignSoundToButton(soundSixButton, chapSounds);
	}

	/**
	 * Selects a sound randomly from a set of sound effects and assigns it a
	 * button that will play it if pressed.
	 */
	private void assignSoundToButton(Button button, Set<SoundEffectId> chapSounds) {

		if (chapSounds.isEmpty()) {
			throw new LaissezException("No chap sounds left to pick from.");
		}

		int pick = random.nextInt(chapSounds.size());
		SoundEffectId soundId = chapSounds.stream().skip(pick).findFirst().orElse(null);

		if (soundId != null) {
			button.setText(soundId.getShortName());
			button.setUserData(soundId);

			chapSounds.remove(soundId);
		}
	}
}