package com.wisneskey.los.service.profile;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LOSException;
import com.wisneskey.los.kernel.LOSKernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.util.JsonUtils;
import com.wisneskey.los.util.ValidationUtils;

/**
 * Service for managing profiles which configure the configuration and behavior
 * of the operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class ProfileService extends AbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

	/**
	 * Default to the development profile for off chair development.
	 */
	public static final String DEFAULT_PROFILE_ID = "Development";

	/**
	 * Base path for where profiles are saved in the resources.
	 */
	private static final String PROFILE_RESOURCE_BASE = "/profile/";

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public ProfileService() {
		super(ServiceId.PROFILE);
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Loads the profile with the specified profile id.
	 * 
	 * @param profileId
	 *          Id of the profile to load.
	 */
	public Profile loadProfile(String profileId) {

		LOGGER.info("Loading profile: profileId={}", profileId);

		String profilePath = PROFILE_RESOURCE_BASE + profileId + ".json";
		Profile profile = null;
		try {
			InputStream input = getClass().getResourceAsStream(profilePath);
			profile = JsonUtils.toObject(input, Profile.class);
		} catch (Exception e) {
			throw new LOSException("Failed to load profile.", e);
		}

		// Validate the profile before we give it to the caller.
		validateProfile(profile);

		return profile;
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
		if (!profile.getSupportedRunModes().contains(LOSKernel.getRunMode())) {
			throw new LOSException("Profile does not supported kernel run mode '" + LOSKernel.getRunMode() + "'.");
		}
		
		LOGGER.debug("Profile validation passed.");
	}
}
