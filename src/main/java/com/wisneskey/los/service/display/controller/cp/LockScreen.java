package com.wisneskey.los.service.display.controller.cp;

import java.util.ArrayList;
import java.util.List;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.security.SecurityService;
import com.wisneskey.los.state.ChairState.MasterState;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Controller for the control panel lock screen.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LockScreen extends AbstractController {

	@FXML
	private ImageView logo;
	
	@FXML
	private TextField pinDisplay;

	@FXML
	private Button buttonOne;

	@FXML
	private Button buttonTwo;

	@FXML
	private Button buttonThree;

	@FXML
	private Button buttonFour;

	@FXML
	private Button buttonFive;

	@FXML
	private Button buttonSix;

	@FXML
	private Button buttonSeven;

	@FXML
	private Button buttonEight;

	@FXML
	private Button buttonNine;

	@FXML
	private Button buttonZero;

	/**
	 * The actual PIN code being entered.
	 */
	private String pinEntered = "";

	/**
	 * The dummy PIN code that is displayed on screen.
	 */
	private String pinDisplayed = "";

	/**
	 * List of all PIN code entry buttons [0-9].
	 */
	private List<Button> entryButtons;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		pinDisplay.setText(pinDisplayed);
		entryButtons = new ArrayList<>(10);
		entryButtons.add(buttonOne);
		entryButtons.add(buttonTwo);
		entryButtons.add(buttonThree);
		entryButtons.add(buttonFour);
		entryButtons.add(buttonFive);
		entryButtons.add(buttonSix);
		entryButtons.add(buttonSeven);
		entryButtons.add(buttonEight);
		entryButtons.add(buttonNine);
		entryButtons.add(buttonZero);

		// Add a listener for the chair state so that if the chair is set to a
		// master state of LOCKED,
		// we can assume we will be getting displayed and we need to enable the PIN
		// entry buttons.
		chairState().masterState().addListener(new MasterStateListener());

		// Put focus on the logo.
		redirectFocus();
	}

	/**
	 * Action listener for button 1 being pressed.
	 * 
	 * @param event
	 *          Button 1 event.
	 */
	@FXML
	public void buttonOnePressed(ActionEvent event) {
		buttonPressed("1");
		redirectFocus();
	}

	/**
	 * Action listener for button 2 being pressed.
	 * 
	 * @param event
	 *          Button 2 event.
	 */
	@FXML
	public void buttonTwoPressed(ActionEvent event) {
		buttonPressed("2");
		redirectFocus();
	}

	/**
	 * Action listener for button 3 being pressed.
	 * 
	 * @param event
	 *          Button 3 event.
	 */
	@FXML
	public void buttonThreePressed(ActionEvent event) {
		buttonPressed("3");
		redirectFocus();
	}

	/**
	 * Action listener for button 4 being pressed.
	 * 
	 * @param event
	 *          Button 4 event.
	 */
	@FXML
	public void buttonFourPressed(ActionEvent event) {
		buttonPressed("4");
		redirectFocus();
	}

	/**
	 * Action listener for button 5 being pressed.
	 * 
	 * @param event
	 *          Button 5 event.
	 */
	@FXML
	public void buttonFivePressed(ActionEvent event) {
		buttonPressed("5");
		redirectFocus();
	}

	/**
	 * Action listener for button 6 being pressed.
	 * 
	 * @param event
	 *          Button 6 event.
	 */
	@FXML
	public void buttonSixPressed(ActionEvent event) {
		buttonPressed("6");
		redirectFocus();
	}

	/**
	 * Action listener for button 7 being pressed.
	 * 
	 * @param event
	 *          Button 7 event.
	 */
	@FXML
	public void buttonSevenPressed(ActionEvent event) {
		buttonPressed("7");
		redirectFocus();
	}

	/**
	 * Action listener for button 8 being pressed.
	 * 
	 * @param event
	 *          Button 8 event.
	 */
	@FXML
	public void buttonEightPressed(ActionEvent event) {
		buttonPressed("8");
		redirectFocus();
	}

	/**
	 * Action listener for button 9 being pressed.
	 * 
	 * @param event
	 *          Button 9 event.
	 */
	@FXML
	public void buttonNinePressed(ActionEvent event) {
		buttonPressed("9");
		redirectFocus();
	}

	/**
	 * Action listener for button 0 being pressed.
	 * 
	 * @param event
	 *          Button 0 event.
	 */
	@FXML
	public void buttonZeroPressed(ActionEvent event) {
		buttonPressed("0");
		redirectFocus();
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method used by event handlers to send focus back to the logo
	 * that the last button pressed does not remain selected on the screen.
	 */
	private void redirectFocus() {
		Platform.runLater(() -> logo.requestFocus());
	}

	/**
	 * Sets the enabled state for all of the PIN code entry buttons.
	 * 
	 * @param enabled
	 *          Flag indicating if buttons should be enabled or not.
	 */
	private void setEntryPadState(boolean enabled) {

		for (Button button : entryButtons) {

			button.setDisable(!enabled);
		}
	}

	/**
	 * Method invoked by the button pressed event handlers to submit their value
	 * as entered.
	 * 
	 * @param buttonValue
	 *          Value of the button that was pressed.
	 */
	private void buttonPressed(String buttonValue) {

		pinEntered += buttonValue;
		pinDisplayed += "#";
		pinDisplay.setText(pinDisplayed);

		// When we have four digits, submit it to the security service.
		if (pinEntered.length() >= 4) {

			String pinCode = pinEntered;

			setEntryPadState(false);
			pinEntered = "";
			pinDisplayed = "";
			pinDisplay.setText(pinDisplayed);

			boolean unlocked = ((SecurityService) Kernel.kernel().getService(ServiceId.SECURITY)).unlockChair(pinCode);
			if (!unlocked) {
				// Failed to unlock so re-enable PIN code entry.
				setEntryPadState(true);
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Listener for changes in the master chair state so that the PIN code entry
	 * can be unlocked if the chair is set in a locked state.
	 */
	private class MasterStateListener implements ChangeListener<MasterState> {

		@Override
		public void changed(ObservableValue<? extends MasterState> observable, MasterState oldValue, MasterState newValue) {

			// If the chair is locked, enable our PIN entry because it will be needed
			// to unlock.
			if (newValue == MasterState.LOCKED) {
				setEntryPadState(true);
				redirectFocus();
			}
		}
	}
}
