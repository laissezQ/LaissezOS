package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for script commands.
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
public abstract class AbstractScriptCommand implements ScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScriptCommand.class);

	/**
	 * Number of milliseconds in a second.
	 */
	protected static final double MILLISECONDS_PER_SECOND = 1000.0;

	/**
	 * Number of seconds to pause after executing a command.
	 */
	private double postCommandPause = 0.0;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	@Override
	public double getPostCommandPause() {
		return postCommandPause;
	}

	public void setPostCommandPause(double postCommandPause) {
		this.postCommandPause = postCommandPause;
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Lets script commands sleep for any fractional number of seconds.
	 * 
	 * @param seconds Number of seconds to sleep.
	 */
	protected void sleepForSeconds(double seconds) {
		try {
			Thread.sleep((long) (seconds * MILLISECONDS_PER_SECOND));
		} catch (InterruptedException e) {
			LOGGER.warn("Interrupted while sleeping.");
			Thread.currentThread().interrupt();
		}
	}
}
