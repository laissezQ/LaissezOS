package com.wisneskey.los.service.script.command;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.music.MusicService;
import com.wisneskey.los.service.music.Track;

/**
 * Script command to play a music track from a designated playlist.
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
public class PlayTrack extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayTrack.class);

	private String playlistName;
	private String trackName;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		MusicService service = Kernel.kernel().getService(ServiceId.MUSIC);

		if (playlistName == null) {
			LOGGER.warn("Playlist name not set; nothing to play.");
			return;
		}

		if (trackName == null) {
			LOGGER.warn("Track name not set; nothing to play.");
			return;
		}

		// Try to get the tracks for the named playlist.
		List<Track> playlistTracks = service.getPlaylistTracks(playlistName);

		// Look for a track with the specified name.
		Track track = playlistTracks.stream().filter(t -> Objects.equals(trackName, t.getTitle())).findFirst().orElse(null);

		if (track != null) {

			// Play the track.
			LOGGER.debug("Playing track: id={} artist={} title={}", track.getTrackId(), track.getArtist(), track.getTitle());
			service.playTrack(track.getTrackId());
		} else {
			
			LOGGER.warn("No track found with the given name; nothing to play.");
		}
	}
}