package com.wisneskey.los.state;

import com.wisneskey.los.service.location.Location;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * State object for the location service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface LocationState extends State {

	/**
	 * Boolean property indicating if the GPS unit has a fix.
	 * 
	 * @return True if the GPS has a fix; false otherwise.
	 */
	ReadOnlyBooleanProperty hasGpsFix();

	/**
	 * Property with the current location.
	 * 
	 * @return Current location if there is a GPS fix.
	 */
	ReadOnlyObjectProperty<Location> location();
}
