package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the control panel boot screen.
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
public class ChapScreen extends AbstractController {

	/**
	 * Fisher Price logo.
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
		
		logo.setOnMouseClicked(new LogoClickHandler());
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Mouse event handler that opens the secret system menu if the logo is
	 * double-clicked.
	 */
	private static class LogoClickHandler implements EventHandler<MouseEvent> {

		public void handle(MouseEvent mouseEvent) {

			if ((mouseEvent.getButton().equals(MouseButton.PRIMARY)) && (mouseEvent.getClickCount() == 2)) {

				((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(ScriptId.CHAP_SCREEN_CLOSE);
			}
		}
	}
}