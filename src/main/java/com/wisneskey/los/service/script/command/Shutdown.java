package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;
import com.wisneskey.los.util.RunProcess;

import javafx.application.Platform;

/**
 * Script command to exit the Laissez Operating System after shutting everything
 * down as cleanly as possible.
 * 
 * Copyright (C) 2024 Paul Wisneskey
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author paul.wisneskey@gmail.com
 */
public class Shutdown extends AbstractScriptCommand {

	/**
	 * Command to run to turn off Raspberry Pi.
	 */
	private static final String SHUTDOWN_COMMAND = "shutdown -h now";

	/**
	 * Default value for flag indicating if Raspberry Pi should be shutdown too.
	 */
	private static final boolean DEFAULT_TURN_OFF = false;

	/**
	 * Flag indicating if the Raspberry Pi should be shutdown too.
	 */
	private boolean turnOff = DEFAULT_TURN_OFF;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public boolean getTurnOff() {
		return turnOff;
	}

	public void setTurnOff(boolean turnOff) {
		this.turnOff = turnOff;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommands methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		// Switch back to main screens so the termination messages are visible.
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.CP_MAIN_SCREEN);
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.HUD_MAIN_SCREEN);

		if (turnOff) {
			RunProcess.runCommand(SHUTDOWN_COMMAND);
		}

		// Exit the JavaFX application.
		Platform.exit();
	}
}
