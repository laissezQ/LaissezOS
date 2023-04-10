package com.wisneskey.los.service.location.driver;

import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

public class DummyGpsDriver implements GpsDriver {

	private static final boolean DEFAULT_HAS_FIX = true;
	private static final double DEFAULT_LATITUDE = 35.6420206;
	private static final double DEFAULT_LONGITUDE = 105.9889903;
	private static final double DEFAULT_ALTITUDE = 2044.982;

	private boolean hasFix = DEFAULT_HAS_FIX;
	private double latitude = DEFAULT_LATITUDE;
	private double longitude = DEFAULT_LONGITUDE;
	private double altitude = DEFAULT_ALTITUDE;

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
		// No initialize to do for dummy driver.
	}

	@Override
	public Location getCurrentLocation() {

		return hasFix ? Location.of(latitude, longitude, altitude) : null;
	}
}
