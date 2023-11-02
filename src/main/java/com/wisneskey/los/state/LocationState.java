package com.wisneskey.los.state;

import com.wisneskey.los.service.location.Location;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * State object for the location service.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
	
	/**
	 * Number of satellites seen by the GPS.
	 * 
	 * @return Last measured number of satellites seen.
	 */
	ReadOnlyIntegerProperty satellitesInView();
	
	/**
	 * Number of satellites that were used for the last GPS fix.
	 * 
	 * @return Number of satellites used for the last GPS fix.
	 */
	ReadOnlyIntegerProperty satellitesInFix();
}
