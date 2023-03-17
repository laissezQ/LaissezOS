package com.wisneskey.los.service.display;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.util.JsonUtils;

/**
 * Class used to track and manage the configuration to use for monitors based on
 * the run mode of the chair. Designed to be loaded from a JSON file.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class Displays {

	private static final Logger LOGGER = LoggerFactory.getLogger(Displays.class);

	/**
	 * Path to the display settings JSON in the resources.
	 */
	private static final String DISPLAY_SETTINGS_RESOURCE = "/display/displays.json";

	private DisplayConfiguration defaultConfiguration;
	private Map<RunMode, DisplayConfiguration> configurationMap = new HashMap<>();

	// ----------------------------------------------------------------------------------------
	// Property setters.
	// ----------------------------------------------------------------------------------------

	/**
	 * Sets the default display configuration.
	 * 
	 * @param defaultConfiguration
	 *          Display configuration to use as default.
	 */
	public void setDefaultConfiguration(DisplayConfiguration defaultConfiguration) {
		this.defaultConfiguration = defaultConfiguration;
	}

	/**
	 * Sets the available display configurations.
	 * 
	 * @param displayConfigs
	 *          List of available display configurations.
	 */
	public void setConfigurations(List<DisplayConfiguration> displayConfigs) {

		configurationMap.clear();
		for (DisplayConfiguration displayConfig : displayConfigs) {
			if (configurationMap.put(displayConfig.getRunMode(), displayConfig) != null) {
				throw new LaissezException("Duplicate display configuration for run mode: " + displayConfig.getRunMode());
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the display configuration to use for the specified run mode.
	 * 
	 * @param runMode
	 * @return
	 */
	public DisplayConfiguration getDisplayConfiguration(RunMode runMode) {

		DisplayConfiguration configuration = configurationMap.get(runMode);
		if (configuration == null) {

			if (defaultConfiguration == null) {
				throw new LaissezException(
						"No display configuration found for run mode and no default configuration set: " + runMode);
			} else {
				LOGGER.info("Using default display configuration for run mode: " + runMode);
				configuration = defaultConfiguration;
			}
		} else {
			LOGGER.info("Using specific display configuration for run mode: " + runMode);
		}

		return configuration;
	}

	// ----------------------------------------------------------------------------------------
	// Static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Load the display settings from their JSON configuration in the resources.
	 * 
	 * @return Displays object containing the settings for the various displays.
	 */
	public static Displays loadDisplays() {

		LOGGER.info("Loading display settings: {}", DISPLAY_SETTINGS_RESOURCE);

		try {
			InputStream input = Displays.class.getResourceAsStream(DISPLAY_SETTINGS_RESOURCE);
			return JsonUtils.toObject(input, Displays.class);
		} catch (Exception e) {
			throw new LaissezException("Failed to load display settings.", e);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Class that represents the configuration to use for a given monitor in a
	 * particular run mode.
	 */
	public static class DisplayConfiguration {

		/**
		 * Run mode the configuration is for.
		 */
		private RunMode runMode;
		
		/**
		 * X location to put the control panel to position it on the proper monitor.
		 */
		private double controlPanelX;

		/**
		 * Y location to put the control panel to position it on the proper monitor.
		 */
		private double controlPanelY;

		/**
		 * X location to put the heads up display to position it on the proper monitor.
		 */
		private double hudX;

		/**
		 * Y location to put the heads up display to position it on the proper monitor.
		 */
		private double hudY;

		// ----------------------------------------------------------------------------------------
		// Property getters/setters.
		// ----------------------------------------------------------------------------------------

		public RunMode getRunMode() {
			return runMode;
		}

		public void setRunMode(RunMode runMode) {
			this.runMode = runMode;
		}

		public double getControlPanelX() {
			return controlPanelX;
		}

		public void setControlPanelX(double controlPanelX) {
			this.controlPanelX = controlPanelX;
		}

		public double getControlPanelY() {
			return controlPanelY;
		}

		public void setControlPanelY(double controlPanelY) {
			this.controlPanelY = controlPanelY;
		}

		public double getHudX() {
			return hudX;
		}

		public void setHudX(double hudX) {
			this.hudX = hudX;
		}

		public double getHudY() {
			return hudY;
		}

		public void setHudY(double hudY) {
			this.hudY = hudY;
		}
	}
}
