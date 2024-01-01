package com.wisneskey.los.service.remote.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalInputProvider;
import com.pi4j.io.gpio.digital.PullResistance;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.remote.RemoteEventHandler;

/**
 * Remote driver that monitors GPIO pins of the Raspberry which are triggered
 * when an external remote board receives a remote event. The button
 * implementation is based on the SimpleButton class from the Pi4j example
 * components.
 *
 * Copyright (C) 2024 Paul Wisneskey
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
public class GpioRemoteDriver implements RemoteDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(GpioRemoteDriver.class);

	/**
	 * GPIO pin number for button A.
	 */
	private static final int BUTTON_A_PIN_NUMBER = 22;

	/**
	 * GPIO pin number for button B.
	 */
	private static final int BUTTON_B_PIN_NUMBER = 23;

	// ----------------------------------------------------------------------------------------
	// RemoteDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(RemoteEventHandler eventHandler) {

		LOGGER.trace("GPIO remote button monitor driver initializing...");
		Context pi4jContext = Kernel.kernel().getPi4jContext();

		new RemoteButtonMonitor(RemoteButtonId.REMOTE_BUTTON_A, pi4jContext, BUTTON_A_PIN_NUMBER, eventHandler);
		new RemoteButtonMonitor(RemoteButtonId.REMOTE_BUTTON_B, pi4jContext, BUTTON_B_PIN_NUMBER, eventHandler);
	}

	@Override
	public void terminate() {
		// Nothing to do here.
	}

	/**
	 * Monitor for a single remote button that is triggered by a GPIO pin on the
	 * Raspberry Pi.
	 */
	private static class RemoteButtonMonitor {

		/**
		 * Digital input object for the pin on the Raspberry Pi that will be
		 * triggered by the external remote hardware.
		 */
		private DigitalInput digitalInput;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		/**
		 * Constructor for a monitor for a single button on a designated GPIO pin.
		 * 
		 * @param buttonId     Id of the remote button the monitor is for.
		 * @param pi4jContext  Pi4J context of the Raspberry Pi we are running on.
		 * @param pinNumber    GPIO pin number to monitor.
		 * @param eventHandler Event handler to use for reporting button state
		 *                       changes to.
		 */
		private RemoteButtonMonitor(RemoteButtonId buttonId, Context pi4jContext, int pinNumber,
				RemoteEventHandler eventHandler) {

			DigitalInputConfig config = DigitalInput //
					.newConfigBuilder(pi4jContext) //
					.id("RBUT" + pinNumber) //
					.address(pinNumber) //
					.debounce(DigitalInput.DEFAULT_DEBOUNCE) //
					.pull(PullResistance.PULL_DOWN) //
					.build();

			DigitalInputProvider provider = pi4jContext.provider("pigpio-digital-input");
			
			digitalInput = provider.create(config);
			digitalInput.addListener(event -> {

				LOGGER.trace("Remote button {} switched state: {}", buttonId, digitalInput.state());

				switch (digitalInput.state()) {

				case HIGH:
					eventHandler.buttonPressed(buttonId);
					break;

				case LOW:
					eventHandler.buttonReleased(buttonId);
					break;

				case UNKNOWN:
					LOGGER.warn("Remote button {} is in UNKNOWN state.", buttonId);
				}
			});
		}
	}
}
