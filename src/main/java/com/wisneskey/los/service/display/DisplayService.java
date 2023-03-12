package com.wisneskey.los.service.display;

import java.io.IOException;

import com.wisneskey.los.LaissezOS;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.DisplayState;

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

	public void initialize(Stage stage) throws IOException {

		if (initialized) {
			throw new RuntimeException("Display manager already initialized.");
		}

		// Determine what screens are available
		// ObservableList<Screen> screens = Screen.getScreens();

		// First set up the control panel.
		cpStage = stage;
		cpScene = new Scene(loadFXML("primary"), 400, 1280);
		cpStage.setScene(cpScene);
		cpStage.show();

		// Now set up the heads up display.
		hudStage = new Stage();
		hudScene = new Scene(loadFXML("secondary"), 640, 480);
		hudStage.setScene(hudScene);
		hudStage.show();

		initialized = true;
	}

	private Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(LaissezOS.class.getResource("/display/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public void setRoot(String fxml) throws IOException {
		cpScene.setRoot(loadFXML(fxml));
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private DisplayState createInitialState(Profile profile) {
		displayState = new InternalDisplayState();
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

	private static class InternalDisplayState implements DisplayState {

	}
}
