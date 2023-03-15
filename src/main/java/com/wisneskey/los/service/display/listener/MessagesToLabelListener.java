package com.wisneskey.los.service.display.listener;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public class MessagesToLabelListener implements ChangeListener<String> {

	private Label label;
	
	public MessagesToLabelListener(Label label) {
		this.label = label;
	}
	
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
