package com.wisneskey.los.service.music;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.MusicState;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 * Service for playing music.
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
public class MusicService extends AbstractService<MusicState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MusicService.class);

	/**
	 * Minimum supported volume.
	 */
	private static final double MIN_VOLUME = 0.0d;

	/**
	 * Maximum supported volume.
	 */
	private static final double MAX_VOLUME = 11.0d;

	/**
	 * Maximum allowed scaled factor for mpg123.
	 */
	private static final double MAX_SCALE_FACTOR = 32768;

	/**
	 * Base of exponential use for emphasizing lower volume scale factors.
	 */
	private static final double SCALE_FACTOR_BASE = 1.6d;

	/**
	 * Extension a file must end with to be considered a track.
	 */
	private static final String MP3_FILE_EXTENSION = ".mp3";

	/**
	 * Object for the state of the music service.
	 */
	private InternalMusicState musicState;

	/**
	 * Map of playlist name to the tracks they contain.
	 */
	private Map<String, List<InternalTrack>> playlistMap;

	/**
	 * Map of track ids to tracks for all tracks in all playlists.
	 */
	private Map<String, InternalTrack> trackMap;

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

	/**
	 * Random generator for selecting next track.
	 */
	private Random random = new Random();

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

	/**
	 * Returns the list of playlists available.
	 * 
	 * @return List of the names for the available playlists.
	 */
	public List<String> getPlaylists() {

		return new ArrayList<>(playlistMap.keySet());
	}

	/**
	 * Returns the tracks for a given playlist.
	 * 
	 * @param  playlistName Name of the playlist to get the tracks for.
	 * 
	 * @return              List of tracks for the named playlist or null if named
	 *                      playlist does not exist.
	 */
	public List<Track> getPlaylistTracks(String playlistName) {

		List<InternalTrack> playlistTracks = playlistMap.get(playlistName);
		return playlistTracks == null ? null : Collections.unmodifiableList(playlistTracks);
	}

	/**
	 * Play a track from the current playlist.
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
				// If a track is already playing, stop it so that we can start another
				// one.
				LOGGER.info("Stopping current track that is playing...");
				playerThread.killProcess(false);
			}

			playing.set(true);
			musicState.currentTrackId.set(track.getTrackId());
			musicState.currentTrackArtist.set(track.getArtist());
			musicState.currentTrackName.set(track.getTitle());

			if (!isBooting()) {
				Kernel.kernel().message("Playing '" + track.getTitle() + "'\n");
			}

			// Get the scale factor to use for setting the volume of the MP3 playback.
			int scaleFactor = getScaleFactor(musicState.volume().get());

			// Do the actual launching of the external player in its own thread so
			// that we do not block the caller.
			playerThread = new PlayerThread(track.getTrackPath(), scaleFactor);
			playerThread.start();
		}
	}

	/**
	 * Move on to another track selected randomly if playing mode is or stops the
	 * track if single playback mode.
	 */
	public void nextTrack() {
		synchronized (playerLock) {

			if (playing.get()) {
				// Stop the current track. If auto play was enabled, a new track will be
				// started.
				playerThread.killProcess(true);
			} else {

				// Just pick a track to play at random since nothing was playing.
				String nextTrackId = pickNextTrackAtRandom(null);
				if (nextTrackId != null) {
					playTrack(nextTrackId);
				}
			}
		}
	}

	/**
	 * Stop a track from playing if one is playing.
	 */
	public void stopPlaying() {

		synchronized (playerLock) {
			if (playing.get()) {
				playerThread.killProcess(false);
				playing.set(false);
				LOGGER.info("Player thread process killed.");
			}

			musicState.currentTrackId.set(null);
			musicState.currentTrackArtist.set(null);
			musicState.currentTrackName.set(null);
		}
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
	 * Returns the scale factor for playing a MP3 track at the configured volume
	 * using mpg123's -f parameter.
	 * 
	 * @param  volume Volume to calculate gain adjustment for.
	 * @return        Gain adjustment to be applied to sound clip to reduce its
	 *                volume.
	 */
	private int getScaleFactor(int volume) {

		double volumeDouble = volume;

		// Make sure volume is in allowed range.
		volumeDouble = Math.max(MIN_VOLUME, volumeDouble);
		volumeDouble = Math.min(volumeDouble, MAX_VOLUME);

		// Use a exponential scale to emphasize scale factor at lower volumes.
		volumeDouble = Math.pow(SCALE_FACTOR_BASE, volumeDouble);
		double scaledMax = Math.pow(SCALE_FACTOR_BASE, MAX_VOLUME);

		return (int) Math.round(MAX_SCALE_FACTOR * (volumeDouble / scaledMax));
	}

	/**
	 * Method invoked by the player thread to report its external process has
	 * completed.
	 */
	private void reportProcessCompletion() {

		LOGGER.info("Player thread reporting process completion.");
		playing.set(false);
		playerThread = null;

		String previousTrackId = musicState.currentTrackId.get();

		musicState.currentTrackId.set(null);
		musicState.currentTrackArtist.set(null);
		musicState.currentTrackName.set(null);

		// If shuffle play is enabled, we need to play another track at random.
		if (musicState.autoPlay().getValue().booleanValue()) {

			String nextTrackId = pickNextTrackAtRandom(previousTrackId);
			if (nextTrackId != null) {
				playTrack(nextTrackId);
			}
		}
	}

	/**
	 * Picks the next track to play from the current playlist avoiding repeats if
	 * possible.
	 * 
	 * @param  lastTrackId Id of the last track played.
	 * @return             Id of the next track to play.
	 */
	private String pickNextTrackAtRandom(String lastTrackId) {

		String nextTrackId = lastTrackId;
		String currentPlaylist = musicState.currentPlaylistName.get();

		if (currentPlaylist != null) {
			List<InternalTrack> tracks = playlistMap.get(currentPlaylist);
			if (tracks.size() > 1) {

				while (Objects.equals(nextTrackId, lastTrackId)) {
					int trackIndex = random.nextInt(tracks.size());
					nextTrackId = tracks.get(trackIndex).getTrackId();
				}
			}
		}

		return nextTrackId;
	}

	/**
	 * Initializes the music services and returns its initial state.
	 * 
	 * @param  profile Profile with initial audio settings.
	 * @return         Configured state object for the service.
	 */
	private MusicState initialize(Profile profile) {

		LOGGER.info("Initializing music service...");

		// Make sure the external player is defined and exists.
		if (profile.getPlayerCommand() == null) {
			throw new LaissezException("External MP3 player not set in profile.");
		}

		File externalPlayer = new File(profile.getPlayerCommand());
		if (!externalPlayer.exists()) {
			throw new LaissezException("External MP3 player not found.");
		}

		if (!externalPlayer.canExecute()) {
			throw new LaissezException("External MP3 player not executable.");
		}

		// Command to start external player appears to be valid so save it.
		this.playerCommand = profile.getPlayerCommand();

		// Check and load all playlists.
		loadPlaylists(profile.getPlaylists());

		musicState = new InternalMusicState(profile.getMusicVolume());

		if (!playlistMap.isEmpty()) {
			String playlistName = playlistMap.keySet().iterator().next();
			musicState.currentPlaylistName.set(playlistName);
		}

		musicState.currentTrackId.set(null);
		musicState.currentTrackArtist.set(null);
		musicState.currentTrackName.set(null);

		return musicState;
	}

	/**
	 * Validates and loads the configured playlists in the profile and sets up the
	 * internal playlist structures.
	 */
	private void loadPlaylists(Map<String, String> profilePlaylists) {

		if ((profilePlaylists == null) || profilePlaylists.isEmpty()) {

			// No playlists configured.
			LOGGER.warn("No playlists configured in profile.");
			this.playlistMap = Collections.emptyMap();
			this.trackMap = Collections.emptyMap();
			return;
		}

		playlistMap = new HashMap<>(profilePlaylists.size());
		trackMap = new HashMap<>();

		// Now index the tracks for each playlist
		for (Map.Entry<String, String> playlistEntry : profilePlaylists.entrySet()) {

			String playlistName = playlistEntry.getKey();
			String playlistDirectory = playlistEntry.getValue();

			List<InternalTrack> playlistTracks = indexPlaylistTracks(playlistName, playlistDirectory);
			if (playlistTracks.isEmpty()) {
				LOGGER.warn("Playlist {} has no tracks; ignoring it.", playlistName);
			} else {

				// Sort the tracks alphabetically by title.
				Collections.sort(playlistTracks, Comparator.comparing(InternalTrack::getTitle));

				// Register the playlist.
				playlistMap.put(playlistName, playlistTracks);

				// Register all of its songs in the master track map.
				playlistTracks.stream().forEach(t -> trackMap.put(t.getTrackId(), t));
			}
		}
	}

	/**
	 * Indexes all MP3's in the music directory as tracks.
	 * 
	 * @param musicDir Directory to index MP3's in.
	 */
	private List<InternalTrack> indexPlaylistTracks(String playlistName, String playlistDirectory) {

		// Expand any leading tilda to be the user's home directory. This assumes
		// that the playlist directory is not under another user.
		playlistDirectory = playlistDirectory.replaceFirst("^~", System.getProperty("user.home"));
		LOGGER.debug("Attempting to scan {} for tracks...", playlistDirectory);

		File scanDirectory = new File(playlistDirectory);
		if (!scanDirectory.exists()) {
			throw new LaissezException("Music directory " + playlistDirectory + " not found; not indexing tracks.");
		}

		List<InternalTrack> playlistTracks = new ArrayList<>();
		try {
			int trackIndexNumber = 0;
			for (File trackFile : scanDirectory.listFiles(new Mp3FileFilter())) {

				String trackId = "Track:" + playlistName + ":" + trackIndexNumber++;
				String trackPath = trackFile.getCanonicalPath();

				InternalTrack track = new InternalTrack(trackId, trackPath);
				boolean success = readTrackData(track);

				if (success) {
					playlistTracks.add(track);
				} else {
					LOGGER.warn("Failed to read data for track: {}", trackPath);
				}

			}
		} catch (IOException e) {
			throw new LaissezException("Failed to index music tracks.", e);
		}

		LOGGER.info("Indexed {} tracks.", playlistTracks.size());
		return playlistTracks;
	}

	/**
	 * Reads the header data for a track's mp3 to get the artist and title of the
	 * track.
	 * 
	 * @param  track Track to read the header information for.
	 * @return       True if track information was read or false otherwise.
	 */
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
		private int scaleFactor;

		private Process playerProcess;
		private boolean notifyWhenDone = true;

		private PlayerThread(String trackPath, int scaleFactor) {
			this.trackPath = trackPath;
			this.scaleFactor = scaleFactor;

			setName("ExternalMp3PlayerThread");
		}

		@Override
		public void run() {

			try {
				LOGGER.info("Attempting to play track: scaleFactor={} path={}", scaleFactor, trackPath);
				ProcessBuilder processBuilder = new ProcessBuilder(//
						playerCommand, //
						//"-o", //
						//"alsa:hw:2,0", //
						"-q", //
						"-f", //
						String.valueOf(scaleFactor), //
						trackPath);
				playerProcess = processBuilder.start();

				// Wait for the process to complete.
				int returnCode = playerProcess.waitFor();

				LOGGER.info("External player completed: rc={}", returnCode);

				if (returnCode != 0) {
					LOGGER.warn("MP3 player exited with non-zero return of {}", returnCode);
				}
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted during MP3 playback.");
				this.interrupt();
			} catch (Exception e) {
				LOGGER.warn("Exception during playing of MP3.", e);
			}

			// Report completion no matter what.
			if (notifyWhenDone) {
				reportProcessCompletion();
			}
		}

		public void killProcess(boolean notifyWhenDone) {
			this.notifyWhenDone = notifyWhenDone;
			playerProcess.destroy();
			try {
				playerProcess.waitFor();
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}

	/**
	 * Internal state object for the Music service.
	 */
	private static class InternalMusicState implements MusicState {

		private IntegerProperty volume;
		private StringProperty currentPlaylistName = new SimpleStringProperty();
		private StringProperty currentTrackId = new SimpleStringProperty();
		private StringProperty currentTrackArtist = new SimpleStringProperty();
		private StringProperty currentTrackName = new SimpleStringProperty();
		private BooleanProperty autoPlay = new SimpleBooleanProperty(true);

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private InternalMusicState(int volume) {
			this.volume = new SimpleIntegerProperty(volume);
		}

		// ----------------------------------------------------------------------------------------
		// MusicState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public StringProperty currentPlaylistName() {
			return currentPlaylistName;
		}

		@Override
		public ReadOnlyStringProperty currentTrackId() {
			return currentTrackId;
		}

		@Override
		public ReadOnlyStringProperty currentTrackArtist() {
			return currentTrackArtist;
		}

		@Override
		public ReadOnlyStringProperty currentTrackName() {
			return currentTrackName;
		}

		@Override
		public BooleanProperty autoPlay() {
			return autoPlay;
		}

		@Override
		public IntegerProperty volume() {
			return volume;
		}
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