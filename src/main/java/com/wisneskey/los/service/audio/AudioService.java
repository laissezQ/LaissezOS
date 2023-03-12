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

/**
 * Service for playing audio tracks and sound effects.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class AudioService extends AbstractService<Object> {

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
	 * Current sound effect set in use.
	 */
	private SoundEffectSet currentSet = DEFAULT_SOUND_EFFECT_SET;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public AudioService() {
		super(ServiceId.AUDIO);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public void selectSet(SoundEffectSet set) {
		this.currentSet = set;
	}

	public SoundEffectSet getCurrentSet() {
		return currentSet;
	}

	public void playEffect(SoundEffect effect) {

		new SoundEffectPlayerThread(currentSet, effect).start();
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

	@Override
	public Object getInitialState(Profile profile) {
		// TODO Auto-generated method stub
		return null;
	}
}
