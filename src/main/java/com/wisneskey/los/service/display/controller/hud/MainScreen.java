package com.wisneskey.los.service.display.controller.hud;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToLabelListener;
import com.wisneskey.los.service.display.map.MapServiceTileFactory;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.state.LocationState;
import com.wisneskey.los.state.MapState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Controller for the heads up display main screen.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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

	/**
	 * Controls the size of the X drawn for the chair on the map.
	 */
	private static final int CHAIR_MARKER_SIZE = 8;

	/**
	 * Controls the width of the lines in the X drawn for the chair on the map.
	 */
	private static final Stroke CHAIR_MARKER_THICKNESS = new BasicStroke(5);

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

	/**
	 * Waypoint used to render the location of the chair on the map.
	 */
	private Waypoint chairMarker;

	/**
	 * Painter for drawing the markers on the map.
	 */
	private WaypointPainter<Waypoint> markerPainter;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		MapState mapState = kernel().chairState().getServiceState(ServiceId.MAP);

		// Get the location state for the initial location and for installing
		// listeners.
		LocationState locationState = Kernel.kernel().chairState().getServiceState(ServiceId.LOCATION);
		Location initialLocation = locationState.location().getValue();

		swingNode = new SwingNode();
		
		// create a map view and set the map to it
		SwingUtilities.invokeLater(() -> {

			mapViewer = new JXMapViewer();

			// Use our Map service as the tile factory.  This is not particularly efficient
			// when everything is being fetched online but will be fine with the local cache.
			MapServiceTileFactory tileFactory = new MapServiceTileFactory();
			mapViewer.setTileFactory(tileFactory);

			// Set the focus
			mapViewer.setZoom(1);
			mapViewer.setAddressLocation(locationToGeoPosition(initialLocation));

			MouseInputListener mia = new TrackingMouseListener(mapViewer);
			mapViewer.addMouseListener(mia);
			mapViewer.addMouseMotionListener(mia);
			mapViewer.addMouseListener(new CenterMapListener(mapViewer));

			// Create a waypoint for the chair and configure the map overlay
			// painter to render that waypoint.
			chairMarker = new ChairMarker();

			markerPainter = new WaypointPainter<>();
			markerPainter.setWaypoints(Collections.singleton(chairMarker));
			markerPainter.setRenderer(new MarkerRenderer());

			mapViewer.setOverlayPainter(markerPainter);

			// Put the map viewer in the Swing node and style it.
			swingNode.setContent(mapViewer);
			swingNode.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid; -fx-background-color: gray;");
			swingNode.effectProperty().set(new DropShadow());
			
			mainPane.centerProperty().set(swingNode);
			BorderPane.setMargin(swingNode, new Insets(4.0, 0.0, 4.0, 2.0));

			// Add listener for when map is moved
			mapViewer.addPropertyChangeListener("addressLocation", new AddressLocationListener());
			
			// Set initial rendering based on current state.
			updateFixStatus(locationState.hasGpsFix().get());
			updateLocation(locationState.location().get());

			// Add listeners for the GPS state.
			locationState.hasGpsFix().addListener(new FixListener());
			locationState.location().addListener(new GpsLocationListener());

		});
				
		
		// Show last message at bottom of the heads up display.
		chairState().message().addListener(new MessagesToLabelListener(message));

		// Listen to the tracking check box so we can handle when its toggled.
		trackingCheckBox.selectedProperty().addListener(new TrackingListener());
		
		// Bind the tracking check box to the state property to keep them in sync.
		trackingCheckBox.selectedProperty().bindBidirectional(mapState.getTracking());
	
		// Listen to the map center property so we can respond when its changed.
		mapState.getMapCenter().addListener(new MapCenterListener());
		// Listen to the zoom slider for controlling the map zoom.
		zoomSlider.valueProperty().addListener(new ZoomListener());
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

	private class AddressLocationListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			
			GeoPosition newPosition =(GeoPosition) event.getNewValue();
			Location location = new Location(newPosition.getLatitude(),  newPosition.getLongitude(), 0);
			
			MapState mapState = kernel().chairState().getServiceState(ServiceId.MAP);
			mapState.getMapCenter().set(location);
		}
	}
	
	/**
	 * Listener that responds when the map center state property is changed.
	 */
	private class MapCenterListener implements ChangeListener<Location> {
		
		@Override
		public void changed(ObservableValue<? extends Location> observable, Location oldValue, Location newValue) {
			updateLocation(newValue);
		}

	}
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

	/**
	 * Listener for monitor the checked state of the tracking check box.
	 * Re-centers the map on the current location when tracking is re-enabled.
	 */
	private class TrackingListener implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

			if (newValue.booleanValue()) {

				// Tracking has been selected so re-center the map on the last known
				// location.
				updateLocation(lastLocation);
			}
		}
	}

	/**
	 * Listener for zoom slider that adjusts the map zoom level when the slider is
	 * changed.
	 */
	private class ZoomListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

			mapViewer.setZoom(newValue.intValue());
		}
	}

	/**
	 * Listener for updating the address location of the map with the latest GPS
	 * coordinates.
	 */
	private class GpsLocationListener implements ChangeListener<Location> {

		@Override
		public void changed(ObservableValue<? extends Location> observable, Location oldValue, Location newValue) {
			updateLocation(newValue);
		}
	}

	/**
	 * Listener that monitors the user's interaction with the map via the HUD and
	 * turns off the tracking check box if the user manually pans the map viewer.
	 */
	private class TrackingMouseListener extends PanMouseInputListener {

		private TrackingMouseListener(JXMapViewer mapViewer) {
			super(mapViewer);
		}

		@Override
		public void mouseDragged(MouseEvent evt) {
			// User has dragged the map so disable the tracking check box.
			MapState mapState = kernel().chairState().getServiceState(ServiceId.MAP);
			mapState.getTracking().set(false);
			//trackingCheckBox.selectedProperty().set(false);

			// Let the pan mouse listener handle the actual drag.
			super.mouseDragged(evt);
		}
	}

	/**
	 * Waypoint implementation that places a waypoint at the last known location
	 * of the GPS (e.g. where the chair presumably is).
	 */
	private class ChairMarker implements Waypoint {

		@Override
		public GeoPosition getPosition() {
			return locationToGeoPosition(lastLocation);
		}
	}

	/**
	 * Waypoint renderer that paints an X at the chair location.
	 */
	private static class MarkerRenderer implements WaypointRenderer<Waypoint> {

		@Override
		public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {

			Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());

			int x = (int) point.getX();
			int y = (int) point.getY();

			g.setColor(java.awt.Color.BLUE);
			g.setStroke(CHAIR_MARKER_THICKNESS);
			g.drawLine(x - CHAIR_MARKER_SIZE, y - CHAIR_MARKER_SIZE, x + CHAIR_MARKER_SIZE, y + CHAIR_MARKER_SIZE);
			g.drawLine(x - CHAIR_MARKER_SIZE, y + CHAIR_MARKER_SIZE, x + CHAIR_MARKER_SIZE, y - CHAIR_MARKER_SIZE);
		}
	}
}
