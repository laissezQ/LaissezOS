package com.wisneskey.los.service.display.controller.cp;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.label.UpdateLabelListener;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.LocationState;
import com.wisneskey.los.state.MapState;
import com.wisneskey.los.util.RunProcess;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Controller for the control panel boot screen.
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
public class SystemScreen extends AbstractController {

	/**
	 * Command to use to open a terminal on the Raspberry Pi.
	 */
	private static final String[] OPEN_TERMINAL_RASPBERRY_PI = { "lxterminal" };

	/**
	 * Command to use to open a terminal on the OS X laptop.
	 */
	private static final String[] OPEN_TERMINAL_LAPTOP = { "/usr/bin/open", "Applications/iTerm" };

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Button to exit LaissezOS.
	 */
	@FXML
	private Button exitButton;

	/**
	 * Label to use to show the status of the GPS fix.
	 */
	@FXML
	private Label gpsStatusLabel;

	/**
	 * Check box that controls if online map download is allowed.
	 */
	@FXML
	private CheckBox onlineMapCheckBox;

	/**
	 * Label for displaying the number of satellites in view.
	 */
	@FXML
	private Label satelliteInViewLabel;

	/**
	 * Label for displaying the number of satellites in GPS fix.
	 */
	@FXML
	private Label satelliteInFixLabel;

	/**
	 * Vertical box to add preset locations to.
	 */
	@FXML
	private VBox locationBox;

	/**
	 * Label for showing the IP address of the Raspberry Pi.
	 */
	@FXML
	private Label ipAddressLabel;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		// Set our IP address in the label.
		ipAddressLabel.setText(getIpAddress());

		// Bind the online map download check box to the map state property.
		MapState mapState = chairState().getServiceState(ServiceId.MAP);
		onlineMapCheckBox.selectedProperty().bindBidirectional(mapState.getOnline());

		LocationState locationState = chairState().getServiceState(ServiceId.LOCATION);

		// Set initial rendering based on current state.
		updateFixStatus(locationState.hasGpsFix().get());

		if( locationState.hasGpsFix().get()) {
			satelliteInViewLabel.setText(String.valueOf(locationState.satellitesInView().get()));
			satelliteInFixLabel.setText(String.valueOf(locationState.satellitesInFix().get()));
		}
		
		// Add listeners for the GPS state.
		locationState.hasGpsFix().addListener(new FixListener());

		locationState.satellitesInView().addListener(new UpdateLabelListener<>(satelliteInViewLabel));
		locationState.satellitesInFix().addListener(new UpdateLabelListener<>(satelliteInFixLabel));

		// Build out the buttons for the preset locations.
		Profile profile = ((ProfileService) kernel().getService(ServiceId.PROFILE)).getState().activeProfile().get();
		for (Map.Entry<String, Location> entry : profile.getPresetLocations().entrySet()) {

			Button locationButton = createListButton(entry.getKey());
			locationButton.setOnAction(e -> jumpToLocation(entry.getKey(), entry.getValue()));
			locationBox.getChildren().add(locationButton);
		}

		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));
	}

	/**
	 * Method invoked by the Open Terminal button.
	 */
	public void terminalPressed() {

		kernel().message("Opening terminal...\n");
		String[] command = kernel().getRunMode() == RunMode.CHAIR ? OPEN_TERMINAL_RASPBERRY_PI : OPEN_TERMINAL_LAPTOP;
		RunProcess.runCommand(command);
	}

	/**
	 * Method invoked by the exit LaissezOS button.
	 */
	public void exitPressed() {

		boolean confirmed = ((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)) //
				.showConfirmation(DisplayId.CP, //
						"Confirm Exit", //
						"LaissezOS exit has been requested!", "Do you want to exit LaissezOS?");

		if (confirmed) {
			runScript(ScriptId.SYSTEM_EXIT);
		}
	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.SYSTEM_SCREEN_CLOSE);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Get the IP address for the machine by using the address selected for an
	 * outgoing connection.
	 * 
	 * @return IP address for machine or "Unknown" if it could not be determined.
	 */
	private String getIpAddress() {

		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			return socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			return "Unknown";
		}

	}

	/**
	 * Method invoked by a location button to jump to a given location on the map.
	 * 
	 * @param location Location to jump to on the map.
	 */
	private void jumpToLocation(String name, Location location) {

		kernel().message("Setting map location: " + name + "\n");
		MapState mapState = kernel().chairState().getServiceState(ServiceId.MAP);

		// Turn of tracking to GPS since we are jumping to a location.
		mapState.getTracking().set(false);
		mapState.getMapCenter().set(location);
	}

	/**
	 * Updates the coloring of the
	 * 
	 * @param hasFix
	 */
	private void updateFixStatus(boolean hasFix) {

		gpsStatusLabel.setTextFill(hasFix ? Color.LIGHTGREEN : Color.RED);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Listener that monitors the state of the GPS fix and updates the color of
	 * the GPS label appropriately.
	 */
	private class FixListener implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			updateFixStatus(newValue.booleanValue());
		}
	}
}