package com.wisneskey.los.service.script.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.music.MusicService;

/**
 * Script command to play a music track.
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

	private String trackId;
	private String trackName;
	
	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
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

	@Override public void perform() {

		MusicService service = Kernel.kernel().getService(ServiceId.MUSIC);
		
		// If we have a track id, just use it but if not we try to see if we have a track name.
		if( trackId == null ) {
			
			if( trackName == null ) {
				LOGGER.warn("No track id or track name give; nothing to play.");
				return;
			}
			
			// Get the track id for the track name.
			trackId = service.getTrackIdForTitle(trackName);
			if( trackId == null) {
				LOGGER.warn("No track found with the given name.");
				return;
			}
		}

		// Play the track.
		service.playTrack(trackId);
	}
}
