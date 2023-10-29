package com.wisneskey.los.util.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.jxmapviewer.viewer.AbstractTileFactory;
import org.jxmapviewer.viewer.Tile;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile factory which tries to load tiles from a local file system cache
 * before downloading them.
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
public class LocalFileCacheTileFactory extends AbstractTileFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileCacheTileFactory.class);

	private File basePath;

	// ----------------------------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a factory with a designated base path.
	 * 
	 * @param tileCacheBasePath
	 * @param info
	 */
	public LocalFileCacheTileFactory(String tileCacheBasePath, TileFactoryInfo info) {
		super(info);

		basePath = new File(tileCacheBasePath);
	}

	@Override
	public Tile getTile(int x, int y, int zoom) {

		Tile tile;

		String tileLocation = zoom + "/" + x + "/" + y + "/tile.png";
		File tilePath = new File(basePath, tileLocation);
		if (tilePath.exists()) {

			LOGGER.debug("Found tile locally: zoom={} x={} y={}", zoom, x, y);
			BufferedImage image = loadTileImage(tilePath);
			tile = new LocalTile(x, y, zoom, image);

		} else {

			LOGGER.debug("Retreiving and saving tile locally: zoom={} x={} y={}", zoom, x, y);
			tile = super.getTile(x, y, zoom);
			saveTileImage(tilePath, tile);
		}

		return tile;
	}

	private BufferedImage loadTileImage(File tilePath) {

		try {
			return ImageIO.read(tilePath);
		} catch (IOException e) {
			LOGGER.error("Failed to read local tile.", e);
		}

		return null;
	}

	private void saveTileImage(File tilePath, Tile tile) {

		BufferedImage image = tile != null ? tile.getImage() : null;
		if (image == null) {
			return;
		}

		try {
			File tileDir = tilePath.getParentFile();
			if (!tileDir.exists()) {
				Files.createDirectories(tileDir.toPath());
			}

			ImageIO.write(tile.getImage(), "png", tilePath);
		} catch (IOException e) {
			LOGGER.error("Failed to save local tile.", e);
		}
	}

	private static class LocalTile extends Tile {

		private BufferedImage image;

		private LocalTile(int x, int y, int zoom, BufferedImage image) {
			super(x, y, zoom);
			this.image = image;
		}

		@Override
		public BufferedImage getImage() {
			return image;
		}

		@Override
		public synchronized boolean isLoaded() {
			return true;
		}
	}
}
