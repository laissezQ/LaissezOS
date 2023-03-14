package com.wisneskey.los.service.display.controller.cp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class BootScreen {

	private static final Logger LOGGER = LoggerFactory.getLogger(BootScreen.class);

	@FXML
	private TextArea bootMessages;

	@FXML
	public void initialize() {

		Kernel.kernel().chairState().bootMessage().addListener(new BootMessageListener());
	}

	private class BootMessageListener implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if (newValue == null) {
				bootMessages.clear();
			} else {
				bootMessages.selectEnd();
				bootMessages.insertText(bootMessages.textProperty().getValue().length(), newValue);
			}
		}
	}
}
