package com.wisneskey.los.boot;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.service.security.SecurityService;
import com.wisneskey.los.state.ProfileState;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

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
	 * @param kernel
	 *          Kernel to be configured during boot.
	 * @param bootConfig
	 *          Configuration for bootstrap the LBOS instance.
	 */
	public void boot(BootConfiguration bootConfig) {

		RunMode runMode = bootConfig.getRunMode();
		String profileName = bootConfig.getProfileName();

		LOGGER.info("Boot loader starting: runMode={} profile={}", runMode, profileName);

		// Get what should be the uninitialized kernel.
		Kernel kernel = Kernel.kernel();

		// First make sure we have a run mode set since that may affect how services
		// set themselves up (particularly the display service).
		kernel.setRunMode(runMode);

		// Get the profile service stood up first so we can activate the profile for
		// use in configuring all the other services.
		Pair<ProfileService, ProfileState> profileServiceDetails = ProfileService.createService(profileName);
		kernel.registerService(profileServiceDetails);

		// Get the profile from the profile service's state to use for other
		// services.
		Profile profile = profileServiceDetails.getValue().activeProfile().getValue();

		// Register the other services which will use the active profile to
		// configure themselves.
		kernel.registerService(AudioService.createService(profile));
		kernel.registerService(DisplayService.createService(runMode, profile));
		kernel.registerService(RelayService.createService(runMode, profile));
		kernel.registerService(ScriptService.createService(profile));
		kernel.registerService(SecurityService.createService(profile));

		// Initialize the kernel now that its set up.
		Kernel.kernel().initialize();

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
		// start the boot script that is specified in the profile.
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).initialize(stage);
		
		Profile profile = ((ProfileState) Kernel.kernel().chairState().getServiceState(ServiceId.PROFILE)).activeProfile().get();
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(profile.getBootScript());
	}
}
