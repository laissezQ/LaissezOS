package com.wisneskey.los.state;

import com.wisneskey.los.service.relay.RelayId;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * State object for the state of the relays.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface RelayState extends State {

	/**
	 * Returns the state property for the relay with the given if.
	 * 
	 * @param relayId
	 *          Id of the relay to return the state property for.
	 * 
	 * @return Boolean state property for the specified relay.
	 */
	ReadOnlyBooleanProperty getState(RelayId relayId);
}
