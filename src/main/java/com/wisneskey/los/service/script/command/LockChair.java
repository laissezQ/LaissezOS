package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.security.SecurityService;

/**
 * Script command to lock the chair's operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LockChair extends AbstractScriptCommand {

	// ----------------------------------------------------------------------------------------
	// ScriptCommands methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		((SecurityService) Kernel.kernel().getService(ServiceId.SECURITY)).lockChair();
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "LockChair[]";
	}
}
