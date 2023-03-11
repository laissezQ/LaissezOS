package com.wisneskey.los.service.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;

/**
 * Service for playing audio sound effects. Sound effects are loaded entirely
 * into memory before being played so they should be relative short clips.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class AudioService extends AbstractService {

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

		new Thread() { public void run() {

			String resourceLocation = "/audio/" + currentSet + "/" + effect.getResourcePath();

			try {
				InputStream source = getClass().getResourceAsStream(resourceLocation);
				InputStream inputStream = new BufferedInputStream(source);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(inputStream);
				AudioFormat format = audioIn.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioIn);
				clip.start();
			} catch (Exception e1) {
				System.err.println("Error playing sound: " + e1);
			}

			
		} }.run();
		
		
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private String getEffectFileLocation(SoundEffect effect) {

		String resourceLocation = "/audio/" + currentSet + "/" + effect.getResourcePath();
		return getClass().getResource(resourceLocation).toExternalForm();
	}
}
