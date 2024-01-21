package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.lighting.LightingService;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.LightingState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Controller for the lighting effects screen.
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
public class LightingScreen extends AbstractController {

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Slider for controlling the brightness.
	 */
	@FXML
	private Slider brightnessSlider;

	/**
	 * Slider for controlling the speed.
	 */
	@FXML
	private Slider speedSlider;
	
	/**
	 * Slider for controlling the intensity.
	 */
	@FXML
	private Slider intensitySlider;
	
	/**
	 * Check box for toggling a reversed animation.
	 */
	@FXML
	private CheckBox reversedCheckbox;
	
	/**
	 * Vertical box the sound effects clips will be.
	 */
	@FXML
	private VBox effectsBox;

	/**
	 * Color picker for the first color.
	 */
	@FXML
	private ColorPicker firstPicker;
	
	/**
	 * Color picker for the second color.
	 */
	@FXML
	private ColorPicker secondPicker;

	/**
	 * Color picker for the third color.
	 */
	@FXML
	private ColorPicker thirdPicker;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		LightingState lightingState = chairState().getServiceState(ServiceId.LIGHTING);

		// Bind the various parameter controls.
		brightnessSlider.valueProperty().bindBidirectional(lightingState.brightness());
		speedSlider.valueProperty().bindBidirectional(lightingState.speed());
		intensitySlider.valueProperty().bindBidirectional(lightingState.intensity());
		reversedCheckbox.selectedProperty().bindBidirectional(lightingState.reversed());

		// Bind the color pickers to their state counterparts.
		firstPicker.valueProperty().bindBidirectional(lightingState.firstColor());
		secondPicker.valueProperty().bindBidirectional(lightingState.secondColor());
		thirdPicker.valueProperty().bindBidirectional(lightingState.thirdColor());
		
		for (LightingEffectId effectId : LightingEffectId.values() ) {

			Button effectButton = createListButton(effectId.getDescription());
			effectButton.setOnAction(e -> triggerEffect(effectId));
			effectsBox.getChildren().add(effectButton);
		}
		
		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// Allow remote button A to leave the lighting screen.
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
		runScript(ScriptId.LIGHTING_SCREEN_CLOSE);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void triggerEffect(LightingEffectId effectId) {
		
		((LightingService) kernel().getService(ServiceId.LIGHTING)).playEffect(effectId);
	}
}