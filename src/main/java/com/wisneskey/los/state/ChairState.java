package com.wisneskey.los.state;

import com.wisneskey.los.service.ServiceId;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface ChairState extends State {

	// ----------------------------------------------------------------------------------------
	// Top level state properties.
	// ----------------------------------------------------------------------------------------

	ReadOnlyObjectProperty<MasterState> masterState();
	
	// ----------------------------------------------------------------------------------------
	// Service state properties.
	// ----------------------------------------------------------------------------------------

	<T extends State> T getServiceState(ServiceId serviceId);
	
	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	public enum MasterState {
		STARTED,
		BOOTING
	}
}
