package com.wisneskey.los.service.display.cp;

import java.io.IOException;

import com.wisneskey.los.kernel.LBOSKernel;
import com.wisneskey.los.service.audio.SoundEffect;

import javafx.fxml.FXML;

public class SecondaryController {

	@FXML
	private void switchToPrimary() throws IOException {
		LBOSKernel.displayService().setRoot("primary");
		LBOSKernel.audioService().playEffect(SoundEffect.BOOT_COMPLETE);
	}
}