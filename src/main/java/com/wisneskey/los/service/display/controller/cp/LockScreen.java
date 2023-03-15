package com.wisneskey.los.service.display.controller.cp;

import com.wisneskey.los.service.display.controller.AbstractController;

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

	private String pinEntered = "";

	private String pinDisplayed = "";

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		pinDisplay.setText(pinDisplayed);
	}

	@FXML
	public void buttonOnePressed(ActionEvent event) {
		buttonPressed("1");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonTwoPressed(ActionEvent event) {
		buttonPressed("2");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonThreePressed(ActionEvent event) {
		buttonPressed("3");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonFourPressed(ActionEvent event) {
		buttonPressed("4");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonFivePressed(ActionEvent event) {
		buttonPressed("5");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonSixPressed(ActionEvent event) {
		buttonPressed("6");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonSevenPressed(ActionEvent event) {
		buttonPressed("7");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonEightPressed(ActionEvent event) {
		buttonPressed("8");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonNinePressed(ActionEvent event) {
		buttonPressed("9");
		pinDisplay.requestFocus();
	}

	@FXML
	public void buttonZeroPressed(ActionEvent event) {
		buttonPressed("0");
		pinDisplay.requestFocus();
	}

	private void buttonPressed(String buttonValue) {

		pinEntered += buttonValue;
		pinDisplayed += "#";
		pinDisplay.setText(pinDisplayed);

		// When we have four digits, submit it to the security service.
		if (pinEntered.length() >= 4) {

			// TODO: Call unlock on security service.
			pinEntered = "";
			pinDisplayed = "";
			pinDisplay.setText(pinDisplayed);
		}
	}
}
