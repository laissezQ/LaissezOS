package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;

public class BootMessage extends AbstractScriptCommand {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void perform() {
		Kernel.kernel().bootMessage(message);
	}
}
