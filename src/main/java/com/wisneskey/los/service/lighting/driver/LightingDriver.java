package com.wisneskey.los.service.lighting.driver;

import com.wisneskey.los.service.profile.model.Profile;

/**
 * Interface defining an implementation of a lighting system based on a
 * particular technology.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface LightingDriver {

	/**
	 * Initialize the lighting driver based on the profile.
	 * 
	 * @param profile
	 *          Profile with settings for the lighting driver.
	 */
	void initialize(Profile profile);

	/**
	 * Shutdown the driver when terminating the service.
	 */
	void terminate();
	
	/**
	 * Initial test implementation.
	 */
	void runTest();
}
