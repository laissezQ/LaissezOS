package com.wisneskey.los.service.lighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.driver.DummyLightingDriver;
import com.wisneskey.los.service.lighting.driver.LightingDriver;
import com.wisneskey.los.service.lighting.driver.wled.WLEDLightingDriver;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.state.LightingState;

import javafx.util.Pair;

/**
 * Service for running lighting animations.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LightingService extends AbstractService<LightingState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightingService.class);

	/**
	 * Internal state object for tracking the state of the lighting system.
	 */
	private InternalLightingState lightingState;

	/**
	 * Driver to use to control the lighting.
	 */
	private LightingDriver lightingDriver;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private LightingService() {
		super(ServiceId.LIGHTING);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void terminate() {

		LOGGER.trace("Lighting service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public void runTest() {
		
		// Enable power to the lighting strip.
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.RELAY_7);
		
		lightingDriver.runTest();
	}
	
	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Sets the driver to use for communicating with the lights.
	 * 
	 * @param lightingDriver
	 *          Lighting driver to use to communicate with the lighting strips.
	 */
	private void setLightingDriver(LightingDriver lightingDriver) {
		this.lightingDriver = lightingDriver;
	}

	/**
	 * Initializes the service and its lighting driver and returns the initial state.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use to configure the relay state.
	 * @return Configured display state object.
	 */
	private LightingState initialize(RunMode runMode, Profile profile) {

		if (lightingDriver == null) {
			throw new LaissezException("Lighting driver not set.");
		}

		lightingState = new InternalLightingState();

		// Let the lighting driver initialize itself based on the profile.
		lightingDriver.initialize(profile);

		return lightingState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the lighting service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use for configuring initial state of the lighting
	 *          service.
	 * @return Lighting service instance and its initial state object.
	 */
	public static Pair<LightingService, LightingState> createService(RunMode runMode, Profile profile) {

		LightingService service = new LightingService();

		// Set the lighting driver based on the run mode.
		switch (runMode) {
		case CHAIR:
			service.setLightingDriver(new WLEDLightingDriver());
			break;
		case DEV:
			service.setLightingDriver(new DummyLightingDriver());
			break;
		default:
			throw new LaissezException("Unknown run mode during lighting driver selection: " + runMode);
		}

		// Give the service a chance to initialize.
		LightingState state = service.initialize(runMode, profile);

		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the lighting state.
	 */
	private static class InternalLightingState implements LightingState {

	}
}
