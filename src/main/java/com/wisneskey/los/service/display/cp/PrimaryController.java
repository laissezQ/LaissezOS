package com.wisneskey.los.service.display.cp;

import java.io.IOException;
import java.net.URISyntaxException;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.DisplayStyle;

import javafx.fxml.FXML;


public class PrimaryController {

	@FXML
	private void switchToSecondary() throws IOException, URISyntaxException {		
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).setRoot("secondary");
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).changeDisplayStyle(DisplayStyle.NORD_LIGHT);
	}
}
