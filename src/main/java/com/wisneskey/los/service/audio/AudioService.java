package com.wisneskey.los.service.audio;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.AudioState;
import com.wisneskey.los.state.ChairState.MasterState;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

/**
 * Service for playing audio tracks and sound effects.
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
public class AudioService extends AbstractService<AudioState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioService.class);

	/**
	 * Minimum supported volume.
	 */
	private static final float MIN_VOLUME = 0.0f;

	/**
	 * Maximum supported volume.
	 */
	private static final float MAX_VOLUME = 11.0f;

	/**
	 * Maximum allowed scaled factor for mpg123.
	 */
	private static final double MAX_SCALE_FACTOR = 32768;

	/**
	 * Base of exponential use for emphasizing lower volume scale factors.
	 */
	private static final double SCALE_FACTOR_BASE = 1.6d;

	/**
	 * Object for the state of the audio service.
	 */
	private InternalAudioState audioState;

	/**
	 * Command to use to start the external player.
	 */
	private String playerCommand;

	/**
	 * Base path to the sound effect MP3s.
	 */
	private String basePath;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private AudioService() {
		super(ServiceId.AUDIO);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Play a sound effect.
	 * 
	 * @param effectId          Id of the sound effect to play.
	 * @param waitForCompletion Flag indicating if the call should not return
	 *                            until the playback is completed.
	 */
	public void playEffect(SoundEffectId effectId, boolean waitForCompletion) {

		// Get the scale factor to use for setting the volume of the MP3 playback.
		int scaleFactor;

		if (Kernel.kernel().chairState().masterState().getValue() == MasterState.CHAP) {
			scaleFactor = getScaleFactor(audioState.chapModeVolume().get());
		} else {
			scaleFactor = getScaleFactor(audioState.volume().get());
		}

		// Get the path to the MP3 file for the effect.
		Path mp3Path = Paths.get(basePath, effectId.getFileName());

		LOGGER.debug("Playing sound effect: id={} mp3={} scaleFactor={}", effectId, mp3Path, scaleFactor);

		Thread playerThread = new SoundEffectPlayerThread(mp3Path.toString(), scaleFactor);
		playerThread.start();

		if (waitForCompletion) {
			try {
				playerThread.join();
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted waiting for audio clip to complete.");
				Thread.currentThread().interrupt();
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public AudioState getState() {
		return audioState;
	}

	@Override
	public void terminate() {
		// Nothing to be done for termination, player threads will wrap up on their own.
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the audio service and returns its initial state.
	 * 
	 * @param  profile Profile with initial audio settings.
	 * @return         Configured state object for the service.
	 */
	private AudioState initialize(Profile profile) {

		LOGGER.info("Initializing audio service...");

		// Make sure the external player is defined.
		if (profile.getPlayerCommand() == null) {
			throw new LaissezException("External MP3 player not set in profile.");
		}

		// Make sure base path is defined.
		if (profile.getSoundEffectDir() == null) {
			throw new LaissezException("Sound effect directory not set.");
		}

		this.playerCommand = profile.getPlayerCommand();
		this.basePath = profile.getSoundEffectDir();

		audioState = new InternalAudioState(profile.getVolume(), profile.getChapModeVolume());
		return audioState;
	}

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

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the audio service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile with initial audio settings.
	 * @return         Audio service instance and its initial state object.
	 */
	public static Pair<AudioService, AudioState> createService(Profile profile) {

		AudioService service = new AudioService();
		AudioState state = service.initialize(profile);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private class SoundEffectPlayerThread extends Thread {

		private String trackPath;
		private int scaleFactor;

		private SoundEffectPlayerThread(String trackPath, int scaleFactor) {
			this.trackPath = trackPath;
			this.scaleFactor = scaleFactor;

			setName("SoundEffectPlayerThread");
		}

		@Override
		public void run() {

			try {
				LOGGER.info("Attempting to play sound effect: scaleFactor={} path={}", scaleFactor, trackPath);
				ProcessBuilder processBuilder = new ProcessBuilder(//
						playerCommand, //
						// "-o", //
						// "alsa:hw:2,0", //
						"-q", //
						"-f", //
						String.valueOf(scaleFactor), //
						trackPath);
				Process playerProcess = processBuilder.start();

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
		}
	}

	/**
	 * Internal state object for the Audio service.
	 */
	private static class InternalAudioState implements AudioState {

		private IntegerProperty volume;

		private IntegerProperty chapModeVolume;

		private AtomicInteger playingCount;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private InternalAudioState(int volume, int chapModeVolume) {
			this.volume = new SimpleIntegerProperty(volume);
			this.chapModeVolume = new SimpleIntegerProperty(chapModeVolume);
			this.playingCount = new AtomicInteger(0);
		}

		// ----------------------------------------------------------------------------------------
		// AudioState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public IntegerProperty volume() {
			return volume;
		}

		@Override
		public IntegerProperty chapModeVolume() {
			return chapModeVolume;
		}

		@Override
		public AtomicInteger playingCount() {
			return playingCount;
		}
	}
}