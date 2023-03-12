package com.wisneskey.los.service.profile;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LOSException;
import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.ProfileState;
import com.wisneskey.los.util.JsonUtils;
import com.wisneskey.los.util.ValidationUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Service for managing profiles which specify the configuration and behavior of
 * the operating system. Profiles are intended to be read only but can be used
 * to set up the initial state of the chair which may then be changed.
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
	private InternalProfileState profileState = new InternalProfileState();

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public ProfileService() {
		super(ServiceId.PROFILE);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	public ProfileState getInitialState(Profile profile) {
		return profileState;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Loads the profile with the specified profile id as the active profile.
	 * 
	 * @param profileId
	 *          Id of the profile to activate.
	 */
	public void activateProfile(String profileId) {

		if (profileState.activeProfile().get() != null) {
			throw new LOSException("Profile already active.");
		}

		LOGGER.info("Activating profile: profileId={}", profileId);

		String profilePath = PROFILE_RESOURCE_BASE + profileId + ".json";
		Profile profile = null;
		try {
			InputStream input = getClass().getResourceAsStream(profilePath);
			profile = JsonUtils.toObject(input, Profile.class);
		} catch (Exception e) {
			throw new LOSException("Failed to load profile.", e);
		}

		// Validate the profile before we apply it.
		validateProfile(profile);

		// Now set it in our internal state.
		profileState.setActiveProfile(profile);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Checks to make sure a loaded profile is valid and throws an exception of
	 * there are any issues with the profile.
	 * 
	 * @param profile
	 *          Profile to validate.
	 */
	private void validateProfile(Profile profile) {

		LOGGER.debug("Validating profile...");

		// First make sure the profile has supported run modes.
		ValidationUtils.requireValue(profile.getSupportedRunModes(), "Supported run modes required.");

		// Since we have valid run modes, make sure the current run mode is
		// supported by the profile.
		if (!profile.getSupportedRunModes().contains(LOSKernel.kernel().getRunMode())) {
			throw new LOSException("Profile does not supported kernel run mode '" + LOSKernel.kernel().getRunMode() + "'.");
		}

		LOGGER.debug("Profile validation passed.");
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

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
		// Public methods.
		// ----------------------------------------------------------------------------------------

		public void setActiveProfile(Profile profile) {
			this.activeProfile.set(profile);
		}
	}
}
