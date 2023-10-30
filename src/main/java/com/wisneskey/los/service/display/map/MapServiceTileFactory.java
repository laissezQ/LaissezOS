package com.wisneskey.los.service.display.map;

import java.awt.image.BufferedImage;

import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.map.MapService;

/**
 * A tile factory which loads tiles from the Map service.
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
public class MapServiceTileFactory extends TileFactory {

	/**
	 * Map service for getting tile images from.
	 */
	private MapService mapService;

	// ----------------------------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a factory with an optional designated base path for persistent
	 * local storage.
	 * 
	 * @param tileCacheConfigName Name of the alias to use for the tile cache
	 *                              configuration.
	 * @param info                Tile factory configuration.
	 */
	public MapServiceTileFactory() {
		super(new OSMTileFactoryInfo());
		this.mapService = Kernel.kernel().getService(ServiceId.MAP);
	}

	// ----------------------------------------------------------------------------------------
	// TileFactory methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public Tile getTile(int x, int y, int zoom) {
		return new MapServiceTile(x, y, zoom, mapService.getTileImage(x, y, zoom));
	}

	@Override
	public void dispose() {
		// Nothing to do to dispose of factory. The Map service itself will clean up
		// its cache when it is shut down.
	}

	@Override
	protected void startLoading(Tile tile) {
		// Tell the map service
		BufferedImage image = mapService.getTileImage(tile.getX(), tile.getY(), tile.getZoom());

		if (tile instanceof MapServiceTile) {
			((MapServiceTile) tile).setImage(image);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private static class MapServiceTile extends Tile {

		private BufferedImage image;

		private MapServiceTile(int x, int y, int zoom, BufferedImage image) {
			super(x, y, zoom);
			this.image = image;
		}

		// ----------------------------------------------------------------------------------------
		// Tile methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public BufferedImage getImage() {
			return image;
		}

		@Override
		public synchronized boolean isLoaded() {
			return image != null;
		}

		// ----------------------------------------------------------------------------------------
		// Public methods.
		// ----------------------------------------------------------------------------------------

		public void setImage(BufferedImage image) {
			this.image = image;
		}
	}
}
