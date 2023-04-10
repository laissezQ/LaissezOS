package com.wisneskey.los.service.location;

import java.util.Objects;

/**
 * Model object for containing a location from the GPS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Location {

	private double latitude;
	private double longitude;
	private double altitude;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public Location(double latitude, double longitude, double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

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
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public int hashCode() {
		return Objects.hash(altitude, latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Double.doubleToLongBits(altitude) == Double.doubleToLongBits(other.altitude)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
	}

	public String toString() {
		return "Location[latitude=" + getLatitude() + ", longitude=" + getLongitude() + ", altitude=" + getAltitude() + "]";
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns a location object for the given coordinates.
	 * 
	 * @param latitude
	 *          Latitude of location.
	 * @param longitude
	 *          Longitude of location.
	 * @param altitude
	 *          Altitude of location.
	 * @return Location object with the specified coordinates.
	 */
	public static Location of(double latitude, double longitude, double altitude) {
		return new Location(latitude, longitude, altitude);
	}
}
