package com.wisneskey.los.service.display;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.Displays.DisplayConfiguration;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.DisplayState;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main point for working with the Laissez Boy displays.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class DisplayService extends AbstractService<DisplayState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisplayService.class);

	/**
	 * Base path for where stylesheets are saved in the resources.
	 */
	private static final String STYLESHEET_RESOURCE_BASE = "/display/stylesheet/";

	/**
	 * Base path for where scene definitions are saved in the resources.
	 */
	private static final String SCENE_RESOURCE_BASE = "/display/scene/";

	/**
	 * Width of the heads up display.
	 */
	private static final double HUD_WIDTH = 640.0;

	/**
	 * Height of the heads up display.
	 */
	private static final double HUD_HEIGHT = 480;

	/**
	 * Width of the control panel display.
	 */
	private static final double CONTROL_PANEL_WIDTH = 400;

	/**
	 * Height of the control panel display.
	 */
	private static final double CONTROL_PANEL_HEIGHT = 1280;

	/**
	 * Internal service state object.
	 */
	private InternalDisplayState displayState;

	/**
	 * Flag indicating if the display manager is initialized.
	 */
	private boolean initialized = false;

	/**
	 * Stage for the control panel.
	 */
	private Stage cpStage;

	/**
	 * Stage for the heads up display.
	 */
	private Stage hudStage;

	/**
	 * Map of scene id's to their loaded scenes.
	 */
	private Map<SceneId, Scene> sceneMap = new HashMap<>();

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to request use of static service creation method.
	 */
	public DisplayService() {
		super(ServiceId.DISPLAY);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method called by SpringFX once the application is launched by the boot
	 * loader.
	 * 
	 * @param stage
	 *          Main stage for the SpringFX application.
	 * @throws IOException
	 */
	public void initialize(Stage stage) throws IOException {

		if (initialized) {
			throw new RuntimeException("Display manager already initialized.");
		}

		// Determine the run mode since that governs how and what is shown.
		RunMode runMode = Kernel.kernel().getRunMode();

		// Apply the style set in our state
		applyStyle(displayState.currentStyle().getValue());

		// Get the display settings for our run mode.
		Displays displays = Displays.loadDisplays();
		DisplayConfiguration displayConfig = displays.getDisplayConfiguration(runMode);

		// Load all of the scenes so that we can start displaying them once the
		// stages
		// are set up.
		loadScenes();

		// Create the stages for both screens but the run mode will dictate which
		// ones actually get shown and where they are placed.

		// Control panel stage:
		cpStage = stage;
		cpStage.setX(displayConfig.getControlPanelX());
		cpStage.setY(displayConfig.getControlPanelY());

		// Heads up display stage:
		hudStage = new Stage();
		hudStage.setX(displayConfig.getHudX());
		hudStage.setY(displayConfig.getHudY());

		// Select initial scenes
		showScene(SceneId.CP_SPLASH_SCREEN);
		showScene(SceneId.HUD_SPLASH_SCREEN);

		// Show each stage depending on run mode and screen availability.
		showControlPanel(runMode);
		showHeadsUpDisplay(runMode);

		initialized = true;
	}

	/**
	 * Shows the requested scene in the appropriate display (as designated by the
	 * scene id).
	 * 
	 * @param sceneId
	 *          Id of the scene to show.
	 */
	public void showScene(SceneId sceneId) {

		LOGGER.info("Changing scene on {}: {}", sceneId.getDisplayId(), sceneId);

		Scene scene = sceneMap.get(sceneId);
		if (scene == null) {
			LOGGER.error("Scene not found: " + sceneId);
			return;
		}

		switch (sceneId.getDisplayId()) {
		case CP:
			cpStage.setScene(scene);
			break;
		case HUD:
			hudStage.setScene(scene);
		}
	}

	/**
	 * Change the style of the UI displays.
	 * 
	 * @param newStyle
	 *          New style for the UI displays.
	 */
	public void changeDisplayStyle(DisplayStyle newStyle) {

		LOGGER.info("Changing display style: " + newStyle);

		if (newStyle == displayState.currentStyle().getValue()) {
			// No change in styles so nothing to do.
			LOGGER.debug("Requested style is same as existing style; nothing to do.");
			return;
		}

		// Apply the style and then update our state.
		applyStyle(newStyle);
		displayState.setCurrentStyle(newStyle);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Optionally show the control panel based on the run mode.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 */
	private void showControlPanel(RunMode runMode) {

		switch (runMode) {
		case DEV:
			cpStage.show();
			break;
		case PI2B_CP:
			cpStage.show();
			cpStage.setFullScreenExitHint("");
			cpStage.setFullScreen(true);
			break;
		default:
			LOGGER.info("Control panel not being shown in run mode: " + runMode);
		}
	}

	/**
	 * Optionally show the heads up display based on the run mode.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 */
	private void showHeadsUpDisplay(RunMode runMode) {

		switch (runMode) {
		case DEV:
			hudStage.show();
			break;
		case PI2B_HUD:
			hudStage.show();
			hudStage.setFullScreen(true);
			break;
		default:
			LOGGER.info("Heads up display not being shown in run mode: " + runMode);
		}
	}

	/**
	 * Apply a designated style to the UI by activating its corresponding
	 * stylesheet (or clearing the stylesheet for the NONE style).
	 * 
	 * @param style
	 *          Style to apply to the UI.
	 */
	private void applyStyle(DisplayStyle style) {

		LOGGER.debug("Applying stylesheet: " + style);

		String stylesheetLocation = null;

		if (style != DisplayStyle.NONE) {
			String stylesheetPath = STYLESHEET_RESOURCE_BASE + style.getCSSFile();
			URL stylesheetURL = getClass().getResource(stylesheetPath);

			if (stylesheetURL != null) {
				stylesheetLocation = stylesheetURL.toString();
			} else {
				LOGGER.error("Stylesheet not found found for style: " + stylesheetPath);
			}
		}

		// Load the stylesheet. If the display style is NONE or the stylesheet CSS
		// was not found the location will be null and will have the effect of
		// removing any stylesheet currently in effect.
		Application.setUserAgentStylesheet(stylesheetLocation);

	}

	/**
	 * Creates the initial display state object as configured by the supplied
	 * profile.
	 * 
	 * @param profile
	 *          Profile to use to configure the display state.
	 * @return Configured display state object.
	 */
	private DisplayState createInitialState(Profile profile) {
		displayState = new InternalDisplayState();
		displayState.setCurrentStyle(profile.getDisplayStyle());
		return displayState;
	}

	/**
	 * Load all of the registered scenes from their FXML definitions.
	 */
	private void loadScenes() {

		LOGGER.info("Loading {} scenes...", SceneId.values().length);

		for (SceneId sceneId : SceneId.values()) {

			String scenePath = SCENE_RESOURCE_BASE + sceneId.getDisplayId() + "/" + sceneId.getFxmlName() + ".fxml";
			LOGGER.debug("Loading scene: " + scenePath);

			URL sceneURL = this.getClass().getResource(scenePath);
			if (sceneURL == null) {
				throw new LaissezException("Scene FXML not found: " + sceneId);
			}

			FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);
			Parent parent;
			try {
				parent = fxmlLoader.load();
			} catch (IOException e) {
				throw new LaissezException("Failed to load scene: " + sceneId, e);
			}

			double width = 0.0;
			double height = 0.0;
			switch (sceneId.getDisplayId()) {
			case CP:
				width = CONTROL_PANEL_WIDTH;
				height = CONTROL_PANEL_HEIGHT;
				break;
			case HUD:
				width = HUD_WIDTH;
				height = HUD_HEIGHT;
				break;
			default:
				throw new LaissezException("Unhandled display id for setting scene dimensions: " + sceneId.getDisplayId());
			}

			Scene scene = new Scene(parent, width, height);
			sceneMap.put(sceneId, scene);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the display service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param runMode
	 *          Run mode for the operating system.
	 * @param profile
	 *          Profile to use for configuring initial state of the display
	 *          service.
	 * @return Audio service instance and its initial state object.
	 */
	public static Pair<DisplayService, DisplayState> createService(RunMode runMode, Profile profile) {

		DisplayService service = new DisplayService();
		DisplayState state = service.createInitialState(profile);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the Display service.
	 */
	private static class InternalDisplayState implements DisplayState {

		/**
		 * Current display style.
		 */
		private SimpleObjectProperty<DisplayStyle> currentStyle = new SimpleObjectProperty<>(this, "currentStyle");

		// ----------------------------------------------------------------------------------------
		// DisplayState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<DisplayStyle> currentStyle() {
			return currentStyle;
		}

		// ----------------------------------------------------------------------------------------
		// Supporting methods.
		// ----------------------------------------------------------------------------------------

		private void setCurrentStyle(DisplayStyle newStyle) {
			currentStyle.setValue(newStyle);
		}
	}
}
