package com.wisneskey.los.state;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * State object with the state of the map service.
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
public interface MapState extends State {

	/**
	 * Returns if the map service is running in online where mode where it is allowed
	 * to fetch tiles from a server if they are not found in the cache.
	 * 
	 * @return True if map service can fetch tiles online; false otherwise.
	 */
	ReadOnlyBooleanProperty getOnline();
}
