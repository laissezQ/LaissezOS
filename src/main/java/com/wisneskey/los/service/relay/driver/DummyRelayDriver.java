package com.wisneskey.los.service.relay.driver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Dummy relay driver used in development environments with no actual relay access.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class DummyRelayDriver implements RelayDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyRelayDriver.class);

	@Override
	public Map<RelayId,Boolean> initialize(Profile profile) {

		LOGGER.debug("Initialized dummy relay driver.");
		
		Map<RelayId, Boolean> stateMap = new HashMap<>();
		for( RelayId id : RelayId.values()) {
			stateMap.put(id, Boolean.FALSE);
		}
		
		return stateMap;
	}
}
