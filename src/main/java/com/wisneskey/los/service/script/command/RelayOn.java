package com.wisneskey.los.service.script.command;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;

/**
 * Script command to turn a relay on, possibly for a given duration.
 *
 * Copyright (C) 2025 Paul Wisneskey
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
public class RelayOn extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelayOn.class);

	/**
	 * Default value for flag indicating if command should wait for a timed relay
	 * to complete.
	 */
	private static final boolean DEFAULT_WAIT_FOR_COMPLETION = false;

	/**
	 * Id of the relay to turn on.
	 */
	private RelayId relayId;

	/**
	 * Optional number of seconds relay should be on for.
	 */
	private Double forSeconds;

	/**
	 * Flag indicating if command should wait for a timed relay to complete.
	 */
	private boolean waitForCompletion = DEFAULT_WAIT_FOR_COMPLETION;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public RelayId getRelayId() {
		return relayId;
	}

	public void setRelayId(RelayId relayId) {
		this.relayId = relayId;
	}

	public Double getForSeconds() {
		return forSeconds;
	}

	public void setForSeconds(Double forSeconds) {
		this.forSeconds = forSeconds;
	}
	
	public boolean getWaitForCompletion() {
		return waitForCompletion;
	}
	
	public void setWaitForCompletion(boolean waitForCompletion) {
		this.waitForCompletion = waitForCompletion;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		if (relayId == null) {
			LOGGER.warn("No relay id specified; skipping command.");
			return;
		}

		Duration duration = forSeconds == null ? null : Duration.ofMillis((long) (forSeconds * 1000.0D));
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(relayId, duration, waitForCompletion);
	}
}