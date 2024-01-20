package com.wisneskey.los.service.lighting.driver;

import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.LightingState;

/**
 * Dummy lighting driver used in development environments with no actual
 * lighting control.
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
public class DummyLightingDriver implements LightingDriver {

	@Override
	public void initialize(Profile profile, LightingState state) {
		// Nothing to do here
	}

	@Override
	public void terminate() {
		// Nothing to do here
	}

	@Override
	public void playEffect(LightingEffectId effectId) {
		// Nothing to do here
	}
}
