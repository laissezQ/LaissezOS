package com.wisneskey.los.state;

import com.wisneskey.los.service.lighting.LightingEffectId;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.paint.Color;

/**
 * Sate object for the state of the lighting systems.
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
public interface LightingState extends State {

	/**
	 * Brightness of the LED lights as a percentage.
	 * 
	 * @return Brightness of the lights.
	 */
	IntegerProperty brightness();
	
	/**
	 * Current foreground color.
	 * 
	 * @return Color object for foreground color.
	 */
	ObjectProperty<Color> foreground();
	
	/**
	 * Current background color.
	 * 
	 * @return Color object for background color.
	 */
	ObjectProperty<Color> background();
	
	/**
	 * Returns the current effect being shown by the lighting system.
	 * 
	 * @return Id of the current lighting effect being shown.
	 */
	ReadOnlyObjectProperty<LightingEffectId> currentEffect();
}
