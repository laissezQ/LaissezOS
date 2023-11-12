package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Controller for the audio effects screen.
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
public class AboutScreen extends AbstractController {

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


	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {
		// Nothing to do here at the moment.
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.AUDIO_SCREEN_CLOSE);
	}
}