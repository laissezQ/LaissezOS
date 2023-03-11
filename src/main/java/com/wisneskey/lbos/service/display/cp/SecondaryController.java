package com.wisneskey.lbos.service.display.cp;

import java.io.IOException;

import com.wisneskey.lbos.kernel.LBOSKernel;
import com.wisneskey.lbos.service.audio.SoundEffect;

import javafx.fxml.FXML;

public class SecondaryController {

	@FXML
	private void switchToPrimary() throws IOException {
		LBOSKernel.displayService().setRoot("primary");
		LBOSKernel.audioService().playEffect(SoundEffect.BOOT_COMPLETE);
	}
}