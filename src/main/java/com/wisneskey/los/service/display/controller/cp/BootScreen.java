package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToTextAreaListener;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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
public class BootScreen extends AbstractController {

	/**
	 * Maximum number of lines to keep in the messages text area.
	 */
	private static final int MAX_LINE_COUNT = 100;

	/**
	 * Text area for displaying messages.
	 */
	@FXML
	private TextArea messages;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		chairState().message().addListener(new MessagesToTextAreaListener(messages, MAX_LINE_COUNT));
	}
}