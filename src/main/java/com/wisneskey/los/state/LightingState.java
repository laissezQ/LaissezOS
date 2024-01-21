package com.wisneskey.los.state;

import com.wisneskey.los.service.lighting.LightingEffectId;

import javafx.beans.property.BooleanProperty;
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
	 * Speed of the LED effect (0 - 255).
	 * 
	 * @return Speed of the LED effect.
	 */
	IntegerProperty speed();

	/**
	 * Intensity of the LED effect (0 - 255).
	 * 
	 * @return Intensity of the LED effect.
	 */
	IntegerProperty intensity();
	
	/**
	 * Flag indicating if the effect animation should be reversed.
	 * 
	 * @return True iff animation is reversed.
	 */
	BooleanProperty reversed();
	
	/**
	 * First color for effect (usually foreground color).
	 * 
	 * @return Color object for first color.
	 */
	ObjectProperty<Color> firstColor();
	
	/**
	 * Second color for effect (usually background color).
	 * 
	 * @return Color object for second color.
	 */
	ObjectProperty<Color> secondColor();
	
	/**
	 * Third color for effect.
	 * 
	 * @return Color object for third color.
	 */
	ObjectProperty<Color> thirdColor();
	
	/**
	 * Returns the current effect being shown by the lighting system.
	 * 
	 * @return Id of the current lighting effect being shown.
	 */
	ReadOnlyObjectProperty<LightingEffectId> currentEffect();
}
