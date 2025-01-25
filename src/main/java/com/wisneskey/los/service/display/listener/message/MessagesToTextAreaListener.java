package com.wisneskey.los.service.display.listener.message;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 * Change listener that monitors for new chair messages and adds them to a
 * scrolling text area. Properly handles messages that do not have a new line
 * (e.g. should be added to the previous message). Tracks the number of lines in
 * the text area and trims the oldest when the maximum is reached.
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
 *
 */
public class MessagesToTextAreaListener implements ChangeListener<String> {

	/**
	 * Constant representing a solid block character to use for a simulated
	 * cursor.
	 */
	private static final String SOLID_BLOCK = "\u2588";

	/**
	 * Text area to append the messages to.
	 */
	private TextArea textArea;

	/**
	 * Maximum number of lines to allow in the text area.
	 */
	private int maxLines = 0;

	/**
	 * Number of lines currently in the text area.
	 */
	private int lineCount = 0;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a listener that uses the supplied text area with the specified
	 * maximum number of lines.
	 * 
	 * @param textArea Text area to use for displaying messages.
	 * @param maxLines Maximum number of lines to keep in the text area.
	 */
	public MessagesToTextAreaListener(TextArea textArea, int maxLines) {
		this.textArea = textArea;
		this.maxLines = maxLines;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (newValue == null) {
			textArea.setText(SOLID_BLOCK);
			lineCount = 0;
		} else {
			int length = textArea.getText().length();
			textArea.selectRange(length - 1, length);
			textArea.replaceSelection(newValue + SOLID_BLOCK);

			if (newValue.contains("\n")) {
				lineCount += countNewLines(newValue);
			}

			while (lineCount > maxLines) {
				int firstLineEnd = textArea.getText().indexOf("\n");
				textArea.selectRange(0, firstLineEnd + 1);
				textArea.replaceSelection("");
				lineCount--;
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Count the number of new lines in the latest message by counting the number
	 * of newline characters it contains.
	 * 
	 * @param  message Message to count the newlines in.
	 * @return         Number of newline characters found in the message.
	 */
	private int countNewLines(String message) {

		int newLineCount = 0;
		for (int index = 0; index < message.length(); index++) {
			if (message.charAt(index) == '\n') {
				newLineCount++;
			}
		}

		return newLineCount;
	}
}
