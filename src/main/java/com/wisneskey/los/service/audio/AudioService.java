package com.wisneskey.los.service.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
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
	 * @param effectId
	 *          Id of the sound effect to play.
	 */
	public void playEffect(SoundEffectId effectId) {

		LOGGER.debug("Playing sound effect: {}", effectId);
		new SoundEffectPlayerThread(effectId).start();
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
	 * Creates the initial state of the service using the supplied profile for
	 * configuration.
	 * 
	 * @param profile
	 *          Profile to use for configuring initial service state.
	 * @return Configured state object for the service.
	 */
	private AudioState createInitialState(Profile profile) {
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
	 * @param profile
	 *          Profile to use for configuring initial state of the audio service.
	 * @return Audio service instance and its initial state object.
	 */
	public static Pair<AudioService, AudioState> createService(Profile profile) {

		AudioService service = new AudioService();
		AudioState state = service.createInitialState(profile);
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

		public SoundEffectPlayerThread( SoundEffectId effectId) {
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
				clip.open(audioIn);

				PLAYER_LOGGER.trace("Audio clip playback starting; load time: {}", timer.elapsedAsString());
				clip.start();
				PLAYER_LOGGER.trace("Audio clip playback started; total time: {}", timer.elapsedAsString());
				
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
