package com.wisneskey.los.kernel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LOSException;
import com.wisneskey.los.service.Service;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.profile.ProfileService;

/**
 * Core of the Laissez Boy Operating System that manages all services, hardware,
 * etc.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LOSKernel {

	private static final Logger LOGGER = LoggerFactory.getLogger(LOSKernel.class);

	/**
	 * Flag indicating if the kernel has been initialized.
	 */
	private static boolean initialized = false;

	/**
	 * Mode chair is running.
	 */
	private static RunMode runMode;

	/**
	 * Map of service id to their service objects.
	 */
	private static Map<ServiceId, Service> serviceMap = new HashMap<>();

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation.
	 */
	private LOSKernel() {
	}

	// ----------------------------------------------------------------------------------------
	// Public static kernel initialization methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Set the run mode for the LBOS.
	 * 
	 * @param mode
	 *          Run mode to set for the
	 */
	public static void setRunMode(RunMode mode) {

		requireUninitializedKernel();

		if ((runMode != null) && (runMode != mode)) {
			throw new LOSException("Run mode can not be changed once set.");
		}

		runMode = mode;
	}

	/**
	 * Method used by the bootstrap processing to register the expected services
	 * for the kernel.
	 * 
	 * @param serviceId
	 *          Id of the service being registered.
	 * @param service
	 *          Service object to register.
	 */
	public static void registerService(Service service) {

		requireUninitializedKernel();

		if (serviceMap.put(service.getServiceId(), service) != null) {
			throw new LOSException("Duplicate service registration: id=" + service.getServiceId());
		}

		LOGGER.debug("Registered service: id=" + service.getServiceId());
	}

	/**
	 * Method used by the bootstrap processing to initialize the kernel after it
	 * has been configured.
	 */
	public static void initialize() {

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
	 * Return the audio service registered with the kernel.
	 * 
	 * @return Audio service instance.
	 */
	public static AudioService audioService() {

		requireInitializedKernel();
		return AudioService.class.cast(serviceMap.get(ServiceId.AUDIO));
	}

	/**
	 * Return the display service registered with the kernel.
	 * 
	 * @return Display service instance.
	 */
	public static DisplayService displayService() {

		requireInitializedKernel();
		return DisplayService.class.cast(serviceMap.get(ServiceId.DISPLAY));
	}

	/**
	 * Return the profile service registered with the kernel.
	 * 
	 * @return Profile service instance.
	 */
	public static ProfileService profileService() {

		requireInitializedKernel();
		return ProfileService.class.cast(serviceMap.get(ServiceId.PROFILE));
	}
	
	/**
	 * Returns the indicator that designates what environment the application is
	 * being run in.
	 * 
	 * @return Enumerated type designated the environment the application is being
	 *         run in.
	 */
	public static RunMode getRunMode() {

		requireInitializedKernel();

		if (runMode == null) {
			throw new LOSException("Run mode not set.");
		}

		return runMode;
	}

	// ----------------------------------------------------------------------------------------
	// Supporting static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Checks to make sure the kernel has been initialized and throws a run time
	 * exception if it has not.
	 */
	private static void requireInitializedKernel() {

		if (!initialized) {
			throw new LOSException("LBOS kernel not initialized.");
		}
	}

	/**
	 * Checks to make sure the kernel has not been initialized and throws a run
	 * time exception if it has.
	 */
	private static void requireUninitializedKernel() {

		if (initialized) {
			throw new LOSException("LBOS kernel already initialized.");
		}
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Enumerated type used to indicate where the application is being run so that
	 * it can configuration itself appropriately.
	 */
	public enum RunMode {
		DEV("Off chair development where chair system interfaces are simulated."),
		PI2B("Raspberry PI2B prototyping (limited display support).");

		// ----------------------------------------------------------------------------------------
		// Variables.
		// ----------------------------------------------------------------------------------------

		private String description;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private RunMode(String description) {
			this.description = description;
		}

		// ----------------------------------------------------------------------------------------
		// Public methods.
		// ----------------------------------------------------------------------------------------

		public String getDescription() {
			return description;
		}
	}
}
