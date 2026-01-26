package com.wisneskey.los.service.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.remote.driver.DummyRemoteDriver;
import com.wisneskey.los.service.remote.driver.GpioRemoteDriver;
import com.wisneskey.los.service.remote.driver.RemoteDriver;
import com.wisneskey.los.state.RemoteState;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

/**
 * Service for watching out for the pressing of buttons on a remote control and
 * deliver remote pressed events to the current scene controllers so they can be
 * effectively treated like other UI events.
 *
 * Copyright (C) 2026 Paul Wisneskey
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
public class RemoteService extends AbstractService<RemoteState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteService.class);

	/**
	 * Internal state object for tracking the state of the remotes.
	 */
	private InternalRemoteState remoteState;

	/**
	 * Driver to use to actual watch for remote events.
	 */
	private RemoteDriver remoteDriver;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private RemoteService() {
		super(ServiceId.REMOTE);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public RemoteState getState() {
		return remoteState;
	}

	@Override
	public void terminate() {

		if (remoteDriver != null) {
			remoteDriver.terminate();
			remoteDriver = null;
		}

		LOGGER.trace("Remote service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Sets the driver to use fore receiving the remote's signals.
	 * 
	 * @param remoteDriver Remote driver to use for receiving remote's signals.
	 */
	private void setRemoteDriver(RemoteDriver remoteDriver) {
		this.remoteDriver = remoteDriver;
	}

	/**
	 * Initializes the service ands its relay driver and returns the initial
	 * state.
	 * 
	 * @return
	 */
	private RemoteState initialize() {

		if (remoteDriver == null) {
			throw new LaissezException("Remote driver not set.");
		}

		remoteState = new InternalRemoteState();
		remoteDriver.initialize(new InternalRemoteEventHandler());

		return remoteState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the service with its initialized state.
	 * 
	 * @param  runMode Run mode of the application.
	 * @return         Service and its initialized state.
	 */
	public static Pair<RemoteService, RemoteState> createService(RunMode runMode) {

		RemoteService service = new RemoteService();

		// Set the remote driver based on the run mode.
		switch (runMode) {
		case CHAIR:
			service.setRemoteDriver(new GpioRemoteDriver());
			break;
		case DEV:
			service.setRemoteDriver(new DummyRemoteDriver());
			break;
		default:
			throw new LaissezException("Unknown run mode during remote driver selection: " + runMode);
		}

		// Give the service a chance to initialize.
		RemoteState state = service.initialize();
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the remote state.
	 */
	private static class InternalRemoteState implements RemoteState {

		/**
		 * Property for button A's pressed state;
		 */
		private SimpleBooleanProperty buttonA = new SimpleBooleanProperty(false);

		/**
		 * Property for button B's pressed state;
		 */
		private SimpleBooleanProperty buttonB = new SimpleBooleanProperty(false);

		// ----------------------------------------------------------------------------------------
		// RemoteState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyBooleanProperty buttonA() {
			return buttonA;
		}

		@Override
		public ReadOnlyBooleanProperty buttonB() {
			return buttonB;
		}

		// ----------------------------------------------------------------------------------------
		// Supporting methods.
		// ----------------------------------------------------------------------------------------

		private void updateButtonState(RemoteButtonId buttonId, boolean pressed) {

			switch (buttonId) {
			case REMOTE_BUTTON_A:
				buttonA.set(pressed);
				break;

			case REMOTE_BUTTON_B:
				buttonB.set(pressed);
				break;

			default:
				throw new LaissezException("Unhandled button in state update.");
			}
		}
	}

	/**
	 * Remote event handler that received events from the remote driver and passes
	 * them on to the service.
	 */
	private class InternalRemoteEventHandler implements RemoteEventHandler {

		@Override
		public void buttonPressed(RemoteButtonId buttonId) {

			LOGGER.debug("Button pressed: {}", buttonId);
			remoteState.updateButtonState(buttonId, true);
		}

		@Override
		public void buttonReleased(RemoteButtonId buttonId) {

			LOGGER.debug("Button released: {}", buttonId);
			remoteState.updateButtonState(buttonId, false);
		}
	}
}
