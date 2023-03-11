package com.wisneskey.los.service.display.cp;

import java.io.IOException;
import java.net.URISyntaxException;

import com.wisneskey.los.kernel.LBOSKernel;
import com.wisneskey.los.service.audio.SoundEffect;

import javafx.fxml.FXML;


public class PrimaryController {

	@FXML
	private void switchToSecondary() throws IOException, URISyntaxException {		
		LBOSKernel.displayService().setRoot("secondary");
		LBOSKernel.audioService().playEffect(SoundEffect.BOOT_COMPLETE);
	}
}
