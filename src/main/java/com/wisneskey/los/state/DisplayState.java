package com.wisneskey.los.state;

import com.wisneskey.los.service.display.DisplayStyle;

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
}
