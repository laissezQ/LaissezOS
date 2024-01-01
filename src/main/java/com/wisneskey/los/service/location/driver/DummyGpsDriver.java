package com.wisneskey.los.service.location.driver;

import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

/**
 * Dummy driver for simulating GPS output.
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
public class DummyGpsDriver implements GpsDriver {

	private static final boolean DEFAULT_HAS_FIX = true;

	private boolean hasFix = DEFAULT_HAS_FIX;
	private double latitude;
	private double longitude;
	private double altitude;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public boolean getHasFix() {
		return hasFix;
	}

	public void setHasFix(boolean hasFix) {
		this.hasFix = hasFix;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	// ----------------------------------------------------------------------------------------
	// GpsDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(Profile profile) {
		latitude = profile.getInitialLatitude();
		longitude = profile.getInitialLongitude();
		altitude = profile.getInitialAltitude();
	}

	@Override
	public Location getCurrentLocation() {

		return hasFix ? Location.of(latitude, longitude, altitude) : null;
	}

	@Override
	public int getSatellitesInFix() {
		return 0;
	}

	@Override
	public int getSatellitesInView() {
		return 0;
	}
}
