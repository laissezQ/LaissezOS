package com.wisneskey.los.state;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * security service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface SecurityState extends State {

	/**
	 * Return the message displayed on the lock screen.
	 * 
	 * @return Message to display on lock screen.
	 */
	ReadOnlyStringProperty lockMessage();
}
