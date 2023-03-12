package com.wisneskey.los.service.audio;

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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
	 * Default sound effect set to use.
	 */
	private static final SoundEffectSet DEFAULT_SOUND_EFFECT_SET = SoundEffectSet.DEV;

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

	public void selectSet(SoundEffectSet set) {
		audioState.setSoundEffectSet(set);
	}

	public void playEffect(SoundEffect effect) {

		new SoundEffectPlayerThread(audioState.selectedSoundEffectSet().get(), effect).start();
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private AudioState createInitialState(Profile profile) {
		audioState = new InternalAudioState();
		// TODO: Set the initial selected audio set from profile here.
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

		private SoundEffectSet set;
		private SoundEffect effect;

		public SoundEffectPlayerThread(SoundEffectSet set, SoundEffect effect) {
			this.set = set;
			this.effect = effect;

			setDaemon(true);
			setName("SoundEffectPlayer[" + effect + "]");
		}

		@Override
		public void run() {

			String resourceLocation = AUDIO_RESOURCE_BASE + set + "/" + effect.getResourcePath();

			try {
				InputStream source = getClass().getResourceAsStream(resourceLocation);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(source);
				AudioFormat format = audioIn.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioIn);
				clip.start();
			} catch (Exception e) {
				LOGGER.error("Failed to play sound effect: set=" + set + " effect=" + effect, e);
			}
		}

	}

	private static class InternalAudioState implements AudioState {

		/**
		 * Current sound effect set in use.
		 */
		private SimpleObjectProperty<SoundEffectSet> currentSet = new SimpleObjectProperty<SoundEffectSet>(this,
				"selectedSoundEffectSet", DEFAULT_SOUND_EFFECT_SET);

		@Override
		public ReadOnlyObjectProperty<SoundEffectSet> selectedSoundEffectSet() {
			return currentSet;
		}

		public void setSoundEffectSet(SoundEffectSet newSet) {
			currentSet.setValue(newSet);
		}
	}
}
