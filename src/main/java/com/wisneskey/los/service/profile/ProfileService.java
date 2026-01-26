package com.wisneskey.los.service.profile;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.ProfileState;
import com.wisneskey.los.util.JsonUtils;
import com.wisneskey.los.util.ValidationUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

/**
 * Service for managing profiles which specify the configuration and behavior of
 * the operating system. Profiles are intended to be read only but can be used
 * to set up the initial state of the chair which may then be changed.
 * 
 * Copyright (C) 2026 Paul Wisneskey
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
public class ProfileService extends AbstractService<ProfileState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

	/**
	 * Default to the development profile for off chair development.
	 */
	public static final String DEFAULT_PROFILE_ID = "Development";

	/**
	 * Base path for where profiles are saved in the resources.
	 */
	private static final String PROFILE_RESOURCE_BASE = "/profile/";

	/**
	 * Internal state object.
	 */
	private InternalProfileState profileState;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private ProfileService() {
		super(ServiceId.PROFILE);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public ProfileState getState() {
		return profileState;
	}
	
	@Override
	public void terminate() {
		LOGGER.trace("Profile service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	// Currently no public methods; other services just use the profile for
	// initialization.

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates the initial profile state by loading the profile with the given id.
	 * 
	 * @param  profileId Id of the profile to load for the initial profile state.
	 * @return           Profile state object with the specified profile loaded.
	 */
	private ProfileState createInitialState(String profileId) {
		profileState = new InternalProfileState();
		Profile profile = loadProfile(profileId);
		profileState.setActiveProfile(profile);
		return profileState;
	}

	/**
	 * Loads the profile with the specified profile id.
	 * 
	 * @param  profileId Id of the profile to load.
	 * @return           The loaded profile
	 */
	private Profile loadProfile(String profileId) {

		if (profileState.activeProfile().get() != null) {
			throw new LaissezException("Profile already loaded.");
		}

		LOGGER.info("Loading profile: {}", profileId);

		String profilePath = PROFILE_RESOURCE_BASE + profileId + ".json";
		Profile profile = null;
		try {
			InputStream input = getClass().getResourceAsStream(profilePath);
			if( input == null ) {
				throw new FileNotFoundException("Resouce " + profilePath + " not found.");
			}
			
			profile = JsonUtils.toObject(input, Profile.class);
		} catch (Exception e) {
			throw new LaissezException("Failed to load profile.", e);
		}

		// Validate the profile
		validateProfile(profile);

		LOGGER.info("Profile loaded: {}", profile.getDescription());
		return profile;
	}

	/**
	 * Checks to make sure a loaded profile is valid and throws an exception of
	 * there are any issues with the profile.
	 * 
	 * @param profile Profile to validate.
	 */
	private void validateProfile(Profile profile) {

		LOGGER.debug("Validating profile...");

		// First make sure the profile has supported run modes.
		ValidationUtils.requireValue(profile.getSupportedRunModes(), "Supported run modes required.");

		// Since we have valid run modes, make sure the current run mode is
		// supported by the profile.
		if (!profile.getSupportedRunModes().contains(Kernel.kernel().getRunMode())) {
			throw new LaissezException("Profile does not supported kernel run mode '" + Kernel.kernel().getRunMode() + "'.");
		}

		LOGGER.debug("Profile validation passed.");
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the profile service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profileId Id of the profile to load for the profile state.
	 * @return           Audio service instance and its initial state object.
	 */
	public static Pair<ProfileService, ProfileState> createService(String profileId) {

		if (profileId == null) {
			profileId = DEFAULT_PROFILE_ID;
		}
		ProfileService service = new ProfileService();
		ProfileState state = service.createInitialState(profileId);
		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Internal state object for the profile service.
	 */
	private static class InternalProfileState implements ProfileState {

		private ObjectProperty<Profile> activeProfile = new SimpleObjectProperty<>(this, "activeProfile");

		// ----------------------------------------------------------------------------------------
		// ProfileState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<Profile> activeProfile() {
			return activeProfile;
		}

		// ----------------------------------------------------------------------------------------
		// Supporting methods.
		// ----------------------------------------------------------------------------------------

		/**
		 * Update the active profile.
		 * 
		 * @param profile Profile to make the active profile.
		 */
		private void setActiveProfile(Profile profile) {
			this.activeProfile.set(profile);
		}
	}
}