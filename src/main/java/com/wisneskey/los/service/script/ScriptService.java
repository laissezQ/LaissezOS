package com.wisneskey.los.service.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.ScriptState;

import javafx.util.Pair;

/**
 * Service for running basic scripts to orchestrate the chair's other services.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class ScriptService extends AbstractService<ScriptState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);

	/**
	 * Base path for where audio clips are saved in the resources.
	 */
	private static final String SCRIPT_RESOURCE_BASE = "/script/";

	/**
	 * Object for the state of the script service.
	 */
	private InternalScriptState scriptState;

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
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

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
}
