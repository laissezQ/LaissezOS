package com.wisneskey.los.service.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.state.AudioState;
import com.wisneskey.los.state.ChairState.MasterState;
import com.wisneskey.los.util.StopWatch;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

/**
 * Service for playing audio tracks and sound effects.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
	 * Minimum allowed gain adjustment (e.g. none).
	 */
	private static final float MIN_GAIN_ADJUSTMENT = 0.0f;

	/**
	 * Maximum allowed gain adjustment (e.g. silent).
	 */
	private static final float MAX_GAIN_ADJUSTMENT = -80.0f;

	/**
	 * Base path for where audio clips are saved in the resources.
	 */
	private static final String AUDIO_RESOURCE_BASE = "/audio/";

	/**
	 * Object for the state of the audio service.
	 */
	private InternalAudioState audioState;

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

		IntegerProperty volumeProperty;
		if( Kernel.kernel().chairState().masterState().getValue() == MasterState.CHAP) {
			volumeProperty = audioState.chapModeVolume();
		} else
		{
			volumeProperty = audioState.volume();
		}

		float gainAdjustment = getGainAdjustment(volumeProperty.get(), effectId.clipGain());
		
		LOGGER.debug("Playing sound effect: id={} volume={} gainAdjustment={}", effectId,
				volumeProperty.getValue(), gainAdjustment);
		
		Thread playerThread = new SoundEffectPlayerThread(effectId, gainAdjustment);
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
		LOGGER.trace("Audio service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the audio services and returns its initial state.
	 * 
	 * @param  profile Profile with initial audio settings.
	 * @return         Configured state object for the service.
	 */
	private AudioState initialize(Profile profile) {

		LOGGER.info("Initializing audio service...");

		if (Kernel.kernel().getRunMode() == RunMode.CHAIR) {

			// If we are running on the chair itself, we need to toggle the relay that
			// enables power to the amplifier board.
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.AMPLIFIER);
		}

		audioState = new InternalAudioState(profile.getVolume(), profile.getChapModeVolume());
		return audioState;
	}

	/**
	 * Returns the gain adjustment (reduction in gain) to be applied to a sound
	 * clip to play it at the indicated volume.
	 * 
	 * @param  volume Volume to calculate gain adjustment for.
	 * @return        Gain adjustment to be applied to sound clip to reduce its
	 *                volume.
	 */
	private float getGainAdjustment(int volume, float clipAdjustment) {

		float volumeFloat =  volume;
		
		// Make sure volume is in allowed range.
		volumeFloat = Math.max(MIN_VOLUME, volumeFloat);
		volumeFloat = Math.min(volumeFloat, MAX_VOLUME);

		// Add the clip adjustment after we limit the range so that some clips can
		// go slightly above or below the range.
		volumeFloat += clipAdjustment;
		
		// Gain adjustment is applied to lower volume so we invert the percentage.
		float adjustPercent = 1.0f - (volumeFloat / MAX_VOLUME);		
		return MIN_GAIN_ADJUSTMENT + adjustPercent * (MAX_GAIN_ADJUSTMENT + MIN_GAIN_ADJUSTMENT);
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

	/**
	 * Thread for playing a sound effect via the javax.sound classes.
	 */
	private static class SoundEffectPlayerThread extends Thread {

		private static final Logger PLAYER_LOGGER = LoggerFactory.getLogger("EffectPlayer");

		/**
		 * Id of the sound effect to play back.
		 */
		private SoundEffectId effectId;
		
		/**
		 * Adjustment to apply to lower the volume of the clip.
		 */
		private float gainAdjustment;
		
		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		public SoundEffectPlayerThread(SoundEffectId effectId, float gainAdjustment) {
			this.effectId = effectId;
			this.gainAdjustment = gainAdjustment;
			
			setDaemon(true);
			setName("SoundEffectPlayer[" + effectId + "]");
		}

		// ----------------------------------------------------------------------------------------
		// Thread methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public void run() {

			PLAYER_LOGGER.trace("Player thread started...");

			String resourceLocation = AUDIO_RESOURCE_BASE + effectId.getResourcePath();
			PLAYER_LOGGER.trace("Audio clip resource: {}", resourceLocation);

			try {

				CountDownLatch waitLatch = new CountDownLatch(1);
				StopWatch timer = new StopWatch();

				// Load the resource to play back as a clip. Put it in a buffered input
				// stream so that stream can rewind after reading the header
				// information.
				InputStream source = getClass().getResourceAsStream(resourceLocation);
				if (source == null) {
					throw new IOException("Sound effect resource not found.");
				}

				BufferedInputStream bufferedSource = new BufferedInputStream(source);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedSource);
				AudioFormat format = audioIn.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip clip = (Clip) AudioSystem.getLine(info);

				// Add a listener so we can wait until the clip stops playing and then
				// close it.
				clip.addLineListener(e -> {
					if (e.getType() == LineEvent.Type.STOP) {
						clip.close();
						waitLatch.countDown();
					}
				});

				clip.open(audioIn);
				
				// Adjust the volume by applying the supplied gain adjustment.
				FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(gainAdjustment); 
				
				// Start the playback.
				clip.start();

				if (PLAYER_LOGGER.isTraceEnabled()) {
					PLAYER_LOGGER.trace("Audio clip playback started; time until start: {}", timer.elapsedAsString());
				}

				// Now wait for the clip to complete playing.
				waitLatch.await();

				if (PLAYER_LOGGER.isTraceEnabled()) {
					PLAYER_LOGGER.trace("Audio clip playback completed; total time: {}", timer.elapsedAsString());
				}

			} catch (InterruptedException e) {
				PLAYER_LOGGER.error("Interrupted while playing sound effect.");
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				PLAYER_LOGGER.error("Failed to play sound effect: " + effectId, e);
			}

			PLAYER_LOGGER.trace("Player thread ended.");
		}
	}

	/**
	 * Internal state object for the Audio service.
	 */
	private static class InternalAudioState implements AudioState {

		private IntegerProperty volume;

		private IntegerProperty chapModeVolume;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private InternalAudioState(int volume, int chapModeVolume) {
			this.volume = new SimpleIntegerProperty(volume);
			this.chapModeVolume = new SimpleIntegerProperty(chapModeVolume);
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
	}
}
