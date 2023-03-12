package com.wisneskey.los.state;

import com.wisneskey.los.service.profile.model.Profile;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface ProfileState {

	ReadOnlyObjectProperty<Profile> activeProfile();
}
