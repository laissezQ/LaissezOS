package com.wisneskey.los.service.location.driver;

import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

/**
 * Interface defining an implementation of a GS driver interface that can work
 * with a particular GPS board.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
public interface GpsDriver {

	/**
	 * Initialize the GPS driver based on the profile.
	 * 
	 * @param profile Profile with settings for the GPS driver.
	 */
	void initialize(Profile profile);

	/**
	 * Returns the current location from the GPD driver or null if no location is
	 * known.
	 * 
	 * @return Current location or null if GPS has no fix.
	 */
	Location getCurrentLocation();
	
	/**
	 * Returns the number of satellites that were used for fast fix.
	 * 
	 * @return Number of satellites in last fix; zero if no fix.
	 */
	int getSatellitesInFix();
	
	/**
	 * Returns the last number of satellites reported in view by the GPS.
	 * 
	 * @return Number of satellites last reported as in view by GPS.
	 */
	int getSatellitesInView();
}
