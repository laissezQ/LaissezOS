package com.wisneskey.los.service.display;

/**
 * Enumerated type defining the scenes that are available for display.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum SceneId {

	// 
	// Control Panel scenes:
	//
	CP_SPLASH_SCREEN(DisplayId.CP, "SplashScreen"),
	
	//
	// Heads Up Display scenes:
	//
	HUD_SPLASH_SCREEN(DisplayId.HUD, "SplashScreen");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Id of the display the scene is for.
	 */
	private DisplayId displayId;

	/**
	 * Name of the FXML file resource that defines the scene.
	 */
	private String fxmlName;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SceneId(DisplayId displayId, String fxmlName) {
		this.displayId = displayId;
		this.fxmlName = fxmlName;
	}
	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the id of the display the scene can be shown on.
	 * 
	 * @return Id of display to show scene on.
	 */
	public DisplayId getDisplayId() {
		return displayId;
	}

	/**
	 * Returns the name of the FXML file containing the scene definition.
	 * 
	 * @return Name of scene's FXML file.
	 */
	public String getFxmlName() {
		return fxmlName;
	}
}
