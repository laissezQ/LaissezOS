package com.wisneskey.los.service.display;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.LaissezOS;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
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
	 * Scene for the control panel display.
	 */
	private Scene cpScene;

	/**
	 * Scene for the heads up display.
	 */
	private Scene hudScene;

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

		// Apply the style set in our state
		applyStyle(displayState.currentStyle().getValue());

		// Create the stages for both screens but the run mode will dictate which
		// ones actually
		// get shown and where they are placed.

		// Control panel stage:
		cpStage = stage;
		cpScene = new Scene(loadFXML("primary"), 400, 1280);
		cpStage.setScene(cpScene);

		// Heads up display stage:
		hudStage = new Stage();
		hudScene = new Scene(loadFXML("secondary"), 640, 480);
		hudStage.setScene(hudScene);

		// Show each stage depending on run mode and screen availability.
		RunMode runMode = Kernel.kernel().getRunMode();
		showControlPanel(runMode);
		showHeadsUpDisplay(runMode);

		initialized = true;
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

	public void setRoot(String fxml) throws IOException {
		cpScene.setRoot(loadFXML(fxml));
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

	private Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(LaissezOS.class.getResource("/display/" + fxml + ".fxml"));
		return fxmlLoader.load();
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
