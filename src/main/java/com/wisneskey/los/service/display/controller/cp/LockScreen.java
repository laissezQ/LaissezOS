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

/**
 * Controller for the control panel lock screen.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LockScreen extends AbstractController {

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
		
		// Add a listener for the chair state so that if the chair is set to a master state of LOCKED,
		// we can assume we will be getting displayed and we need to enable the PIN entry buttons.
		chairState().masterState().addListener(new MasterStateListener());
		
		// Put focus on the PIN display.
		redirectFocus();
	}

	@FXML
	public void buttonOnePressed(ActionEvent event) {
		buttonPressed("1");
		redirectFocus();
	}

	@FXML
	public void buttonTwoPressed(ActionEvent event) {
		buttonPressed("2");
		redirectFocus();
	}

	@FXML
	public void buttonThreePressed(ActionEvent event) {
		buttonPressed("3");
		redirectFocus();
	}

	@FXML
	public void buttonFourPressed(ActionEvent event) {
		buttonPressed("4");
		redirectFocus();
	}

	@FXML
	public void buttonFivePressed(ActionEvent event) {
		buttonPressed("5");
		redirectFocus();
	}

	@FXML
	public void buttonSixPressed(ActionEvent event) {
		buttonPressed("6");
		redirectFocus();
	}

	@FXML
	public void buttonSevenPressed(ActionEvent event) {
		buttonPressed("7");
		redirectFocus();
	}

	@FXML
	public void buttonEightPressed(ActionEvent event) {
		buttonPressed("8");
		redirectFocus();
	}

	@FXML
	public void buttonNinePressed(ActionEvent event) {
		buttonPressed("9");
		redirectFocus();
	}

	@FXML
	public void buttonZeroPressed(ActionEvent event) {
		buttonPressed("0");
		redirectFocus();
	}

	private void redirectFocus() {
		Platform.runLater(() -> pinDisplay.requestFocus());
	}
	
	private void setEntryPadState(boolean enabled) {
		
		for( Button button : entryButtons) {
			
			button.setDisable(! enabled);
		}
	}
	
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
			if( ! unlocked ) {
				// Failed to unlock so re-enable PIN code entry.
				setEntryPadState(true);
			}
		}
	}
	
	private class MasterStateListener implements ChangeListener<MasterState> {

		@Override
		public void changed(ObservableValue<? extends MasterState> observable, MasterState oldValue, MasterState newValue) {

			// If the chair is locked, enabled our PIN entry because it will be needed to unlock.
			if( newValue == MasterState.LOCKED) {
				setEntryPadState(true);
				redirectFocus();
			}
		}
	}
}
