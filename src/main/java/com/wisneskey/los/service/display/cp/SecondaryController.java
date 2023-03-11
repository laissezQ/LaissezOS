package com.wisneskey.los.service.display.cp;

import java.io.IOException;

import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.audio.SoundEffect;

import javafx.fxml.FXML;

public class SecondaryController {

	@FXML
	private void switchToPrimary() throws IOException {
		LOSKernel.displayService().setRoot("primary");
		LOSKernel.audioService().playEffect(SoundEffect.BOOT_COMPLETE);
	}
}