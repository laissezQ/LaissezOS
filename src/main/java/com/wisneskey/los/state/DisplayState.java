package com.wisneskey.los.state;

import com.wisneskey.los.service.display.DisplayStyle;
import com.wisneskey.los.service.display.SceneId;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * Display service.
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
public interface DisplayState extends State {

	/**
	 * Returns the read only property for the current display style.
	 * 
	 * @return Read only property containing the id of the currently selected
	 *         display style.
	 */
	ReadOnlyObjectProperty<DisplayStyle> currentStyle();

	/**
	 * Returns the id of the current scene on the heads up display.
	 * 
	 * @return Id of current HUD scene.
	 */
	ReadOnlyObjectProperty<SceneId> hudScene();

	/**
	 * Returns the id of the current scene on the control panel.
	 * 
	 * @return Id of the current control panel scene.
	 */
	ReadOnlyObjectProperty<SceneId> cpScene();
}
