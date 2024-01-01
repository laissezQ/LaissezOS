package com.wisneskey.los.boot;

import java.awt.Toolkit;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.lighting.LightingService;
import com.wisneskey.los.service.location.LocationService;
import com.wisneskey.los.service.map.MapService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.remote.RemoteService;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.service.security.SecurityService;
import com.wisneskey.los.state.ChairState.MasterState;
import com.wisneskey.los.state.ProfileState;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * 
 * Boot loader for configuring and starting the Laissez Boy Operating System in
 * different manners (fast, theatrical, etc.) 
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
public class BootLoader extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(BootLoader.class);

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Main entry point of the boot loader that handles configuration loading,
	 * initializes the kernel and performs the configured boot sequence.
	 * 
	 * @param kernel     Kernel to be configured during boot.
	 * @param bootConfig Configuration for bootstrap the LBOS instance.
	 */
	public void boot(BootConfiguration bootConfig) {

		RunMode runMode = bootConfig.getRunMode();
		String profileName = bootConfig.getProfileName();

		LOGGER.info("Boot loader starting: runMode={} profile={}", runMode, profileName);

		// Install a shutdown hook with the JVM to catch when the JVM is interrupted
		// and we
		// may need to shutdown the kernel.
		Runtime.getRuntime().addShutdownHook(new FallbackShutdownHook());

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
		// configure themselves. Initialize the relay service first since some
		// of the other services may need to energize supporting circuits when
		// the initialize (e.g. lighting and audio).
		kernel.registerService(RelayService.createService(runMode, profile));
		kernel.registerService(LightingService.createService(runMode, profile));
		kernel.registerService(LocationService.createService(runMode, profile));
		kernel.registerService(AudioService.createService(profile));
		kernel.registerService(ScriptService.createService());
		kernel.registerService(SecurityService.createService(profile));
		kernel.registerService(MapService.createService(profile));
		kernel.registerService(RemoteService.createService(runMode));
		
		// Register display service last so all the other services are ready and
		// have initial states that can be displayed.
		kernel.registerService(DisplayService.createService(profile));

		// Initialize the kernel now that its set up.
		Kernel.kernel().initialize();

		// Finally, launch the JavaFX application as the "desktop" for LBOS.
		LOGGER.info("Launching UI...");

		// Initialize the AWT toolkit before JavaFX starts to avoid an assertion failure.
		// See: https://bugs.openjdk.org/browse/JDK-8318129?attachmentSortBy=dateTime
		Toolkit.getDefaultToolkit();
		
		try {
			Application.launch();
		} catch( Exception e ) {
			LOGGER.error("Failed to launch JavaFX application.", e);
		}
		
		LOGGER.info("UI shutdown; shutting down kernel...");
		Kernel.kernel().shutdown();

		LOGGER.info("Exiting...");
	}

	// ----------------------------------------------------------------------------------------
	// Application methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void start(Stage stage) throws IOException {

		// JavaFX is ready to display now so initialize the display manager and
		// start the boot script that is specified in the profile.
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).initialize(stage);

		// Set the chair to its booting state.
		Kernel.kernel().setMasterState(MasterState.BOOTING);
		
		// Run the boot script.
		Profile profile = ((ProfileState) Kernel.kernel().chairState().getServiceState(ServiceId.PROFILE)).activeProfile()
				.get();
		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(profile.getBootScript());
	}

	@Override
	public void stop() {

		// Shut down the kernel.
		Kernel.kernel().shutdown();

		// Finally we must use System.exit() because the Swing Event Dispatch Thread
		// is not terminated. This thread exists because of the JavaFX swing
		// component we use for the map display. I have not found a way
		// to dispose of this component that causes the AWS thread to be terminated.
		System.exit(0);

	}
	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Shutdown hook that is called when the JVM is shutting down. If the kernel
	 * is still initialized, it tells the kernel to shutdown. This occurs when the
	 * JVM was interrupted with something like a control break.
	 */
	private static class FallbackShutdownHook extends Thread {

		@Override
		public void run() {

			Kernel kernel = Kernel.kernel();

			if ((kernel != null) && kernel.isInitialized()) {
				LOGGER.warn("JVM was interrupted; invoking fallback kernel shutdown...");
				kernel.shutdown();
			}
		}
	}
}
