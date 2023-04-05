package com.wisneskey.los.service.relay.driver;

import java.util.Map;

import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;

/**
 * Interface defining an implementation of relay driver interface that can work
 * with a particular relay board.
 * 
 * @author paul.wisneskey@gmail.com
 *
 */
public interface RelayDriver {

	/**
	 * Initialize the relay driver based on the profile.
	 * 
	 * @param profile
	 *          Profile with settings for the relay driver.
	 */
	Map<RelayId, Boolean> initialize(Profile profile);

	/**
	 * Turn on the specified relay.
	 * 
	 * @param relayId
	 *          Id of the relay to turn on.
	 */
	void turnOn(RelayId relayId);
	
	/**
	 * Turn off the specified relay.
	 * 
	 * @param relayid Id of the relay to turn off.
	 */
	void turnOff(RelayId relayid);
}
