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

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public TimedRelayWhenPressedListener(RelayId relayId, Duration duration) {
		this.relayId = relayId;
		this.duration = duration;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

		if (pressed) {
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(relayId, duration);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Static utility method to add the listener to a JavaFX node.
	 * 
	 * @param node
	 *          Node to add the listener to.
	 * @param relayId
	 *          Id of the relay to toggle.
	 * @param duration
	 *          Duration to turn relay on for.
	 */
	public static void add(Node node, RelayId relayId, Duration duration) {
		node.pressedProperty().addListener(new TimedRelayWhenPressedListener(relayId, duration));
	}

}