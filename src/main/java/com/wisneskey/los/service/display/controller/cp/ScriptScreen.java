package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.script.ScriptId;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Vertical box the scripts will be in.
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

			Button scriptButton = createListButton(scriptId.getDescription());
			scriptButton.setOnAction(e -> runScript(scriptId));
			scriptsBox.getChildren().add(scriptButton);
		}
		
		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.SCRIPT_SCREEN_CLOSE);
	}
}