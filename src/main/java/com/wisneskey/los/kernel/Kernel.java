package com.wisneskey.los.kernel;

import java.util.EnumMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode.Platform;
import com.wisneskey.los.service.Service;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.state.ChairState;
import com.wisneskey.los.state.ChairState.MasterState;
import com.wisneskey.los.state.State;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 * Core of the Laissez Boy Operating System that manages all services, hardware,
 * etc.
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
public class Kernel {

	private static final Logger LOGGER = LoggerFactory.getLogger(Kernel.class);

	/**
	 * Singleton kernel instance.
	 */
	private static final Kernel kernel = new Kernel();

	/**
	 * Flag indicating if the kernel has been initialized.
	 */
	private boolean initialized = false;

	/**
	 * Mode chair is running.
	 */
	private RunMode runMode;

	/**
	 * Map of service id to their service objects.
	 */
	private Map<ServiceId, Service<?>> serviceMap = new EnumMap<>(ServiceId.class);

	/**
	 * Top level state object for tracking the current state of the chair.
	 */
	private InternalChairState chairState = new InternalChairState();

	/**
	 * Context for PI4J library (if run mode means running on a Raspberry Pi).
	 */
	private Context pi4jContext;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation by outsiders.
	 */
	private Kernel() {
	}

	// ----------------------------------------------------------------------------------------
	// Public static kernel life cycle methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method used by the bootstrap processing to initialize the kernel after it
	 * has been configured.
	 */
	public void initialize() {

		LOGGER.info("Initializing kernel...");

		requireUninitializedKernel();

		// Verify that all expected services have been registered.
		for (ServiceId serviceId : ServiceId.values()) {

			if (!serviceMap.containsKey(serviceId)) {
				throw new LaissezException("Required service not registered: id=" + serviceId);
			}
		}

		// Set the initialized flag and update the master state of the chair for
		// anyone that might be listening for master state changes. We start with
		// the assumption that Q has started the chair so it will be fully enabled.
		initialized = true;
		chairState.setMasterState(MasterState.RUNNING);

		LOGGER.info("Kernel initialized.");
	}

	/**
	 * Method invoke to terminate the kernel and its services.
	 */
	public void shutdown() {

		if (!initialized) {
			return;
		}

		LOGGER.info("Shutting down kernel...");

		// Shut down the services in particular order because of dependencies based
		// on phases.
		for (ShutdownPhase phase : ShutdownPhase.values()) {

			for (ServiceId serviceId : ServiceId.values()) {

				if (serviceId.getShutdownPhase() == phase) {

					LOGGER.info("Terminating service {} in phase {}...", serviceId, phase);
					Service<?> service = getService(serviceId);
					service.terminate();
				}
			}
		}

		initialized = false;
		LOGGER.info("Kernel shutdown.");

		// Shut down the log manager to ensure all buffered messages get flushed for
		// the JVM exits.
		LogManager.shutdown();
	}

	// ----------------------------------------------------------------------------------------
	// Public kernel operation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns true if the kernel is initialized and false otherwise.
	 * 
	 * @return True iff the kernel is initialized.
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Returns the indicator that designates what environment the application is
	 * being run in.
	 * 
	 * @return Enumerated type designating the environment the application is
	 *         being run in.
	 */
	public RunMode getRunMode() {

		if (runMode == null) {
			throw new LaissezException("Run mode not set.");
		}

		return runMode;
	}

	/**
	 * Returns the service with the given id.
	 * 
	 * @param  <T> Type of service.
	 * @param  id  Id for the service to return.
	 * @return     Service for the requested id.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service<?>> T getService(ServiceId id) {

		Class<T> serviceClass = (Class<T>) id.getServiceClass();
		return serviceClass.cast(serviceMap.get(id));
	}

	/**
	 * Return the top level state object for the chair. The state object is ready
	 * only and can only be manipulated by the kernel and services (for their
	 * specific state sub-objects).
	 * 
	 * @return Top level state object for the chair state.
	 */
	public ChairState chairState() {
		requireInitializedKernel();
		return chairState;
	}

	// ----------------------------------------------------------------------------------------
	// Kernel initialization methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Used by boot loader to set the run mode of the operating system.
	 * 
	 * @param mode Run mode of the operating
	 */
	public void setRunMode(RunMode mode) {

		requireUninitializedKernel();

		if ((runMode != null) && (runMode != mode)) {
			throw new LaissezException("Run mode can not be changed once set.");
		}

		runMode = mode;
		LOGGER.debug("Run mode set: {}", runMode.getDescription());

		// If the run mode means we are actually on a Raspberry PI, then we need to
		// create the pi4jContext.
		if (mode.getPlatform() == Platform.RASPBERRY_PI) {
			LOGGER.info("Initializing PI4J context...");
			pi4jContext = Pi4J.newAutoContext();
		} else {
			LOGGER.info("PI4J context not needed for this run mode.");
		}
	}

