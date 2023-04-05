package com.wisneskey.los.service.relay;

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
	 * Private constructor to request use of static service creation method.
	 */
	public RelayService() {
		super(ServiceId.RELAY);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public void turnOn(RelayId relayId) {

		LOGGER.info("Relay on: " + relayId);

		relayDriver.turnOn(relayId);
		relayState.updateState(relayId, true);
		
	}

	public void turnOff(RelayId relayId) {

		LOGGER.info("Relay off: " + relayId);

		relayDriver.turnOff(relayId);
		relayState.updateState(relayId, false);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void setRelayDriver(RelayDriver relayDriver) {
		this.relayDriver = relayDriver;
	}

	/**
	 * Initializes the service and its relay driver and returns the initial state.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use to configure the relay state.
	 * @return Configured display state object.
	 */
	private RelayState initialize(RunMode runMode, Profile profile) {

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
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use for configuring initial state of the relay service.
	 * @return Relay service instance and its initial state object.
	 */
	public static Pair<RelayService, RelayState> createService(RunMode runMode, Profile profile) {

		RelayService service = new RelayService();

		// Set the relay driver based on the run mode.
		switch (runMode) {
		case PI2B_CP:
		case PI2B_HUD:
			service.setRelayDriver(new KridaRelayDriver());
			break;
		case DEV:
			service.setRelayDriver(new DummyRelayDriver());
			break;
		default:
			throw new LaissezException("Unknown run mode during relay driver selection: " + runMode);
		}

		// Give the service a chance to
		RelayState state = service.initialize(runMode, profile);
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
			stateMap = initialStateMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (e) -> new SimpleBooleanProperty(e.getValue())));
		}
		
		private void updateState(RelayId relayId, boolean state) {
			
			stateMap.get(relayId).set(state);
		}
	}
}
