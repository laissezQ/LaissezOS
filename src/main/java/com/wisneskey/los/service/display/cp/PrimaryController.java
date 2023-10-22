package com.wisneskey.los.service.display.cp;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.DisplayStyle;

import javafx.fxml.FXML;


public class PrimaryController {

	@FXML
	private void switchToSecondary() {		
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).changeDisplayStyle(DisplayStyle.NORD_LIGHT);
	}
}
