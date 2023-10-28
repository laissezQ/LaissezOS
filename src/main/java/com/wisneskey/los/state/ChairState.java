package com.wisneskey.los.state;

import com.wisneskey.los.service.ServiceId;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface ChairState extends State {

	// ----------------------------------------------------------------------------------------
	// Top level state properties.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the master state of the chair.
	 * 
	 * @return Master state property of the chair.
	 */
	ReadOnlyObjectProperty<MasterState> masterState();

	/**
	 * Message property that is used to feed messages into the chair display
	 * system. Display scenes can listen to the property to receive messages as
	 * they are set. It is up to them to decide how to display them.
	 * 
	 * @return Message property for the chair.
	 */
	ReadOnlyStringProperty message();

	// ----------------------------------------------------------------------------------------
	// Service state properties.
	// ----------------------------------------------------------------------------------------

	<T extends State> T getServiceState(ServiceId serviceId);

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	public enum MasterState {
		BOOTING,
		RUNNING,
		LOCKED
	}
}
