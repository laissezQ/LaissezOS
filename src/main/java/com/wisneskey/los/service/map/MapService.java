package com.wisneskey.los.service.map;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.MapState;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

/**
 * Service for provide a map for the display. This service is set up to provide
 * local tile caching using the file system so that maps can easily be made
 * available when the chair is offline (e.g. in a parade).
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
public class MapService extends AbstractService<MapState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapService.class);

	/**
	 * Maximum zoom level support by Open Street Map.
	 */
	private static final int OSM_MAX_ZOOM = 19;

	/**
	 * Base URL for fetch tile images from Open Street Map.
	 */
	private static final String OSM_BASE_URL = "http://tile.openstreetmap.org";

	/**
	 * User agent to use when retrieving tiles from Open Street Map.
	 */
	private static final String USER_AGENT = "LaissezOS/1.0";

	/**
	 * Internal state object for tracking the state of the map service.
	 */
	private InternalMapState mapState;

	/**
	 * Cache for map tiles.
	 */
	private MapTileCache tileCache;

	/**
	 * Base bath to the local tile store.
	 */
	private File tileStoreBasePath;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 * 
	 * @param tileStorePath Directory for the local map tile store.
	 */
	private MapService(String tileStorePath) {

		super(ServiceId.MAP);

		// Create the local tile store directory if one is specified and it doesn't
		// exist.
		if (tileStorePath != null) {
			try {
				tileStoreBasePath = new File(tileStorePath).getCanonicalFile();
				if (!tileStoreBasePath.exists()) {
					Files.createDirectories(tileStoreBasePath.toPath());
				}
			} catch (IOException e) {
				throw new LaissezException("Failed to create local tile image store directory.", e);
			}
		} else {
			tileStoreBasePath = null;
		}

		tileCache = MapTileCache.createCache();
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the tile image for the specified coordinates and zoom level if one
	 * can be retrieved. Tries in order: the in memory cache, local store (if
	 * configured), and retrieval from Open Street Map (if allowed).
	 * 
	 * @param  x    X position of tile image to retrieve.
	 * @param  y    Y position of tile image to retrieve.
	 * @param  zoom Zoom level of tile image to retrieve.
	 * @return      Image for the specified tile if it can be retrieved or null
	 *              otherwise.
	 */
	public BufferedImage getTileImage(int x, int y, int zoom) {

		// First try the cache.
		BufferedImage tileImage = tileCache.getTile(x, y, zoom);
		if (tileImage != null) {
			LOGGER.debug("Found tile image in cache: zoom={} x={} y={}", zoom, x, y);
			return tileImage;
		}

		// If the image is not found in the cache, try the local tile store.
		tileImage = loadFromStore(x, y, zoom);
		if (tileImage != null) {
			return tileImage;
		}

		// If we still didn't find it and we are allowed to go online, try to get
		// it from Open Street Map.
		if (mapState.getOnline().get()) {

			tileImage = fetchTileImage(x, y, zoom);
		}

		if (tileImage != null) {
			// We got a tile image so cache it locally and write it to the local
			// store.
			tileCache.cacheTile(x, y, zoom, tileImage);
			writeToStore(x, y, zoom, tileImage);
		}

		return tileImage;
	}

	@Override
	public void terminate() {

		// Close and flush our map tile cache.
		tileCache.close();

		LOGGER.info("Map service terminated");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Tries to load a tile image from the local file store if one is configured.
	 * 
	 * @param  x    X position of tile image to retrieve.
	 * @param  y    Y position of tile image to retrieve.
	 * @param  zoom Zoom level of tile image to retrieve.
	 * @return      Tile image from local file store if store is configured and
	 *              has the image; false otherwise.
	 */
	private BufferedImage loadFromStore(int x, int y, int zoom) {

		if (tileStoreBasePath == null) {
			// No local store so we can't load anything.
			return null;
		}

		BufferedImage image = null;
		File storePath = createStorePath(x, y, zoom);
		if (storePath.exists()) {
			try {
				image = ImageIO.read(storePath);
			} catch (IOException e) {
				LOGGER.warn("Failed to read existing tile image from store.", e);
			}
		}

		if ((image != null) && LOGGER.isDebugEnabled()) {
			LOGGER.debug("Read tile image from local store: zoom={} x={} y={}", zoom, x, y);
		}

		return image;
	}

	/**
	 * Writes the tile image to the local file store if one is configured.
	 * 
	 * @param x     X position of tile image to write.
	 * @param y     Y position of tile image to write.
	 * @param zoom  Zoom level of tile image to write.
	 * @param image Image to write to store.
	 */
	private void writeToStore(int x, int y, int zoom, BufferedImage image) {

		if (tileStoreBasePath == null) {
			// No local store so we can't write anything.
			return;
		}

		File storePath = createStorePath(x, y, zoom);
		File parentPath = storePath.getParentFile();
		if (!parentPath.exists()) {
			try {
				Files.createDirectories(parentPath.toPath());
			} catch (IOException e) {
				LOGGER.warn("Failed to create tile parent directory in store.", e);
				return;
			}
		}

		try {
			ImageIO.write(image, "png", storePath);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Wrote tile image to local store: zoom={} x={} y={}", zoom, x, y);
			}
		} catch (IOException e) {
			LOGGER.warn("Failed to write tile image to local store.", e);
		}
	}

	private File createStorePath(int x, int y, int zoom) {
		String tilePath = File.separator + zoom + //
				File.separator + x + //
				File.separator + "tile_" + y + ".png";
		return new File(tileStoreBasePath, tilePath);
	}

	/**
	 * Fetches the specified tile image from the Open Street Map servers.
	 * 
	 * @param  x    X coordinate of the tile.
	 * @param  y    Y coordinate of the tile.
	 * @param  zoom Zoom for the tile (map zoom - not OSM zoom).
	 * @return      Image representing the given tile or null if it failed to
	 *              retrieve.
	 */
	private BufferedImage fetchTileImage(int x, int y, int zoom) {

		// Convert zoom to what Open Street Map expects.
		int osmZoom = OSM_MAX_ZOOM - zoom;
		String osmTileUrl = OSM_BASE_URL + "/" + osmZoom + "/" + x + "/" + y + ".png";

		BufferedImage image = null;
		try {

			URL imageURL = new URL(osmTileUrl);
			URLConnection connection = imageURL.openConnection();
			connection.setRequestProperty("User-Agent", USER_AGENT);

			InputStream inputStream = connection.getInputStream();

			ByteArrayOutputStream retrievedBytes = new ByteArrayOutputStream();
			byte[] retrieveBuffer = new byte[256];
			while (true) {
				int n = inputStream.read(retrieveBuffer);
				if (n == -1)
					break;
				retrievedBytes.write(retrieveBuffer, 0, n);
			}

			image = ImageIO.read(new ByteArrayInputStream(retrievedBytes.toByteArray()));
		} catch (Exception e) {
			LOGGER.warn("Failed to retrieve OSM tile image.", e);
		}

		if ((image != null) && LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetched tile image from OSM: zoom={} x={} y={}", zoom, x, y);
		}

		return image;
	}

	/**
	 * Initializes the service and returns the initial state.
	 * 
	 * @param  runMode Run mode for the operating system.
	 * @param  profile Profile to use to configure the service.
	 * @return         Configured map state object.
	 */
	private MapState initialize(Profile profile) {

		mapState = new InternalMapState(profile.getMapOnline());
		return mapState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the map service along with its initial state set
	 * from the supplied profile.
	 * 
	 * @param  runMode Run mode for the operating system.
	 * @param  profile Profile to use for configuring the map service.
	 * @return         Map service instance and its initial state object.
	 */
	public static Pair<MapService, MapState> createService(Profile profile) {

		MapService service = new MapService(profile.getTileStorePath());
		MapState state = service.initialize(profile);

		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the map state.
	 */
	private static class InternalMapState implements MapState {

		private BooleanProperty online;

		// ----------------------------------------------------------------------------------------
		// Constructors
		// ----------------------------------------------------------------------------------------

		private InternalMapState(boolean online) {
			this.online = new SimpleBooleanProperty(online);
		}

		// ----------------------------------------------------------------------------------------
		// Map state methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public BooleanProperty getOnline() {
			return online;
		}
	}
}
