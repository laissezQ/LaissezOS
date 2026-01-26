package com.wisneskey.los.service.security;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.SecurityState;

import javafx.util.Pair;

/**
 * Service for managing the security of the chair operating system.
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
public class SecurityService extends AbstractService<SecurityState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

	/**
	 * Default lock screen method.
	 */
	public static final String DEFAULT_LOCK_MESSAGE = "SYSTEM LOCKED";

	/**
	 * Object for the state of the audio service.
	 */
	private InternalSecurityState securityState;

	/**
	 * 4 digit code required to unlock chair.
	 */
	private String pinCode;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private SecurityService() {
		super(ServiceId.SECURITY);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public SecurityState getState() {
		return securityState;
	}

	@Override
	public void terminate() {
		LOGGER.trace("Security service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public boolean isPinCorrect(String pinEntered) {

		return Objects.equals(pinCode, pinEntered);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates the initial state of the service using the supplied profile for
	 * configuration.
	 * 
	 * @param  profile Profile to use for configuring initial service state.
	 * @return         Configured state object for the service.
	 */
	private SecurityState createInitialState() {
		securityState = new InternalSecurityState();
		return securityState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the security service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile to use for configuring initial state of the
	 *                   security service.
	 * @return         Security service instance and its initial state object.
	 */
	public static Pair<SecurityService, SecurityState> createService(Profile profile) {

		SecurityService service = new SecurityService();
		service.pinCode = profile.getPinCode();

		SecurityState state = service.createInitialState();
		return new Pair<>(service, state);
	}

	/**
	 * Internal state object for the Security service.
	 */
	private static class InternalSecurityState implements SecurityState {

	}
}
