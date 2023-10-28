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
import com.wisneskey.los.util.StopWatch;

import javafx.util.Pair;

/**
 * Service for playing audio tracks and sound effects.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class AudioService extends AbstractService<AudioState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudioService.class);

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

		LOGGER.debug("Playing sound effect: {}", effectId);
		Thread playerThread = new SoundEffectPlayerThread(effectId);
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
	public void terminate() {
		LOGGER.trace("Audio service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the audio services and returns its initial state.
	 * 
	 * @param  profile Profile to use for configuring initial service state.
	 * @return         Configured state object for the service.
	 */
	private AudioState initialize(Profile profile) {

		LOGGER.info("Initializing audio service...");

		if (Kernel.kernel().getRunMode() == RunMode.CHAIR) {

			// If we are running on the chair itself, we need to toggle the relay that
			// enables power to the amplifier board.
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.AMPLIFIER);
		}

		audioState = new InternalAudioState();
		return audioState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the audio service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile to use for configuring initial state of the audio
	 *                   service.
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

		private SoundEffectId effectId;

		public SoundEffectPlayerThread(SoundEffectId effectId) {
			this.effectId = effectId;

			setDaemon(true);
			setName("SoundEffectPlayer[" + effectId + "]");
		}

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

				// Add a listener so we can wait until the clip stops playing.
				clip.addLineListener(e -> {
					if (e.getType() == LineEvent.Type.STOP) {
						waitLatch.countDown();
					}
				});

				clip.open(audioIn);
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
				PLAYER_LOGGER.error("Failed to play sound effect.", e);
			}

			PLAYER_LOGGER.trace("Player thread ended.");
		}
	}

	/**
	 * Internal state object for the Audio service.
	 */
	private static class InternalAudioState implements AudioState {

	}
}
