package com.wisneskey.los.service.display.listener.relay;

import java.time.Duration;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * Listener for a pressed event that starts a timed relay when a control is
 * pressed.
 * 
 * Copyright (C) 2024 Paul Wisneskey
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
public class TimedRelayWhenPressedListener implements ChangeListener<Boolean> {

	/**
	 * Id of the relay to turn on.
	 */
	private RelayId relayId;

	/**
	 * Duration the relay should remain on.
	 */
	private Duration duration;

	/**
	 * Optional message to display when pressed.
	 */
	private String message;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public TimedRelayWhenPressedListener(RelayId relayId, Duration duration, String message) {
		this.relayId = relayId;
		this.duration = duration;
		this.message = message;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

		if (pressed.booleanValue()) {
			Kernel.kernel().message(message);
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(relayId, duration, false);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Static utility method to add the listener to a JavaFX node.
	 * 
	 * @param node     Node to add the listener to.
	 * @param relayId  Id of the relay to toggle.
	 * @param duration Duration to turn relay on for.
	 */
	public static void add(Node node, RelayId relayId, Duration duration) {
		node.pressedProperty().addListener(new TimedRelayWhenPressedListener(relayId, duration, null));
	}

	/**
	 * Static utility method to add the listener to a JavaFX node.
	 * 
	 * @param node     Node to add the listener to.
	 * @param relayId  Id of the relay to toggle.
	 * @param duration Duration to turn relay on for.
	 * @param message  Message to display.
	 */
	public static void add(Node node, RelayId relayId, Duration duration, String message) {
		node.pressedProperty().addListener(new TimedRelayWhenPressedListener(relayId, duration, message));
	}
}