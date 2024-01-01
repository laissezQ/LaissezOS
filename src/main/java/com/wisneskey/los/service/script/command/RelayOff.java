package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;

/**
 * Script command to turn a relay off.
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
public class RelayOff extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelayOff.class);

	/**
	 * Id of the relay to turn off.
	 */
	private RelayId relayId;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public RelayId getRelayId() {
		return relayId;
	}

	public void setRelayId(RelayId relayId) {
		this.relayId = relayId;
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

		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(relayId);
	}
}