package com.wisneskey.los.service.location.driver;

import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

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
}
