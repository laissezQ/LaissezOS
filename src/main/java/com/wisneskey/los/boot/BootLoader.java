package com.wisneskey.los.boot;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffect;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.profile.ProfileService;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * Boot loader for configuring and starting the Laissez Boy Operating System in
 * different manners (fast, theatrical, etc.)
 * 
 * @author paul.wisneskey@gmail.com
 */
public class BootLoader extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(BootLoader.class);

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Main entry point of the boot loader that handles configuration loading,
	 * initializes the kernel and performs the configured boot sequence.
	 * 
	 * @param kernelConfigurator
	 *          Handle to use to configure the kernel and register its services.
	 * @param bootConfig
	 *          Configuration for bootstrap the LBOS instance.
	 */
	public void boot(KernelConfigurator kernelConfigurator, BootConfiguration bootConfig) {

		LOGGER.info("Boot loader starting: runMode={} profile={}", bootConfig.getRunMode(), bootConfig.getProfileName());

		// First make sure we have a run mode set since that may affect how services
		// set themselves up (particularly the display service).
		kernelConfigurator.setRunMode(bootConfig.getRunMode());

		// Get the profile service stood up first so we can activate the profile for
		// use
		// in configuring all the other services.
		ProfileService profileService = new ProfileService();
		profileService.activateProfile(bootConfig.getProfileName());
		kernelConfigurator.register(profileService);

		// Register the other services whcih will use the active profile to configure themselves.
		kernelConfigurator.register(new AudioService());
		kernelConfigurator.register(new DisplayService());

		// Initialize the kernel now that its set up.
		LOSKernel.kernel().initialize();

		// Finally, launch the JavaFX application as the "desktop" for LBOS.
		LOGGER.info("Launching UI...");

		Application.launch();

		LOGGER.info("UI shutdown; exiting...");
	}

	// ----------------------------------------------------------------------------------------
	// Application methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void start(Stage stage) throws IOException {

		// JavaFX is ready to display now so initialize the display manager and
		// start the dramatic boot sequence.
		((DisplayService) LOSKernel.kernel().getService(ServiceId.DISPLAY)).initialize(stage);

		// Play a welcome clip as a test.
		((AudioService) LOSKernel.kernel().getService(ServiceId.AUDIO)).playEffect(SoundEffect.BOOT_COMPLETE);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

}
