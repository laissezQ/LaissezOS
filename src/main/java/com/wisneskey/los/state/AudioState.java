package com.wisneskey.los.state;

import com.wisneskey.los.service.audio.SoundEffectSet;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * Audio service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface AudioState extends State {

	/**
	 * Returns the read only property for the currently selected sound effect set.
	 * 
	 * @return Read only property containing the id of the current selected sound
	 *         effect set.
	 */
	ReadOnlyObjectProperty<SoundEffectSet> selectedSoundEffectSet();
}
