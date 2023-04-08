package com.wisneskey.los.service.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.location.driver.DummyGpsDriver;
import com.wisneskey.los.service.location.driver.GpsDriver;
import com.wisneskey.los.service.location.driver.SparkFunGpsDriver;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.LocationState;

import javafx.util.Pair;

/**
 * Service for providing the current location via GPS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LocationService extends AbstractService<LocationState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

	/**
	 * Internal state object for tracking the location information.
	 */
	private InternalLocationState locationState;
	
	/**
	 * GPS driver to use for communicating with GPS.
	 */
	private GpsDriver gpsDriver;
	
	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to request use of static service creation method.
	 */
	private LocationService() {
		super(ServiceId.LOCATION);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void setGpsDriver(GpsDriver gpsDriver) {
		this.gpsDriver = gpsDriver;
	}
	
	/**
	 * Initializes the service and its relay driver and returns the initial state.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use to configure the relay state.
	 * @return Configured display state object.
	 */
	private LocationState initialize(RunMode runMode, Profile profile) {

		if (gpsDriver == null) {
			throw new LaissezException("GPS driver not set.");
		}
		
		// Let the driver initialize.
		gpsDriver.initialize(profile);

		locationState = new InternalLocationState();
		return locationState;
	}
	
	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the relay service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use for configuring initial state of the relay service.
	 * @return Relay service instance and its initial state object.
	 */
	public static Pair<LocationService, LocationState> createService(RunMode runMode, Profile profile) {

		LocationService service = new LocationService();

		// Set the relay driver based on the run mode.
		switch (runMode) {
		case PI2B_CP:
		case PI2B_HUD:
			service.setGpsDriver(new SparkFunGpsDriver());
			break;
		case DEV:
			service.setGpsDriver(new DummyGpsDriver());
			break;
		default:
			throw new LaissezException("Unknown run mode during GPS driver selection: " + runMode);
		}

		// Give the service a chance to initialize.
		LocationState state = service.initialize(runMode, profile);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the location state.
	 */
	private static class InternalLocationState implements LocationState {

	}
}
