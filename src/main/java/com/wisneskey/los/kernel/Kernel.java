package com.wisneskey.los.kernel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
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
	private Map<ServiceId, Service<?>> serviceMap = new HashMap<>();

	/**
	 * Top level state object for tracking the current state of the chair.
	 */
	private InternalChairState chairState = new InternalChairState();

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation by outsiders.
	 */
	private Kernel() {
	}

	// ----------------------------------------------------------------------------------------
	// Public static kernel initialization methods.
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
		// anyone
		// that might be listening for master state changes.
		initialized = true;
		chairState.setMasterState(MasterState.STARTED);

		LOGGER.info("Kernel initialized.");
	}

	// ----------------------------------------------------------------------------------------
	// Public kernel operation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the indicator that designates what environment the application is
	 * being run in.
	 * 
	 * @return Enumerated type designated the environment the application is being
	 *         run in.
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
	 * @param <T>
	 *          Type of service.
	 * @param id
	 *          Id for the service to return.
	 * @return Service for the requested id.
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
	 * @param mode
	 *          Run mode of the operating
	 */
	public void setRunMode(RunMode mode) {

		requireUninitializedKernel();

		if ((runMode != null) && (runMode != mode)) {
			throw new LaissezException("Run mode can not be changed once set.");
		}

		runMode = mode;
		LOGGER.debug("Run mode set: {}", runMode.getDescription());
	}

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

	public void message(String message) {
		chairState.setMessage(message);
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
		private ObjectProperty<MasterState> masterState = new SimpleObjectProperty<MasterState>(this, "masterState",
				MasterState.BOOTING);

		/**
		 * Last boot message.
		 */
		private StringProperty bootMessage = new SimpleStringProperty();

		/**
		 * Map of service id's to their state objects.
		 */
		private Map<ServiceId, Object> stateMap = new HashMap<>();

		// ----------------------------------------------------------------------------------------
		// ChairState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<MasterState> masterState() {

			return masterState;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends State> T getServiceState(ServiceId id) {

			Class<T> serviceStateClass = (Class<T>) id.getServiceStateClass();
			return serviceStateClass.cast(stateMap.get(id));
		}

		@Override
		public ReadOnlyStringProperty message() {
			return bootMessage;
		}

		// ----------------------------------------------------------------------------------------
		// Private methods.
		// ----------------------------------------------------------------------------------------

		/**
		 * Sets the master state of the chair.
		 * 
		 * @param newState
		 *          New master state for the chair.
		 */
		private void setMasterState(MasterState newState) {
			masterState.setValue(newState);
		}

		/**
		 * Sets the state object for a service (during boot loading).
		 * 
		 * @param serviceId
		 *          If of the service to set the state for.
		 * @param state
		 *          State object for the service.
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
			bootMessage.setValue(message);
		}
	}
}
