package com.wisneskey.los.service.map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

/**
 * Cache for map tile images that uses an Ehcache cache implementation under the
 * covers to manage different persistence configurations. This enables the chair
 * to build up a file system cache of tiles that can be used when the chair is
 * offline (e.g. during a parade).
 *
 * Copyright (C) 2026 Paul Wisneskey
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
public class MapTileCache {

	/**
	 * Name of the map tile cache.
	 */
	private static final String TILE_CACHE_NAME = "osmTileCache";

	/**
	 * Number of tiles to keep cached in the heap (first level cache).
	 */
	private static final int CACHE_HEAP_ENTRIES = 256;

	/**
	 * Number of megabytes to use for secondary level tile cache in off heap memory. 
	 */
	private static final int CACHE_OFF_HEAP_MEMORY = 64;

	/**
	 * Unit of allocation for off heap memory for secondary tile cache.
	 */
	private static final MemoryUnit CACHE_OFF_HEAP_MEMORY_UNIT = MemoryUnit.MB;
	
	/**
	 * Cache manager for the tile cache.
	 */
	private CacheManager cacheManager;

	/**
	 * Tile cache to use for map tile images
	 */
	private Cache<TileCacheKey, CachedImage> tileCache;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static creation method.
	 * 
	 * @param cacheManager Cache manager for the cache.
	 * @param tileCache    Tile cache created based on configuration.
	 */
	private MapTileCache(CacheManager cacheManager, Cache<TileCacheKey, CachedImage> tileCache) {
		this.cacheManager = cacheManager;
		this.tileCache = tileCache;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the image for the tile at the specified zoom and coordinates if it
	 * exists in the cache.
	 * 
	 * @param  x    X position of the tile.
	 * @param  y    Y position of the tile.
	 * @param  zoom Zoom level for the tile.
	 * @return      Image for the tile for the specified position and zoom if it
	 *              exists in the cache or null otherwise.
	 */
	public BufferedImage getTile(int x, int y, int zoom) {

		CachedImage cachedImage = tileCache.get(new TileCacheKey(x, y, zoom));
		return cachedImage == null ? null : cachedImage.getImage();
	}

	/**
	 * Puts the specified tile in the cache.
	 * 
	 * @param tile Tile to place in the cache.
	 */
	public void cacheTile(int x, int y, int zoom, BufferedImage image) {
		CachedImage cachedImage = new CachedImage();
		cachedImage.setImage(image);

		tileCache.put(new TileCacheKey(x, y, zoom), cachedImage);
	}

	/**
	 * Close and dispose of the cache.
	 */
	public void close() {
		cacheManager.close();
	}

	// ----------------------------------------------------------------------------------------
	// Static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Create a map tile cache instance using a designated configuration loaded
	 * from the configuration XML.
	 * 
	 * @param  tileStorePath Directory for the local map tile store.
	 * @return             Map tile cache created with the specified
	 *                     configuration.
	 */
	public static MapTileCache createCache() {
		
		CacheConfiguration<TileCacheKey, CachedImage> cacheConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(TileCacheKey.class, CachedImage.class,
						ResourcePoolsBuilder.newResourcePoolsBuilder() //
								.heap(CACHE_HEAP_ENTRIES, EntryUnit.ENTRIES) //
								.offheap(CACHE_OFF_HEAP_MEMORY, CACHE_OFF_HEAP_MEMORY_UNIT)) //
				.build();

		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder() //
				.withCache(TILE_CACHE_NAME, cacheConfig) //
				.build();
		cacheManager.init();

		Cache<TileCacheKey, CachedImage> tileCache = cacheManager.getCache(TILE_CACHE_NAME, TileCacheKey.class,
				CachedImage.class);

		return new MapTileCache(cacheManager, tileCache);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Class representing the key to the tile cache.
	 */
	public static class TileCacheKey implements Serializable {

		/**
		 * Version id for serialization.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * X position of the tile.
		 */
		private int x;

		/**
		 * Y position of the tile.
		 */
		private int y;

		/**
		 * Zoom level of the tile.
		 */
		private int zoom;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		public TileCacheKey(int x, int y, int zoom) {

			this.x = x;
			this.y = y;
			this.zoom = zoom;
		}

		// ----------------------------------------------------------------------------------------
		// Object methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public int hashCode() {
			return Objects.hash(x, y, zoom);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TileCacheKey other = (TileCacheKey) obj;
			return x == other.x && y == other.y && zoom == other.zoom;
		}
	}

	/**
	 * Class representing a cached map tile image that supports image
	 * serialization as a PNG image file.
	 */
	public static class CachedImage implements Serializable {

		/**
		 * Version id for serialization.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Actual tile image.
		 */
		private BufferedImage image;

		// ----------------------------------------------------------------------------------------
		// Public methods.
		// ----------------------------------------------------------------------------------------

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		// ----------------------------------------------------------------------------------------
		// Serializable methods.
		// ----------------------------------------------------------------------------------------

		/**
		 * Write the cached tile image to an output stream as a PNG.
		 * 
		 * @param  out         Output stream to write tile image to.
		 * @throws IOException If image fails to write.
		 */
		private void writeObject(ObjectOutputStream out) throws IOException {
			ImageIO.write(image, "png", out);
		}

		/**
		 * Read a cached tile image from an input stream.
		 * 
		 * @param  in                     Input stream to read the tile image from.
		 * @throws IOException            If the image fails to read.
		 * @throws ClassNotFoundException If the image object can not be created.
		 */
		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			this.image = ImageIO.read(in);
		}
	}
}
