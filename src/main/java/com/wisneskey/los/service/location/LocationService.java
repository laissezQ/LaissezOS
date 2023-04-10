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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

/**
 * Service for providing the current location via GPS.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LocationService extends AbstractService<LocationState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

	/**
	 * Interval to poll the GPS driver on.
	 */
	private static final long GPS_POLL_INTERVAL_MS = 5000;

	/**
	 * Internal state object for tracking the location information.
	 */
	private InternalLocationState locationState;

	/**
	 * GPS driver to use for communicating with GPS.
	 */
	private GpsDriver gpsDriver;

	/**
	 * Thread for polling the GPS driver.
	 */
	private DriverPoller driverPoller;

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
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void terminate() {
		
		// Stop the driver polling thread.
		driverPoller.interrupt();
		try {
			driverPoller.join();
		} catch (InterruptedException e) {
			LOGGER.warn("Interrupted exception waiting for driver poller thread to shutdown.");
		}

		LOGGER.trace("Location service terminated.");
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

		// Start the poller thread for the driver polling.
		driverPoller = new DriverPoller();
		driverPoller.start();

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
	 * Thread for polling the GPS driver for data on a fixed interval.
	 */
	private class DriverPoller extends Thread {

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private DriverPoller() {
			setName("gpsDriverPoller");
			setDaemon(true);
		}

		// ----------------------------------------------------------------------------------------
		// Thread methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public void run() {

			LOGGER.info("GPS poller thread started.");

			while (!isInterrupted()) {

				try {
					Thread.sleep(GPS_POLL_INTERVAL_MS);
				} catch (InterruptedException e) {
					LOGGER.warn("Interrupted duriog driver poller sleep.");
					break;
				}

				Location latest = gpsDriver.getCurrentLocation();

				LOGGER.debug("GPS location poll: location={}", latest);
				locationState.updateLocation(latest);
			}

			LOGGER.info("GPS poller thread shutdown.");
		}

	}

	/**
	 * Internal state object for the location state.
	 */
	private static class InternalLocationState implements LocationState {

		private BooleanProperty hasFix = new SimpleBooleanProperty(false);

		private ObjectProperty<Location> location = new SimpleObjectProperty<Location>();

		// ----------------------------------------------------------------------------------------
		// LocationState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyBooleanProperty hasGpsFix() {

			return hasFix;
		}

		@Override
		public ReadOnlyObjectProperty<Location> location() {
			return location;
		}

		// ----------------------------------------------------------------------------------------
		// Supporting methods.
		// ----------------------------------------------------------------------------------------

		private void updateLocation(Location latest) {
			if (latest == null) {
				hasFix.setValue(Boolean.FALSE);
				location.setValue(null);
			} else {
				hasFix.setValue(Boolean.TRUE);
				location.setValue(latest);
			}
		}
	}
}
