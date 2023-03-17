package com.wisneskey.los.state;

import com.wisneskey.los.service.profile.model.Profile;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * State object for the profile service.
 * 
 * @author paul.wisneskey@gmail.com
 */
public interface ProfileState extends State {

	/**
	 * Return a read only property containing the active profile.
	 * 
	 * @return Read only property with active profile.
	 */
	ReadOnlyObjectProperty<Profile> activeProfile();
}
