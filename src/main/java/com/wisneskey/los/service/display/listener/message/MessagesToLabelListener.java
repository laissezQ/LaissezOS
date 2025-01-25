package com.wisneskey.los.service.display.listener.message;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * Change listener that monitors for new chair messages and sets them to be
 * displayed in a label. Properly handles messages that do not have a new line
 * (e.g. should be added to the previous message).
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
public class MessagesToLabelListener implements ChangeListener<String> {

	/**
	 * Label to use for displaying the message.
	 */
	private Label label;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a lister that sets the specified label.
	 * 
	 * @param label Label to use to display the chair message.
	 */
	public MessagesToLabelListener(Label label) {
		this.label = label;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

		Platform.runLater(() -> {
			if (newValue == null) {
				label.setText(null);
			} else {
				if ((oldValue == null) || oldValue.endsWith("\n")) {
					label.setText(newValue);
				} else {
					String builtMessage = label.getText() + newValue;
					label.setText(builtMessage);
				}
			}
		});
	}
}
