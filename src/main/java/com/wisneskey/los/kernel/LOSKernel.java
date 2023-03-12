package com.wisneskey.los.kernel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.boot.KernelConfigurator;
import com.wisneskey.los.error.LOSException;
import com.wisneskey.los.service.Service;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.ChairState;
import com.wisneskey.los.state.ProfileState;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Core of the Laissez Boy Operating System that manages all services, hardware,
 * etc.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LOSKernel implements KernelConfigurator {

	private static final Logger LOGGER = LoggerFactory.getLogger(LOSKernel.class);

	/**
	 * Singleton kernel instance.
	 */
	private static final LOSKernel kernel = new LOSKernel();

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
	private LOSKernel() {
	}

	// ----------------------------------------------------------------------------------------
	// Public static kernel initialization methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the configurator that can be used to set up internal kernel state
	 * and register services before it is initialized.
	 * 
	 * @return Configurator to use to configure the kernel.
	 */
	public KernelConfigurator getConfigurator() {
		requireUninitializedKernel();
		return this;
	}

	/**
	 * Method used by the bootstrap processing to initialize the kernel after it
	 * has been configured.
	 */
	public void initialize() {

		requireUninitializedKernel();

		// Verify that all expected services have been registered.
		for (ServiceId serviceId : ServiceId.values()) {

			if (!serviceMap.containsKey(serviceId)) {
				throw new LOSException("Required service not registered: id=" + serviceId);
			}
		}

		initialized = true;
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
			throw new LOSException("Run mode not set.");
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
	public ChairState getChairState() {
		requireInitializedKernel();
		return chairState;
	}

	// ----------------------------------------------------------------------------------------
	// KernelConfigurator methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Set the run mode for the LBOS.
	 * 
	 * @param mode
	 *          Run mode to set for the
	 */
	public void setRunMode(RunMode mode) {

		requireUninitializedKernel();

		if ((runMode != null) && (runMode != mode)) {
			throw new LOSException("Run mode can not be changed once set.");
		}

		runMode = mode;
	}

	public <T> void register(Service<T> service) {

		serviceMap.put(service.getServiceId(), service);
		ProfileState profileState = chairState.getServiceState(ServiceId.PROFILE);
		Profile profile = profileState == null ? null : profileState.activeProfile().get();
		chairState.setServiceState(service.getServiceId(), service.getInitialState(profile));
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
			throw new LOSException("LBOS kernel not initialized.");
		}
	}

	/**
	 * Checks to make sure the kernel has not been initialized and throws a run
	 * time exception if it has.
	 */
	private void requireUninitializedKernel() {

		if (initialized) {
			throw new LOSException("LBOS kernel already initialized.");
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the singleton kernel instance.
	 * @return
	 */
	public static LOSKernel kernel() {
		return kernel;
	}
	
	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private static class InternalChairState implements ChairState {

		private ObjectProperty<MasterState> masterState = new SimpleObjectProperty<MasterState>(this, "masterState",
				MasterState.STARTED);

		private Map<ServiceId, Object> stateMap = new HashMap<>();

		// ----------------------------------------------------------------------------------------
		// ChairState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<MasterState> masterState() {

			return masterState;
		}

		@SuppressWarnings("unchecked")
		public <T> T getServiceState(ServiceId id) {

			Class<T> serviceStateClass = (Class<T>) id.getServiceStateClass();
			return serviceStateClass.cast(stateMap.get(id));
		}

		// ----------------------------------------------------------------------------------------
		// Public methods.
		// ----------------------------------------------------------------------------------------

		public void setMasterState(MasterState newState) {
			masterState.setValue(newState);
		}

		public void setServiceState(ServiceId serviceId, Object state) {
			stateMap.put(serviceId, state);
		}
	}
}
