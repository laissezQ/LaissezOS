package com.wisneskey.los.service.lighting.driver;

import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.LightingState;

/**
 * Interface defining an implementation of a lighting system based on a
 * particular technology.
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
public interface LightingDriver {

	/**
	 * Initialize the lighting driver based on the profile.
	 * 
	 * @param profile Profile with settings for the lighting driver.
	 * @param state   Initial state for lighting.
	 */
	void initialize(Profile profile, LightingState state);

	/**
	 * Shutdown the driver when terminating the service.
	 */
	void terminate();

	/**
	 * Play the designated lighting effect.
	 * 
	 * @param effectId   Id of the effect to play.
	 * @param lightingState Current lighting state.
	 */
	void playEffect(LightingEffectId effectId, LightingState lightingState);

	/**
	 * Change the brightness of the currently playing effect.
	 * 
	 * @param brightness New brightness for effect.
	 */
	void changeBrightness(int brightness);

	/**
	 * Change the speed of the currently playing effect.
	 * 
	 * @param speed New speed for effect.
	 */
	void changeSpeed(int speed);
	
	/**
	 * Change the intensity of the currently playing effect.
	 * 
	 * @param intensity New intensity for effect.
	 */
	void changeIntensity(int intensity);
	
	/**
	 * Change the reverse animation setting for the currently playing effect.
	 * 
	 * @param reversed Reversed flag for animation.
	 */
	void changeReversed(boolean reversed);
	
	/**
	 * Change the colors of the currently playing effect.
	 * 
	 * @param state State with new colors.
	 */
	void changeColor(LightingState state);
}
