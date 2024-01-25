package com.wisneskey.los.service.script.command;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.LightingService;

/**
 * Script command to restore the last stored lighting state.
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
public class RestoreLightingState extends AbstractScriptCommand {

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		((LightingService) Kernel.kernel().getService(ServiceId.LIGHTING)).restoreState();
	}

}
