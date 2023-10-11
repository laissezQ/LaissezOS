package com.wisneskey.los.service.display.controller.hud;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.message.MessagesToLabelListener;
import com.wisneskey.los.service.location.Location;
import com.wisneskey.los.state.LocationState;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Controller for the heads up display main screen.
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
	 * Node to show the Swing based MapViewer in.
	 */
	private SwingNode swingNode;

	/**
	 * Map viewer.
	 */
	private JXMapViewer mapViewer;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		// Get the location state for the initial location and to install a listener
		// to
		// update the map position when the location changes.
		LocationState locationState = Kernel.kernel().chairState().getServiceState(ServiceId.LOCATION);
		Location initialLocation = locationState.location().getValue();

		swingNode = new SwingNode();

		// create a map view and set the map to it
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

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

				MouseInputListener mia = new PanMouseInputListener(mapViewer);
				mapViewer.addMouseListener(mia);
				mapViewer.addMouseMotionListener(mia);
				mapViewer.addMouseListener(new CenterMapListener(mapViewer));
				mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

				swingNode.setContent(mapViewer);
			}
		});

		mainPane.centerProperty().set(swingNode);

		chairState().message().addListener(new MessagesToLabelListener(message));
	}

	/**
	 * Converts a location with coordinates to a GeoPosition object used by the
	 * map viewer.
	 * 
	 * @param location
	 *          Location object with coordinates.
	 * @return The equivalent GeoPosition object.
	 */
	private GeoPosition locationToGeoPosition(Location location) {

		return new GeoPosition(location.getLatitude(), location.getLongitude());
	}
}
