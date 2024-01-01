package com.wisneskey.los.service.display.controller;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.state.ChairState;

/**
 * Abstract base classes for JavaFX controllers that provides utility methods
 * for accessing chair state and services.
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
public abstract class AbstractController implements SceneController {

	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {
		// By default, ignore the remote buttons. Scene controllers can override
		// this method if they want to respond to remote button presses.
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the kernel.
	 * 
	 * @return Kernel for the chair's operating system.
	 */
	protected Kernel kernel() {
		return Kernel.kernel();
	}

	/**
	 * Return the chair state object.
	 * 
	 * @return Top level state object for the chair.
	 */
	protected ChairState chairState() {
		return Kernel.kernel().chairState();
	}
}
