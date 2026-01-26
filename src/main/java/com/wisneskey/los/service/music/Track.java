package com.wisneskey.los.service.music;

/**
 * Interface representing a track that can be played by the Music service.
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
public interface Track {

	/**
	 * Returns the unique identifier for the track.
	 * 
	 * @return String representing a unique identifier for the track.
	 */
	String getTrackId();
	
	/**
	 * Return the artist of the track.
	 * 
	 * @return String containing the name of the artist.
	 */
	String getArtist();
	
	/**
	 * Return the title of the track.
	 * 
	 * @return String containing the title of the track.
	 */
	String getTitle();
}