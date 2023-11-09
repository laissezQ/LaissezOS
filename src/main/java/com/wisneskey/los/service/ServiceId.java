package com.wisneskey.los.service;

import com.wisneskey.los.kernel.ShutdownPhase;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.lighting.LightingService;
import com.wisneskey.los.service.location.LocationService;
import com.wisneskey.los.service.map.MapService;
import com.wisneskey.los.service.profile.ProfileService;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.service.remote.RemoteService;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.service.security.SecurityService;
import com.wisneskey.los.state.AudioState;
import com.wisneskey.los.state.DisplayState;
import com.wisneskey.los.state.LightingState;
import com.wisneskey.los.state.LocationState;
import com.wisneskey.los.state.MapState;
import com.wisneskey.los.state.ProfileState;
import com.wisneskey.los.state.RelayState;
import com.wisneskey.los.state.RemoteState;
import com.wisneskey.los.state.ScriptState;
import com.wisneskey.los.state.SecurityState;
import com.wisneskey.los.state.State;

/**
 * Enumerated type for all of the ids for the services supported in LBOS.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public enum ServiceId {

	AUDIO(AudioService.class, AudioState.class, ShutdownPhase.TWO, "This service goes to 11."),
	DISPLAY(DisplayService.class, DisplayState.class, ShutdownPhase.TWO, "Looking good there!"),
	LIGHTING(LightingService.class, LightingState.class, ShutdownPhase.TWO, "Let there be lighting!"),
	LOCATION(LocationService.class, LocationState.class, ShutdownPhase.TWO, "Where the hell am I?"),
	MAP(MapService.class, MapState.class, ShutdownPhase.THREE, "Roads? Where we are going, we don't need roads!"),
	PROFILE(ProfileService.class, ProfileState.class, ShutdownPhase.THREE, "Keep it low."),
	RELAY(RelayService.class, RelayState.class, ShutdownPhase.THREE, "Have you tried turning it off and on again?"),
	REMOTE(RemoteService.class, RemoteState.class, ShutdownPhase.ONE, "Remotes, how do they work?"),
	SCRIPT(ScriptService.class, ScriptState.class, ShutdownPhase.ONE, "Don't tell me what to do!"),
	SECURITY(SecurityService.class, SecurityState.class, ShutdownPhase.THREE, "1000 times no!");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	/**
	 * Brief description of the service.
	 */
	private String description;

	/**
	 * Class of the service object.
	 */
	private Class<? extends Service<?>> serviceClass;

	/**
	 * Class of the service state object.
	 */
	private Class<? extends State> serviceStateClass;

	/**
	 * Which shutdown phase to terminate service in.
	 */
	private ShutdownPhase shutdownPhase;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private ServiceId(Class<? extends Service<?>> serviceClass, Class<? extends State> serviceStateClass,
			ShutdownPhase shutdownPhase, String description) {
		this.serviceClass = serviceClass;
		this.serviceStateClass = serviceStateClass;
		this.shutdownPhase = shutdownPhase;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the class that implements the service.
	 * 
	 * @param  <T> Type of service.
	 * @return     Class for the service.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service<?>> Class<T> getServiceClass() {
		return (Class<T>) serviceClass;
	}

	/**
	 * Returns the class the provides the service's state.
	 * 
	 * @return Class providing services state.
	 */
	public Class<? extends State> getServiceStateClass() {
		return serviceStateClass;
	}

	/**
	 * Return what shutdown phase the service should be terminated in.
	 * 
	 * @return Shutdown phase to terminate the service in.
	 */
	public ShutdownPhase getShutdownPhase() {
		return shutdownPhase;
	}

	/**
	 * Returns a brief description for the service the id is for.
	 * 
	 * @return Description of the service for the id.
	 */
	public String getDescription() {
		return description;
	}
}