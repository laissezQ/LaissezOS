package com.wisneskey.los.kernel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LBOSException;
import com.wisneskey.los.service.Service;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;

/**
 * Core of the Laissez Boy Operating System that manages all services, hardware,
 * etc.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LBOSKernel {

	private static final Logger LOGGER = LoggerFactory.getLogger(LBOSKernel.class);
	
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
	private LBOSKernel() {
	}

	// ----------------------------------------------------------------------------------------
	// Public static kernel initialization methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Set the run mode for the LBOS.
	 * 
	 * @param mode Run mode to set for the
	 */
	public static void setRunMode(RunMode mode) {

		requireUninitializedKernel();

		if(( runMode != null) && (runMode != mode)) {
			throw new LBOSException("Run mode can not be changed once set.");
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
			throw new LBOSException("Duplicate service registration: id=" + service.getServiceId());
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
				throw new LBOSException("Required service not registered: id=" + serviceId);
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
	 * Returns the indicator that designates what environment the application is
	 * being run in.
	 * 
	 * @return Enumerated type designated the environment the application is being
	 *         run in.
	 */
	public RunMode getRunMode() {

		requireInitializedKernel();

		if (runMode == null) {
			throw new LBOSException("Run mode not set.");
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
			throw new LBOSException("LBOS kernel not initialized.");
		}
	}

	/**
	 * Checks to make sure the kernel has not been initialized and throws a run
	 * time exception if it has.
	 */
	private static void requireUninitializedKernel() {

		if (initialized) {
			throw new LBOSException("LBOS kernel already initialized.");
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
		CHAIR,
		DEV;
	}
}
