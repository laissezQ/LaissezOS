package com.wisneskey.los.service.relay;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.driver.DummyRelayDriver;
import com.wisneskey.los.service.relay.driver.KridaRelayDriver;
import com.wisneskey.los.service.relay.driver.RelayDriver;
import com.wisneskey.los.state.RelayState;

import javafx.util.Pair;

public class RelayService extends AbstractService<RelayState> {

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
		relayState = new InternalRelayState();
		if( relayDriver == null ) {
			throw new LaissezException("Relay driver not set.");
		}
		
		// Let the relay driver initialize itself based on the profile.
		relayDriver.initialize(profile);
		
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

	}
}
