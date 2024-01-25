package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.lighting.LightingService;

/**
 * Script command to run a specified lighting effect.
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
public class RunLightingEffect extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(RunLightingEffect.class);

	/**
	 * Id of the lighting effect to run.
	 */
	private LightingEffectId effectId;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public LightingEffectId getEffectId() {
		return effectId;
	}

	public void setEffectId(LightingEffectId effectId) {
		this.effectId = effectId;
	}
	

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		LightingEffectId playId = getEffectId();
		if( playId == null ) {
			LOGGER.warn("No lighting effect configured to play.");
		} else {
			
			((LightingService) Kernel.kernel().getService(ServiceId.LIGHTING)).playEffect(playId);
		}
	}
}