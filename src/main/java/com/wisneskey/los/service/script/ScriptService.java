package com.wisneskey.los.service.script;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.script.command.ScriptCommand;
import com.wisneskey.los.state.ScriptState;
import com.wisneskey.los.util.JsonUtils;

import javafx.util.Pair;

/**
 * Service for running basic scripts to orchestrate the chair's other services.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class ScriptService extends AbstractService<ScriptState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);
	private static final Logger RUNNER_LOGGER = LoggerFactory.getLogger("ScriptRunner");

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
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Runs the script for the specified script id.
	 * 
	 * @param scriptId
	 */
	public synchronized void runScript(ScriptId scriptId) {

		LOGGER.info("Running script: {}", scriptId);
		if (scriptRunning.get()) {
			LOGGER.error("Can not run script: script already running.");
		}

		// Load the script. If we get back a null here, we can assume the script was
		// not found or failed to load.
		Script script = loadScript(scriptId);
		if (script == null) {
			LOGGER.trace("No script returned from load; not running script.");
			return;
		}

		// Set the script as running and launch a script runner thread to execute
		// it.
		scriptRunning.set(true);

		runner = new ScriptRunnerThread(script);
		runner.start();
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method called by the script runner thread when its done running its script.
	 */
	private void scriptRunEnded() {
		// Indicate there is no longer a script running.
		runner = null;
		scriptRunning.set(false);
	}

	/**
	 * Load the script for the given script id.
	 * 
	 * @param scriptId
	 *          Id of the script to load.
	 * @return Script for the given script id or null if it could not be loaded.
	 */
	private Script loadScript(ScriptId scriptId) {

		try {
			String scriptLocation = SCRIPT_RESOURCE_BASE + scriptId.getName() + ".json";
			InputStream inputStream = this.getClass().getResourceAsStream(scriptLocation);
			return JsonUtils.toObject(inputStream, Script.class);
		} catch (Exception e) {
			LOGGER.error("Failed to load {} script.", scriptId, e);
		}

		return null;
	}

	/**
	 * Creates the initial state of the service using the supplied profile for
	 * configuration.
	 * 
	 * @param profile
	 *          Profile to use for configuring initial service state.
	 * @return Configured state object for the service.
	 */
	private ScriptState createInitialState(Profile profile) {
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
	 * @param profile
	 *          Profile to use for configuring initial state of the script
	 *          service.
	 * @return Script service instance and its initial state object.
	 */
	public static Pair<ScriptService, ScriptState> createService(Profile profile) {

		ScriptService service = new ScriptService();
		ScriptState state = service.createInitialState(profile);
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

			for( ScriptCommand command : script.getCommands() ) {

				LOGGER.debug("Performing command: {}", command);
				
				try {
					command.perform();
				} catch( Exception e) {
					LOGGER.error("Error during command execution: aborting", e);
					break;
				}
			}

			// Let the service know we are done running the script.
			ScriptService.this.scriptRunEnded();

			RUNNER_LOGGER.trace("Script runner thread stopped.");
		}
	}
}