	/**
	 * Registers a service with the kernel.
	 * 
	 * @param <S>            Class service uses for its state.
	 * @param <T>            Type of service.
	 * @param serviceDetails Pair consisting of service and its state to register
	 *                         with the kernel.
	 */
	public <S extends Service<T>, T extends State> void registerService(Pair<S, T> serviceDetails) {

		Service<T> service = serviceDetails.getKey();
		T state = serviceDetails.getValue();

		if (serviceMap.put(service.getServiceId(), service) != null) {
			throw new LaissezException("Duplicate service registration: " + service.getServiceId());
		}

		chairState.setServiceState(service.getServiceId(), state);
		LOGGER.info("Registered service: {}", service.getServiceId());
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the Pi4J context to be used by any drivers necessary to actually run
	 * on a Raspberry Pi.
	 * 
	 * @return Pi4J context for the Pi4J library.
	 */
	public Context getPi4jContext() {
		if (pi4jContext == null) {
			throw new LaissezException("Request for Pi4jContext but context not initialized.");
		}

		return pi4jContext;
	}

	/**
	 * Write a message to the kernel for reporting on chair operations. The
	 * message property is observed by the UI controller's to report on status in
	 * the UI. The message may be null.
	 * 
	 * @param message Message to report for latest status (ignored if null).
	 */
	public void message(String message) {
		if (message != null) {
			chairState.setMessage(message);
		}
	}

	/**
	 * Sets the master state of the chair.
	 * 
	 * @param state New master start for the chair.
	 */
	public void setMasterState(MasterState state) {
		LOGGER.debug("Changing chair state to {}.", state);
		chairState.setMasterState(state);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Checks to make sure the kernel has been initialized and throws a run time
	 * exception if it has not.
	 */
	private void requireInitializedKernel() {

		if (!initialized) {
			throw new LaissezException("LBOS kernel not initialized.");
		}
	}

	/**
	 * Checks to make sure the kernel has not been initialized and throws a run
	 * time exception if it has.
	 */
	private void requireUninitializedKernel() {

		if (initialized) {
			throw new LaissezException("LBOS kernel already initialized.");
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the singleton kernel instance.
	 * 
	 * @return
	 */
	public static Kernel kernel() {
		return kernel;
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Class implementing the chair's overall state tracking object.
	 */
	private static class InternalChairState implements ChairState {

		/**
		 * Current master state of the chair.
		 */
		private ObjectProperty<MasterState> masterState = new SimpleObjectProperty<>(this, "masterState",
				MasterState.BOOTING);

		/**
		 * Last message.
		 */
		private StringProperty message = new SimpleStringProperty();

		/**
		 * State of bar (assume it starts lowered but boot script should try to
		 * lower it just in case its not really lowered.
		 */
		private ObjectProperty<BarState> barState = new SimpleObjectProperty<>(this, "barState", BarState.LOWERED);

		/**
		 * Map of service id's to their state objects.
		 */
		private Map<ServiceId, Object> stateMap = new EnumMap<>(ServiceId.class);

		// ----------------------------------------------------------------------------------------
		// ChairState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<MasterState> masterState() {
			return masterState;
		}

		@Override
		public ObjectProperty<BarState> barState() {
			return barState;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends State> T getServiceState(ServiceId id) {

			Class<T> serviceStateClass = (Class<T>) id.getServiceStateClass();
			return serviceStateClass.cast(stateMap.get(id));
		}

		@Override
		public ReadOnlyStringProperty message() {
			return message;
		}

		// ----------------------------------------------------------------------------------------
		// Private methods.
		// ----------------------------------------------------------------------------------------

		/**
		 * Sets the master state of the chair.
		 * 
		 * @param newState New master state for the chair.
		 */
		private void setMasterState(MasterState newState) {
			masterState.setValue(newState);
		}

		/**
		 * Sets the state object for a service (during boot loading).
		 * 
		 * @param serviceId If of the service to set the state for.
		 * @param state     State object for the service.
		 */
		private void setServiceState(ServiceId serviceId, Object state) {
			stateMap.put(serviceId, state);
		}

		/**
		 * Sets the current message.
		 * 
		 * @param message Message to set or null to clear message.
		 */
		private void setMessage(String message) {
			this.message.setValue(message);
		}
	}
}
