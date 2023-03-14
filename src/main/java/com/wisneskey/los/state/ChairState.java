package com.wisneskey.los.state;

import com.wisneskey.los.service.ServiceId;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface ChairState extends State {

	// ----------------------------------------------------------------------------------------
	// Top level state properties.
	// ----------------------------------------------------------------------------------------

	ReadOnlyObjectProperty<MasterState> masterState();
	
	ReadOnlyStringProperty bootMessage();
	
	// ----------------------------------------------------------------------------------------
	// Service state properties.
	// ----------------------------------------------------------------------------------------

	<T extends State> T getServiceState(ServiceId serviceId);
	
	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	public enum MasterState {
		BOOTING,
		STARTED
	}
}
