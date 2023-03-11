package com.wisneskey.los.service.display.cp;

import java.io.IOException;
import java.net.URISyntaxException;

import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.audio.SoundEffect;

import javafx.fxml.FXML;


public class PrimaryController {

	@FXML
	private void switchToSecondary() throws IOException, URISyntaxException {		
		LOSKernel.displayService().setRoot("secondary");
		LOSKernel.audioService().playEffect(SoundEffect.BOOT_COMPLETE);
	}
}
