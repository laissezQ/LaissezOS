package com.wisneskey.lbos.service.display;

import java.io.IOException;

import com.wisneskey.lbos.LaissezBoyOS;
import com.wisneskey.lbos.service.AbstractService;
import com.wisneskey.lbos.service.ServiceId;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main point for working with the Laissez Boy displays.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class DisplayService extends AbstractService {

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
	 * Private constructor to disallow instantiation.
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
		//ObservableList<Screen> screens = Screen.getScreens();

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
		FXMLLoader fxmlLoader = new FXMLLoader(LaissezBoyOS.class.getResource("/display/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public void setRoot(String fxml) throws IOException {
		cpScene.setRoot(loadFXML(fxml));
	}

}
