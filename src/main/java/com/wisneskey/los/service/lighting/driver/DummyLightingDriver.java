package com.wisneskey.los.service.lighting.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.LightingState;

/**
 * Dummy lighting driver used in development environments with no actual
 * lighting control.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyLightingDriver.class);

	@Override
	public void initialize(Profile profile, LightingState state) {
		state.intensity().set(128);
		state.speed().set(128);
		state.reversed().set(false);
	}

	@Override
	public void terminate() {
		// Nothing to do here
	}

	@Override
	public void playEffect(LightingEffectId effectId, LightingState lightingState) {
		LOGGER.info("Switching lighting effect: effectId={}", effectId);
	}

	@Override
	public void changeBrightness(int brightness) {
		LOGGER.info("Changing brightness: newValue={}", brightness);
	}

	@Override
	public void changeColor(LightingState state) {
		LOGGER.info("Changing colors: first={} second={} third={}", state.firstColor().getValue(),
				state.secondColor().getValue(), state.thirdColor().getValue());
	}

	@Override
	public void changeSpeed(int speed) {
		LOGGER.info("Changing speed: newValue={}", speed);
	}

	@Override
	public void changeIntensity(int intensity) {
		LOGGER.info("Changing intensity: newValue={}", intensity);
	}

	@Override
	public void changeReversed(boolean reversed) {
		LOGGER.info("Changing reversed flag: newValue={}", reversed);
	}
}