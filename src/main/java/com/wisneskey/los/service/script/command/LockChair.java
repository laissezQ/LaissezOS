package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.SceneId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.security.SecurityService;

/**
 * Script command to lock the chair's operating system.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public class LockChair extends AbstractScriptCommand {

	private String lockMessage = SecurityService.DEFAULT_LOCK_MESSAGE;
	private ScriptId unlockScript;
	private ScriptId unlockFailedScript;
	private SceneId hudScene;
	private SceneId cpScene;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public String getLockMessage() {
		return lockMessage;
	}

	public void setLockMessage(String lockMessage) {
		this.lockMessage = lockMessage;
	}

	public ScriptId getUnlockScript() {
		return unlockScript;
	}

	public void setUnlockScript(ScriptId unlockScript) {
		this.unlockScript = unlockScript;
	}

	public ScriptId getUnlockFailedScript() {
		return unlockFailedScript;
	}

	public void setUnlockFailedScript(ScriptId unlockFailedScript) {
		this.unlockFailedScript = unlockFailedScript;
	}

	public SceneId getHudScene() {
		return hudScene;
	}

	public void setHudScene(SceneId hudScene) {
		this.hudScene = hudScene;
	}

	public SceneId getCpScene() {
		return cpScene;
	}

	public void setCpScene(SceneId cpScene) {
		this.cpScene = cpScene;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommands methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		((SecurityService) Kernel.kernel().getService(ServiceId.SECURITY)).lockChair(lockMessage, unlockScript,
				unlockFailedScript, hudScene, cpScene);
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "LockChair[]";
	}
}
