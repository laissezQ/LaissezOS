package com.wisneskey.los.state;

import com.wisneskey.los.service.location.Location;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;

/**
 * State object with the state of the map service.
 *
 * Copyright (C) 2025 Paul Wisneskey
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
public interface MapState extends State {

	/**
	 * Property that controls if the map map service is allowed to download map
	 * tiles from Open Street Maps online. Can be set by anyone.
	 * 
	 * @return True if map service can fetch tiles online; false otherwise.
	 */
	BooleanProperty getOnline();

	/**
	 * Property that indicates if the map should be tracking the chair location.
	 * 
	 * @return True if the map is tracking the actual reported GPS location for
	 *         the chair.
	 */
	BooleanProperty getTracking();

	/**
	 * Property containing the coordinate of the current center of the map being
	 * displayed.
	 * 
	 * @return Location of the map's display center.
	 */
	ObjectProperty<Location> getMapCenter();
}
