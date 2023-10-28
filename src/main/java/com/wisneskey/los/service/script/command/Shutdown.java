package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;

/**
 * Script command to exit the Laissez Operating System after shutting everything
 * down as cleanly as possible.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Shutdown extends AbstractScriptCommand {

	// ----------------------------------------------------------------------------------------
	// ScriptCommands methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		// Switch back to main screens so the termination messages are visible.
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.CP_MAIN_SCREEN);
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.HUD_MAIN_SCREEN);

		// Use system exit and rely on the fallback kernel termination to shut everything down.
		sleepForSeconds(2.0);
		System.exit(0);
	}
}
