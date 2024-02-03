package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * Controller for the audio effects screen.
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
public class AboutScreen extends AbstractController {

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {
		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.ABOUT_SCREEN_CLOSE);
	}
	
	/**
	 * Method invoked when Ann Heren's name is pressed.
	 */
	public void annPressed() {
		runScript(ScriptId.GAME_SCREEN_OPEN);
	}
	
	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {
		
		// Allow remote button A to leave the about screen.
		if( buttonId == RemoteButtonId.REMOTE_BUTTON_A) {
			resumePressed();
		} else {
			super.remoteButtonPressed(buttonId);
		}
	}
}