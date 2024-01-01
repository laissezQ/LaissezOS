package com.wisneskey.los.service.lighting.driver.wled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.driver.LightingDriver;
import com.wisneskey.los.service.lighting.driver.wled.client.WledClient;
import com.wisneskey.los.service.lighting.driver.wled.client.model.Summary;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;

/**
 * Driver for controlling lights with the WLED application running on a host.
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
public class WledLightingDriver implements LightingDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(WledLightingDriver.class);

	/**
	 * Flag indicating if we found the WLED controller and could communicate with
	 * it.
	 */
	private boolean online = false;

	/**
	 * Client to use for communicating with the WLED controller via its JSON API.
	 */
	private WledClient controllerClient;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void runTest() {

		// Don't even try if the connection to the controller failed during
		// initialization.
		if (!online) {
			return;
		}

	}

	// ----------------------------------------------------------------------------------------
	// LightingDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(Profile profile) {

		if (profile.getWledHostAddress() == null) {
			throw new LaissezException("No WLED controller host address set.");
		}

		LOGGER.info("Initializing WLED lighting driver: host={}", profile.getWledHostAddress());

		controllerClient = WledClient.create(profile.getWledHostAddress());

		Summary summary;
		try {
			// Verify the connection by requesting the info from the controller.
			summary = controllerClient.getSummary();
			online = true;
			LOGGER.info("Connected to WLED lighting driver: name={}", summary.getInfo().getName());
		} catch (Exception e) {
			LOGGER.error("Failed to initialize lighting driver: {}", e.getMessage());
			online = false;
		}

		if (!online) {
			return;
		}

		// TODO: Validate the information we obtained from the controller to make
		// sure its configuration matches what we expect in terms of lighting
		// segments, etc.

		// If we were able to talk to the controller, go ahead and energize
		// the relays to enable power to the LED lighting on both sides of the
		// chair.
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.LIGHTING_A);
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.LIGHTING_B);
	}

	@Override
	public void terminate() {

		// Turn off the power to the LED strips.
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(RelayId.LIGHTING_A);
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(RelayId.LIGHTING_B);
	}
}
