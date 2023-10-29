package com.wisneskey.los.service.display.controller.hud;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToLabelListener;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.state.LocationState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Controller for the heads up display main screen.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public class MainScreen extends AbstractController {

	@FXML
	private BorderPane mainPane;

	/**
	 * Label to use to show last message in.
	 */
	@FXML
	private Label message;

	/**
	 * Label to use to show the status of the GPS fix.
	 */
	@FXML
	private Label gpsStatusLabel;

	/**
	 * Check box to select if the map is tracking the chair (e.g. centered on
	 * chair).
	 */
	@FXML
	private CheckBox trackingCheckBox;

	/**
	 * Slider use to control the map zoom level.
	 */
	@FXML
	private Slider zoomSlider;

	/**
	 * Node to show the Swing based MapViewer in.
	 */
	private SwingNode swingNode;

	/**
	 * Map viewer.
	 */
	private JXMapViewer mapViewer;

	/**
	 * Last reported location by GPS.
	 */
	private Location lastLocation;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		// Get the location state for the initial location and for installing
		// listeners.
		LocationState locationState = Kernel.kernel().chairState().getServiceState(ServiceId.LOCATION);
		Location initialLocation = locationState.location().getValue();

		swingNode = new SwingNode();

		// create a map view and set the map to it
		SwingUtilities.invokeLater(() -> {

			mapViewer = new JXMapViewer();

			// Create a TileFactoryInfo for OpenStreetMap

			TileFactoryInfo info = new OSMTileFactoryInfo();
			DefaultTileFactory tileFactory = new DefaultTileFactory(info);
			mapViewer.setTileFactory(tileFactory);

			// Use 8 threads in parallel to load the tiles
			tileFactory.setThreadPoolSize(8);

			// Set the focus
			mapViewer.setZoom(1);
			mapViewer.setAddressLocation(locationToGeoPosition(initialLocation));

			MouseInputListener mia = new TrackingMouseListener(mapViewer);
			mapViewer.addMouseListener(mia);
			mapViewer.addMouseMotionListener(mia);
			mapViewer.addMouseListener(new CenterMapListener(mapViewer));

			swingNode.setContent(mapViewer);
		});

		mainPane.centerProperty().set(swingNode);

		// Show last message at bottom of the heads up display.
		chairState().message().addListener(new MessagesToLabelListener(message));

		// Listen to the tracking check box so we can handle when its toggled.
		trackingCheckBox.selectedProperty().addListener(new TrackingListener());
		
		// Add listeners for the GPS state.
		locationState.hasGpsFix().addListener(new FixListener());
		locationState.location().addListener(new GpsLocationListener());

		// Finally, set initial rendering based on current state.
		updateFixStatus(locationState.hasGpsFix().get());
		updateLocation(locationState.location().get());
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Updates the coloring of the
	 * 
	 * @param hasFix
	 */
	private void updateFixStatus(boolean hasFix) {

		gpsStatusLabel.setTextFill(hasFix ? Color.LIGHTGREEN : Color.RED);
	}

	/**
	 * Method invoked when a new location is reported by the GPS.
	 * 
	 * @param location New location reported by the GPS.
	 */
	private void updateLocation(Location location) {

		if (location == null) {
			// Ignore no location reports since we are tracking the GPS fix status. We
			// will
			// keep the last known location.
			return;
		}

		lastLocation = location;
		mapViewer.setAddressLocation(locationToGeoPosition(location));
		
		if (trackingCheckBox.isPressed()) {

			// Tracking check box is selected so center the map on the new position.
			mapViewer.recenterToAddressLocation();
		}
	}

	/**
	 * Converts a location with coordinates to a GeoPosition object used by the
	 * map viewer.
	 * 
	 * @param  location Location object with coordinates.
	 * @return          The equivalent GeoPosition object.
	 */
	private GeoPosition locationToGeoPosition(Location location) {

		return new GeoPosition(location.getLatitude(), location.getLongitude());
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

	private class TrackingListener implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			
			if( newValue.booleanValue() ) {
				
				// Tracking has been selected so re-center the map on the last known location.
				updateLocation(lastLocation);
			}
		}
	}

	private class GpsLocationListener implements ChangeListener<Location> {

		@Override
		public void changed(ObservableValue<? extends Location> observable, Location oldValue, Location newValue) {
			updateLocation(newValue);
		}
	}

	private class TrackingMouseListener extends PanMouseInputListener {

		private TrackingMouseListener(JXMapViewer mapViewer) {
			super(mapViewer);
		}

		@Override
		public void mouseDragged(MouseEvent evt) {

			// User has dragged the map so disable the tracking check box.
			trackingCheckBox.selectedProperty().set(false);

			// Let the pan mouse listener handle the actual drag.
			super.mouseDragged(evt);
		}
	}
}
