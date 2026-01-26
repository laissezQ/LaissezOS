package com.wisneskey.los.state;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * Music service.
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
public interface MusicState extends State {

	/**
	 * Name of the currently selected playlist.
	 * 
	 * @return Name of the currently selected playlist.
	 */
	StringProperty currentPlaylistName();

	/**
	 * Id of the currently playing track (or null if nothing currently playing).
	 * 
	 * @return Id of currently playing track or null if no track playing.
	 */
	ReadOnlyStringProperty currentTrackId();

	/**
	 * Artist for the currently playing track (or null if nothing currently
	 * playing).
	 * 
	 * @return Artist for currently playing track or null if no track playing.
	 */
	ReadOnlyStringProperty currentTrackArtist();

	/**
	 * Name of the currently playing track (or null if nothing currently playing).
	 * 
	 * @return Name of currently playing track or null if no track playing.
	 */
	ReadOnlyStringProperty currentTrackName();

	/**
	 * Property indicating if service should auto-play another track when the
	 * current one ends.
	 * 
	 * @return Enumerated value representing the current state of the player.
	 */
	BooleanProperty autoPlay();

	/**
	 * Volume of the music playback.
	 * 
	 * @return Volume of chair from 0 - 11.
	 */
	IntegerProperty volume();
}