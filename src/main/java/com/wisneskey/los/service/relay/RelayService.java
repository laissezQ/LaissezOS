package com.wisneskey.los.service.relay;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.driver.DummyRelayDriver;
import com.wisneskey.los.service.relay.driver.KridaRelayDriver;
import com.wisneskey.los.service.relay.driver.RelayDriver;
import com.wisneskey.los.state.RelayState;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

/**
 * Service for turning relays on and off.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public class RelayService extends AbstractService<RelayState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelayService.class);

	/**
	 * Internal state object for tracking the state of the relays.
	 */
	private InternalRelayState relayState;

	/**
	 * Driver to use to talk to actual relay board.
	 */
	private RelayDriver relayDriver;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private RelayService() {
		super(ServiceId.RELAY);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void terminate() {

		// Turn all relays off during service termination.
		for (RelayId relayId : RelayId.values()) {
			turnOff(relayId);
		}

		LOGGER.trace("Relay service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Turn on the relay with the specified id.
	 * 
	 * @param relayId Id of the relay to turn on.
	 */
	public void turnOn(RelayId relayId) {

		LOGGER.info("Relay on: {}", relayId);

		relayDriver.turnOn(relayId);
		relayState.updateState(relayId, true);
	}

	/**
	 * Turns on the specified relay for the given duration and then turns it off.
	 * 
	 * @param relayId  Id of the relay to turn on.
	 * @param duration Duration the relay should be on.
	 */
	public void turnOn(RelayId relayId, Duration duration) {

		// We just use a thread for this since we do not expect many relays to be
		// running simultaneously.
		new TimedRelayThread(relayId, duration).start();
	}

	/**
	 * Turn off the relay with the specified id.
	 * 
	 * @param relayId Id of the relay to turn off.
	 */
	public void turnOff(RelayId relayId) {

		LOGGER.info("Relay off: {}", relayId);

		relayDriver.turnOff(relayId);
		relayState.updateState(relayId, false);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Set the driver to use for communicating with the relays.
	 * 
	 * @param relayDriver Relay driver to use to communicate with relay hardware.
	 */
	private void setRelayDriver(RelayDriver relayDriver) {
		this.relayDriver = relayDriver;
	}

	/**
	 * Initializes the service and its relay driver and returns the initial state.
	 * 
	 * @param  profile Profile to use to configure the relay state.
	 * @return         Configured display state object.
	 */
	private RelayState initialize(Profile profile) {

		if (relayDriver == null) {
			throw new LaissezException("Relay driver not set.");
		}

		relayState = new InternalRelayState();

		// Let the relay driver initialize itself based on the profile.
		Map<RelayId, Boolean> initialState = relayDriver.initialize(profile);
		relayState.setInitialState(initialState);

		return relayState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the relay service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  runMode Run mode for the operating system.
	 * @param  profile Profile to use for configuring initial state of the relay
	 *                   service.
	 * @return         Relay service instance and its initial state object.
	 */
	public static Pair<RelayService, RelayState> createService(RunMode runMode, Profile profile) {

		RelayService service = new RelayService();

		// Set the relay driver based on the run mode.
		switch (runMode) {
		case CHAIR:
			service.setRelayDriver(new KridaRelayDriver());
			break;
		case DEV:
			service.setRelayDriver(new DummyRelayDriver());
			break;
		default:
			throw new LaissezException("Unknown run mode during relay driver selection: " + runMode);
		}

		// Give the service a chance to initialize.
		RelayState state = service.initialize(profile);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the relay state.
	 */
	private static class InternalRelayState implements RelayState {

		private Map<RelayId, BooleanProperty> stateMap;

		// ----------------------------------------------------------------------------------------
		// Relay state methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyBooleanProperty getState(RelayId relayId) {

			BooleanProperty stateProperty = stateMap.get(relayId);
			if (stateProperty == null) {
				throw new LaissezException("No state property found for relay id: " + relayId);
			}

			return stateProperty;
		}

		// ----------------------------------------------------------------------------------------
		// Private methods.
		// ----------------------------------------------------------------------------------------

		private void setInitialState(Map<RelayId, Boolean> initialStateMap) {
			stateMap = initialStateMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> new SimpleBooleanProperty(e.getValue())));
		}

		private void updateState(RelayId relayId, boolean state) {

			stateMap.get(relayId).set(state);
		}
	}

	/**
	 * Thread that energizes a relay for a given duration.
	 */
	private class TimedRelayThread extends Thread {

		private RelayId relayId;
		private Duration duration;

		private TimedRelayThread(RelayId relayId, Duration duration) {

			setDaemon(true);

			this.relayId = relayId;
			this.duration = duration;
		}

		@Override
		public void run() {

			LOGGER.info("Timed relay on for {} ms: {}", duration.toMillis(), relayId);
			RelayService.this.relayDriver.turnOn(relayId);

			try {
				Thread.sleep(duration.toMillis());
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted waiting for relay duration to elapsed.");
				Thread.currentThread().interrupt();
			} finally {

				// Turn off relay no matter what.
				LOGGER.info("Timed relay off: {}", relayId);
				RelayService.this.relayDriver.turnOff(relayId);
			}
		}
	}
}
