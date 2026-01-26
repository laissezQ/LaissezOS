package com.wisneskey.los.service.script;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.script.command.ScriptCommand;
import com.wisneskey.los.state.ScriptState;
import com.wisneskey.los.util.JsonUtils;

import javafx.util.Pair;

/**
 * Service for running basic scripts to orchestrate the chair's other services.
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
public class ScriptService extends AbstractService<ScriptState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);
	private static final Logger RUNNER_LOGGER = LoggerFactory.getLogger("ScriptRunner");

	/**
	 * Number of milliseconds in a second.
	 */
	private static final double MILLISECONDS_PER_SECOND = 1000.0;

	/**
	 * Base path for where audio clips are saved in the resources.
	 */
	private static final String SCRIPT_RESOURCE_BASE = "/script/";

	/**
	 * Object for the state of the script service.
	 */
	private InternalScriptState scriptState;

	/**
	 * Boolean to indicate if a script is already running.
	 */
	private AtomicBoolean scriptRunning = new AtomicBoolean(false);

	/**
	 * Thread that is running a script or null if no script running.
	 */
	private ScriptRunnerThread runner;

	/**
	 * Map of script id to its loaded script.
	 */
	private EnumMap<ScriptId, Script> scriptCache = new EnumMap<>(ScriptId.class);

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private ScriptService() {
		super(ServiceId.SCRIPT);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public ScriptState getState() {
		return scriptState;
	}

	@Override
	public void terminate() {
		if (scriptRunning.get()) {

			runner.interrupt();
			try {
				runner.join();
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted waiting for script runner thread to terminate.");
				Thread.currentThread().interrupt();
			}
		}

		LOGGER.trace("Script service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Runs the script for the specified script id.
	 * 
	 * @param scriptId
	 */
	public synchronized void runScript(ScriptId scriptId) {

		// Set the script as running and launch a script runner thread to execute
		// it.
		if (!scriptRunning.compareAndExchangeAcquire(false, true)) {

			LOGGER.info("Running script: {}", scriptId);

			// Load the script. If we get back a null here, we can assume the script
			// was
			// not found or failed to load.
			Script script = scriptCache.get(scriptId);
			if (script == null) {
				LOGGER.trace("No script returned from cache; not running script.");
				return;
			}

			runner = new ScriptRunnerThread(script);
			runner.start();
		} else {
			LOGGER.warn("Script already running; did not run new script.");
		}
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates the initial state of the service using the supplied profile for
	 * configuration.
	 * 
	 * @return Configured state object for the service.
	 */
	private ScriptState createInitialState() {

		// Load all scripts and store them in the cache.
		LOGGER.info("Loading {} scripts...", ScriptId.values().length);

		for (ScriptId scriptId : ScriptId.values()) {

			try {

				String scriptLocation = SCRIPT_RESOURCE_BASE + scriptId.getName() + ".json";
				InputStream inputStream = this.getClass().getResourceAsStream(scriptLocation);
				Script script = JsonUtils.toObject(inputStream, Script.class);
				scriptCache.put(scriptId, script);

			} catch (Exception e) {
				throw new LaissezException("Failed to load script : " + scriptId, e);
			}
		}

		scriptState = new InternalScriptState();
		return scriptState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the script service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile to use for configuring initial state of the script
	 *                   service.
	 * @return         Script service instance and its initial state object.
	 */
	public static Pair<ScriptService, ScriptState> createService() {

		ScriptService service = new ScriptService();
		ScriptState state = service.createInitialState();
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the Script service.
	 */
	private static class InternalScriptState implements ScriptState {

	}

	/**
	 * Thread for running a script in the background.
	 */
	private class ScriptRunnerThread extends Thread {

		private Script script;

		public ScriptRunnerThread(Script script) {
			this.script = script;
		}

		@Override
		public void run() {

			RUNNER_LOGGER.trace("Script runner thread started...");

			for (ScriptCommand command : script.getCommands()) {

				LOGGER.debug("Performing command: {}", command);

				try {
					command.perform();
				} catch (Exception e) {
					LOGGER.error("Error during command execution: aborting", e);
					break;
				}

				if (command.getPostCommandPause() > 0.0) {
					try {
						Thread.sleep((long) (command.getPostCommandPause() * MILLISECONDS_PER_SECOND));
					} catch (InterruptedException e) {
						LOGGER.warn("Interrupted while in pause.");
						Thread.currentThread().interrupt();
					}
				}
			}

			// Let the service know we are done running the script.
			scriptRunning.set(false);
			runner = null;

			RUNNER_LOGGER.trace("Script runner thread stopped.");
		}
	}
}
