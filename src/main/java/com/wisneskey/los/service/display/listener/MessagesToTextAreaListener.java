package com.wisneskey.los.service.display.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

public class MessagesToTextAreaListener implements ChangeListener<String> {

	private static final String SOLID_BLOCK = "\u2588";

	private TextArea textArea;

	private int lineCount = 0;
	private int maxLines = 0;

	public MessagesToTextAreaListener(TextArea textArea, int maxLines) {
		this.textArea = textArea;
		this.maxLines = maxLines;
	}

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
