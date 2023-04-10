package com.wisneskey.los.service.location.driver;

import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.model.Profile;

/**
 * Interface defining an implementation of a GS driver interface that can work
 * with a particular GPS board.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface GpsDriver {

	/**
	 * Initialize the GPS driver based on the profile.
	 * 
	 * @param profile
	 *          Profile with settings for the GPS driver.
	 */
	void initialize(Profile profile);

	/**
	 * Returns the current location from the GPD driver or null if no location is
	 * known.
	 * 
	 * @return Current location or null if GPS has no fix.
	 */
	Location getCurrentLocation();
}
