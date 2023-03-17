package com.wisneskey.los.service.display.listener;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * Change listener that monitors for new chair messages and sets them to be
 * displayed in a label. Properly handles messages that do not have a new line
 * (e.g. should be added to the previous message).
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
	 * @param label
	 *          Label to use to display the chair message.
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
