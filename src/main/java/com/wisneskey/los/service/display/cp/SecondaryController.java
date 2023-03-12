package com.wisneskey.los.service.display.cp;

import java.io.IOException;

import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;

import javafx.fxml.FXML;

public class SecondaryController {

	@FXML
	private void switchToPrimary() throws IOException {
		((DisplayService) LOSKernel.kernel().getService(ServiceId.DISPLAY)).setRoot("primary");
	}
}