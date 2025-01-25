package com.wisneskey.los.service.music;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.MusicState;

import javafx.util.Pair;

/**
 * Service for playing music.
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
public class MusicService extends AbstractService<MusicState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MusicService.class);

	/**
	 * Extension a file must end with to be considered a track.
	 */
	private static final String MP3_FILE_EXTENSION = ".mp3";

	/**
	 * Object for the state of the music service.
	 */
	private InternalMusicState musicState;

	/**
	 * Map of track id's to their tracks.
	 */
	private Map<String, InternalTrack> trackMap = new HashMap<>();

	/**
	 * List of all available tracks. Starts as empty list and is replaced after
	 * tracks are indexed (if indexing does not fail).
	 */
	private List<Track> trackList = Collections.emptyList();

	/**
	 * Atomic boolean used to monitor if something is currently playing.
	 */
	private AtomicBoolean playing = new AtomicBoolean(false);

	/**
	 * Thread being used to run the external player.
	 */
	private PlayerThread playerThread;

	/**
	 * Lock object used to ensure only one caller at a time is working with the
	 * player thread.
	 */
	private Object playerLock = new Object();

	/**
	 * Command to use to start the external player.
	 */
	private String playerCommand;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private MusicService() {
		super(ServiceId.MUSIC);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public List<Track> getTracks() {
		return trackList;
	}

	/**
	 * Play a track.
	 * 
	 * @param trackId Id of the track to start playing.
	 */
	public void playTrack(String trackId) {

		InternalTrack track = trackMap.get(trackId);
		if (track == null) {
			LOGGER.error("Attempt to play an unknown track with id {}", trackId);
			return;
		}

		synchronized (playerLock) {

			if (playing.get()) {
				LOGGER.warn("Attempt to play a track with another track already playing.");
			}

			playing.set(true);

			// Do the actual launching of the external player in its own thread so
			// that
			// we do not block the caller.
			PlayerThread playerThread = new PlayerThread(track.getTrackPath());
			playerThread.start();
		}
	}

	/**
	 * Stop a track from playing if one is playing.
	 */
	public void stopPlaying() {
		synchronized (playerLock) {
			if (playing.get()) {
				playerThread.killProcess();
			}
		}
	}

	/**
	 * Look up a track id for the track with a specified title.
	 * 
	 * @param  title Title of the track to find the id for.
	 * @return       Id of the track with the title or null if not found.
	 */
	public String getTrackIdForTitle(String title) {

		for (Track track : trackList) {
			if (title.equals(track.getTitle())) {
				return track.getTrackId();
			}
		}

		return null;
	}
	
	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public MusicState getState() {
		return musicState;
	}

	@Override
	public void terminate() {
		LOGGER.trace("Music service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method invoked by the player thread to report its external process has
	 * completed.
	 */
	private void reportProcessCompletion() {

		synchronized (playerLock) {
			playing.set(false);
			playerThread = null;
		}
	}

	/**
	 * Initializes the music services and returns its initial state.
	 * 
	 * @param  profile Profile with initial audio settings.
	 * @return         Configured state object for the service.
	 */
	private MusicState initialize(Profile profile) {

		LOGGER.info("Initializing music service...");

		// We do not turn on the amplifier as the audio service does that. That
		// service handles the sounds effects so will get used before music is
		// played.

		// Make sure the external player is defined and exists.
		if( profile.getPlayerCommand() == null) {
			throw new LaissezException("External MP3 player not set in profile.");
		}
		
		File externalPlayer = new File(profile.getPlayerCommand());
		if( !externalPlayer.exists()) {
			throw new LaissezException("External MP3 player not found.");
		}
		
		if( ! externalPlayer.canExecute()) {
			throw new LaissezException("External MP3 player not executable.");
		}
		
		// Command to start external player appears to be valid so save it.
		this.playerCommand = profile.getPlayerCommand();
		
		// Look at the profile for our music directory and scan all MP3 files in
		// that directory.
		indexTracks(profile.getMusicDir());

		musicState = new InternalMusicState();
		return musicState;
	}

	/**
	 * Indexes all MP3's in the music directory as tracks.
	 * 
	 * @param musicDir Directory to index MP3's in.
	 */
	private void indexTracks(String musicDir) {

		if (musicDir == null) {
			LOGGER.warn("Music directory not set; not indexing tracks.");
			return;
		}

		// Expand any leading tilda to be the user's home directory. This assumes
		// that the
		// music directory is not under another user.
		musicDir = musicDir.replaceFirst("^~", System.getProperty("user.home"));
		LOGGER.debug("Attempting to scan {} for tracks...", musicDir);

		File scanDirectory = new File(musicDir);
		if (!scanDirectory.exists()) {
			LOGGER.warn("Music directory not found; not indexing tracks.");
			return;
		}

		try {
			int trackIndexNumber = 0;
			for (File trackFile : scanDirectory.listFiles(new Mp3FileFilter())) {

				String trackId = "Track:" + trackIndexNumber++;
				String trackPath = trackFile.getCanonicalPath();

				InternalTrack track = new InternalTrack(trackId, trackPath);
				boolean success = readTrackData(track);

				if (success) {
					trackMap.put(trackId, track);
				}

			}
		} catch (IOException e) {
			LOGGER.error("Failed to index music tracks.", e);
		}

		trackList = Collections.unmodifiableList(new ArrayList<>(trackMap.values()));
		LOGGER.info("Indexed {} tracks.", trackList.size());
	}

	private boolean readTrackData(InternalTrack track) {

		String trackPath = track.getTrackPath();
		LOGGER.info("Attempting to read track data: {}", trackPath);
		
		try {
			Mp3File mp3File = new Mp3File(trackPath);

			if (mp3File.hasId3v1Tag()) {

				ID3v1 id3v1Tag = mp3File.getId3v1Tag();
				track.setArtist(id3v1Tag.getArtist());
				track.setTitle(id3v1Tag.getTitle());

			} else if (mp3File.hasId3v2Tag()) {

				ID3v2 id3v2Tag = mp3File.getId3v2Tag();
				track.setArtist(id3v2Tag.getArtist());
				track.setTitle(id3v2Tag.getTitle());

			} else {
				LOGGER.warn("Did not find expected Id3 tag: {}", trackPath);
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.warn("Failed to read track data: " + trackPath, e);
			return false;
		}
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the music service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile with initial music settings.
	 * @return         Music service instance and its initial state object.
	 */
	public static Pair<MusicService, MusicState> createService(Profile profile) {

		MusicService service = new MusicService();
		MusicState state = service.initialize(profile);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private class PlayerThread extends Thread {

		private String trackPath;
		private Process playerProcess;

		private PlayerThread(String trackPath) {
			this.trackPath = trackPath;
			
			setName("ExternalMp3PlayerThread");
		}

		@Override
		public void run() {

			try {
				LOGGER.info("Attempting to play track: {}", trackPath);
				ProcessBuilder processBuilder = new ProcessBuilder(playerCommand, "-q", trackPath);
				playerProcess = processBuilder.start();

				// Wait for the process to complete.
				int returnCode = playerProcess.waitFor();

				LOGGER.info("External player completed: rc={}", returnCode);

				if (returnCode != 0) {
					LOGGER.warn("MP3 player exited with non-zer return of {}", returnCode);
				}
			} catch (Exception e) {
				LOGGER.warn("Exception during playing of MP3.", e);
			}

			// Report completion no matter what.
			reportProcessCompletion();
		}

		public void killProcess() {
			playerProcess.destroy();
		}
	}

	/**
	 * Internal state object for the Music service.
	 */
	private static class InternalMusicState implements MusicState {

	}

	/**
	 * Internal representation of a music track that can be played by the Music
	 * service.
	 */
	private static class InternalTrack implements Track {

		private String trackId;
		private String trackPath;

		private String artist;
		private String title;

		// ----------------------------------------------------------------------------------------
		// Constructors
		// ----------------------------------------------------------------------------------------

		private InternalTrack(String trackId, String trackPath) {
			this.trackId = trackId;
			this.trackPath = trackPath;
		}

		// ----------------------------------------------------------------------------------------
		// Track methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public String getTrackId() {
			return trackId;
		}

		@Override
		public String getArtist() {
			return artist;
		}

		@Override
		public String getTitle() {
			return title;
		}

		// ----------------------------------------------------------------------------------------
		// Internal methods.
		// ----------------------------------------------------------------------------------------

		private String getTrackPath() {
			return trackPath;
		}

		private void setArtist(String artist) {
			this.artist = artist;
		}

		private void setTitle(String title) {
			this.title = title;
		}
	}

	/**
	 * File name filter for selecting only MP3's as tracks.
	 */
	private static class Mp3FileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(MP3_FILE_EXTENSION);
		}

	}
}