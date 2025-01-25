package com.wisneskey.los.service.display;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.SceneController;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.state.DisplayState;
import com.wisneskey.los.state.RemoteState;
import com.wisneskey.los.util.PropertyChangeListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * Service for working with the Laissez Boy displays.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
public class DisplayService extends AbstractService<DisplayState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisplayService.class);

	/**
	 * Default starting scene for the heads up display.
	 */
	public static final SceneId STARTING_HUD_SCENE = SceneId.HUD_SPLASH_SCREEN;

	/**
	 * Default starting scene for the control panel.
	 */
	public static final SceneId STARTING_CP_SCENE = SceneId.CP_SPLASH_SCREEN;

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
	private static final double HUD_WIDTH = 800.0;

	/**
	 * Height of the heads up display.
	 */
	private static final double HUD_HEIGHT = 480.0;

	/**
	 * Width of the control panel display.
	 */
	private static final double CONTROL_PANEL_WIDTH = 400.0;

	/**
	 * Height of the control panel display.
	 */
	private static final double CONTROL_PANEL_HEIGHT = 1280.0;

	/**
	 * Height of the behind the scenes alert dialog.
	 */
	private static final double ALERT_WIDTH = 380.0;

	/**
	 * Height of the behind the scenes alert dialog.
	 */
	private static final double ALERT_HEIGHT = 240.0;

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
	private Map<SceneId, Parent> sceneMap = new EnumMap<>(SceneId.class);

	/**
	 * Map of scene id's to their controllers for that scene.
	 */
	private Map<SceneId, SceneController> sceneControllerMap = new EnumMap<>(SceneId.class);

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
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public DisplayState getState() {
		return displayState;
	}

	@Override
	public void terminate() {
		LOGGER.trace("Display service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method called by SpringFX once the application is launched by the boot
	 * loader.
	 * 
	 * @param  stage       Main stage for the SpringFX application.
	 * @throws IOException
	 */
	public void initialize(Stage stage) {

		if (initialized) {
			throw new LaissezException("Display manager already initialized.");
		}

		// Log the screens
		for (Screen screen : Screen.getScreens()) {

			boolean isPrimary = screen.equals(Screen.getPrimary());
			Rectangle2D bounds = screen.getVisualBounds();
			LOGGER.info("Screen: minX={} minY={} primary={}", bounds.getMinX(), bounds.getMinY(), isPrimary);
		}

		// Determine the run mode since that governs how and what is shown.
		RunMode runMode = Kernel.kernel().getRunMode();

		// Apply the style set in our state
		applyStyle(displayState.currentStyle().getValue());

		// Load all of the scenes so that we can start displaying them once the
		// stages are set up.
		loadScenes();

		// Create the stages for both screens but the run mode will dictate which
		// ones actually get shown and where they are placed. We set the initial
		// scene to be the splash screen so that we can change its contents later.
		// If we switch the actual scene, the menu bar pops up.

		// Control panel stage:
		cpStage = stage;
		cpStage.setScene(
				new Scene(sceneMap.get(displayState.cpScene().getValue()), CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT));

		// Heads up display stage:
		hudStage = new Stage();
		hudStage.setScene(new Scene(sceneMap.get(displayState.hudScene().getValue()), HUD_WIDTH, HUD_HEIGHT));

		switch (runMode) {
		case DEV:
			initDisplaysForDev();
			break;
		case CHAIR:
			initDisplaysForChair();
			break;
		default:
			throw new LaissezException("Unhandled chair mode in display initialization: " + runMode);
		}

		// Monitor the remote service state so we can detect button changes and tell
		// the current active scenes about buttons being pressed.
		RemoteState remoteState = Kernel.kernel().chairState().getServiceState(ServiceId.REMOTE);
		remoteState.buttonA().addListener(
				new PropertyChangeListener<>(t -> remoteButtonChange(t.booleanValue(), RemoteButtonId.REMOTE_BUTTON_A)));
		remoteState.buttonB().addListener(
				new PropertyChangeListener<>(t -> remoteButtonChange(t.booleanValue(), RemoteButtonId.REMOTE_BUTTON_B)));

		initialized = true;
	}

	/**
	 * Shows the requested scene in the appropriate display (as designated by the
	 * scene id).
	 * 
	 * @param sceneId Id of the scene to show.
	 */
	public void showScene(SceneId sceneId) {

		LOGGER.info("Changing scene on {}: {}", sceneId.getDisplayId(), sceneId);

		Parent content = sceneMap.get(sceneId);
		if (content == null) {
			LOGGER.error("Scene not found: {}", sceneId);
			return;
		}

		// Change the appropriate display's scene contents. We do not set a new
		// scene because in full screen mode this causes the menu bar to reappear.
		if (sceneId.getDisplayId() == DisplayId.CP) {

			Platform.runLater(() -> cpStage.getScene().setRoot(content));
			Platform.runLater(() -> cpStage.requestFocus());

		} else {

			Platform.runLater(() -> hudStage.getScene().setRoot(content));
			Platform.runLater(() -> hudStage.requestFocus());

		}

		// Update the display state for any change listeners.
		displayState.updateScene(sceneId);

		SceneController controller = sceneControllerMap.get(sceneId);
		controller.sceneShown();
	}

	/**
	 * Change the style of the UI displays.
	 * 
	 * @param newStyle New style for the UI displays.
	 */
	public void changeDisplayStyle(DisplayStyle newStyle) {

		LOGGER.info("Changing display style: {}", newStyle);

		if (newStyle == displayState.currentStyle().getValue()) {
			// No change in styles so nothing to do.
			LOGGER.debug("Requested style is same as existing style; nothing to do.");
			return;
		}

		// Apply the style and then update our state.
		applyStyle(newStyle);
		displayState.setCurrentStyle(newStyle);
	}

	/**
	 * Presents a confirmation dialog. Intended to be used only for hidden system
	 * processing and not normal chair operation.
	 * 
	 * @return True if operation is confirmed; false otherwise.
	 */
	public boolean showConfirmation(DisplayId displayId, String title, String header, String content) {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setResizable(false);
		alert.setWidth(ALERT_WIDTH);
		alert.setHeight(ALERT_HEIGHT);

		Stage targetStage = (displayId == DisplayId.CP) ? cpStage : hudStage;
		alert.initOwner(targetStage);
		alert.setX(centerOn(targetStage.getX(), targetStage.getWidth(), alert.getWidth()));
		alert.setX(centerOn(targetStage.getY(), targetStage.getHeight(), alert.getHeight()));
		Optional<ButtonType> option = alert.showAndWait();

		return option.isPresent() && (option.get() == ButtonType.OK);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Calculate the offset necessary to center a component on top of another one.
	 * 
	 * @param  targetPosition    Position of the target that is being centered on.
	 * @param  targetMeasurement Measurement of the target that is being centered
	 *                             on.
	 * @param  measurement       Measurement of what is being centered.
	 * @return                   Position that will center the component on top of
	 *                           the target.
	 */
	private double centerOn(double targetPosition, double targetMeasurement, double measurement) {
		return targetPosition + (targetMeasurement / 2.0) - (measurement / 2.0);
	}

	/**
	 * Configure the two display windows for development mode.
	 */
	private void initDisplaysForDev() {

		hudStage.setX(100.0);
		hudStage.setY(100.0);
		hudStage.show();

		cpStage.setX(1100.0);
		cpStage.setY(40.0);
		cpStage.show();
	}

	/**
	 * Configure the two display windows to map the appropriate displays that are
	 * hooked up on the actual chair.
	 */
	private void initDisplaysForChair() {

		// Technique to get both screens with no window controls visible:
		// https://stackoverflow.com/questions/13030556/multiple-javafx-stages-in-fullscreen-mode

		Screen cpScreen = Screen.getPrimary();
		cpStage.setX(cpScreen.getVisualBounds().getMinX());
		cpStage.setY(cpScreen.getVisualBounds().getMinY());
		cpStage.setFullScreenExitHint("");
		cpStage.setFullScreen(true);
		cpStage.show();

		Screen hudScreen = Screen.getScreens().get(1);
		hudStage.setX(hudScreen.getBounds().getMinX());
		hudStage.setY(hudScreen.getBounds().getMinY());
		hudStage.initStyle(StageStyle.UNDECORATED);
		hudStage.show();
	}

	/**
	 * Apply a designated style to the UI by activating its corresponding
	 * stylesheet (or clearing the stylesheet for the NONE style).
	 * 
	 * @param style Style to apply to the UI.
	 */
	private void applyStyle(DisplayStyle style) {

		LOGGER.debug("Applying stylesheet: {}", style);

		String stylesheetLocation = null;

		if (style != DisplayStyle.NONE) {
			String stylesheetPath = STYLESHEET_RESOURCE_BASE + style.getCSSFile();
			URL stylesheetURL = getClass().getResource(stylesheetPath);

			if (stylesheetURL != null) {
				stylesheetLocation = stylesheetURL.toString();
			} else {
				LOGGER.error("Stylesheet not found found for style: {}", stylesheetPath);
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
	 * @param  profile Profile to use to configure the display state.
	 * @return         Configured display state object.
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

			String scenePath = SCENE_RESOURCE_BASE + sceneId.getDisplayId() + File.separator + sceneId.getFxmlName()
					+ ".fxml";
			LOGGER.debug("Loading scene: {}", scenePath);

			URL sceneURL = this.getClass().getResource(scenePath);
			if (sceneURL == null) {
				throw new LaissezException("Scene FXML not found: " + sceneId);
			}

			FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);
			SceneController controller;
			Parent parent;
			try {
				parent = fxmlLoader.load();
				controller = fxmlLoader.getController();
			} catch (Exception e) {
				throw new LaissezException("Failed to load scene: " + sceneId, e);
			}

			sceneMap.put(sceneId, parent);
			sceneControllerMap.put(sceneId, controller);
		}
	}

	/**
	 * Method invoked by the remote button state even listeners when a remote
	 * button is pressed.
	 * 
	 * @param buttonId Id of the button that was pressed.
	 */
	private void remoteButtonChange(boolean pressed, RemoteButtonId buttonId) {

		if (pressed) {
			SceneId cpSceneId = displayState.cpScene().getValue();
			SceneId hudSceneId = displayState.hudScene().getValue();

			SceneController cpController = cpSceneId == null ? null : sceneControllerMap.get(cpSceneId);
			SceneController hudController = hudSceneId == null ? null : sceneControllerMap.get(hudSceneId);

			if (cpController != null) {
				cpController.remoteButtonPressed(buttonId);
			}

			if (hudController != null) {
				hudController.remoteButtonPressed(buttonId);
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the display service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile to use for configuring initial state of the display
	 *                   service.
	 * @return         Display service instance and its initial state object.
	 */
	public static Pair<DisplayService, DisplayState> createService(Profile profile) {

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
		 * Current HUD scene.
		 */
		private SimpleObjectProperty<SceneId> hudScene = new SimpleObjectProperty<>(STARTING_HUD_SCENE);

		/**
		 * Current control panel scene.
		 */
		private SimpleObjectProperty<SceneId> cpScene = new SimpleObjectProperty<>(STARTING_CP_SCENE);
		/**
		 * Current display style.
		 */
		private SimpleObjectProperty<DisplayStyle> currentStyle = new SimpleObjectProperty<>(this, "currentStyle");

		// ----------------------------------------------------------------------------------------
		// DisplayState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<SceneId> hudScene() {
			return hudScene;
		}

		@Override
		public ReadOnlyObjectProperty<SceneId> cpScene() {
			return cpScene;
		}

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

		private void updateScene(SceneId sceneId) {
			switch (sceneId.getDisplayId()) {
			case CP:
				cpScene.setValue(sceneId);
				break;
			case HUD:
				hudScene.setValue(sceneId);
				break;
			default:
				throw new LaissezException("Unknown display id while changing scene: " + sceneId.getDisplayId());

			}
		}
	}
}
