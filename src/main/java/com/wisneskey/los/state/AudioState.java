package com.wisneskey.los.state;

import com.wisneskey.los.service.audio.SoundEffectSet;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface AudioState {

	ReadOnlyObjectProperty<SoundEffectSet> selectedSoundEffectsSet();
}
