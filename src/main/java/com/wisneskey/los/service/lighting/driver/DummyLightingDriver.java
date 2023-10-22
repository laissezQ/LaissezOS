package com.wisneskey.los.service.lighting.driver;

import com.wisneskey.los.service.profile.model.Profile;

/**
 * Dummy lighting driver used in development environments with no actual lighting control.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class DummyLightingDriver implements LightingDriver {

	@Override
	public void initialize(Profile profile) {
		// Nothing to do here
	}

	@Override
	public void runTest() {
		// Nothing to do here
	}
}
