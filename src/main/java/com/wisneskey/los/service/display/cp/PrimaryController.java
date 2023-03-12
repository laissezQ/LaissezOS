package com.wisneskey.los.service.display.cp;

import java.io.IOException;
import java.net.URISyntaxException;

import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;

import javafx.fxml.FXML;


public class PrimaryController {

	@FXML
	private void switchToSecondary() throws IOException, URISyntaxException {		
		((DisplayService) LOSKernel.kernel().getService(ServiceId.DISPLAY)).setRoot("secondary");
	}
}
