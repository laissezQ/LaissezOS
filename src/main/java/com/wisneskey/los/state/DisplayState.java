package com.wisneskey.los.state;

import com.wisneskey.los.service.display.DisplayStyle;
import com.wisneskey.los.service.display.SceneId;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * Display service.
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
