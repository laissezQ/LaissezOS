package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Controller for the scripts screen.
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
public class ScriptScreen extends AbstractController {

	/**
	 * Width to make the buttons for playing audio clips.
	 */
	private static final double SCRIPT_BUTTON_WIDTH = 364.0;
	
	/**
	 * Font to use for the buttons for playing audio clips.
	 */
	private static final Font SCRIPT_BUTTON_FONT = new Font("System Bold", 14.0);

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
	 * Vertical box the sound effects clips will be.
	 */
	@FXML
	private VBox scriptsBox;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		for( ScriptId scriptId : ScriptId.values()) {

			Button scriptButton = new Button(scriptId.getDescription());
			scriptButton.setFont(SCRIPT_BUTTON_FONT);
			scriptButton.setEffect(new DropShadow());
			scriptButton.setMinWidth(SCRIPT_BUTTON_WIDTH);
			
			scriptButton.setOnAction(e ->runScript(scriptId));

			scriptsBox.getChildren().add(scriptButton);
		}
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.AUDIO_SCREEN_CLOSE);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	public void runScript(ScriptId scriptId) {

		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(scriptId);
	}
}