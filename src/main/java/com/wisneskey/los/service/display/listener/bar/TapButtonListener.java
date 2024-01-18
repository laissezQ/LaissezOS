package com.wisneskey.los.service.display.listener.bar;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.state.ChairState.BarState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * Listener for support the push button running of the water tap in the bar
 * based on the bar's state.
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
public class TapButtonListener implements ChangeListener<Boolean> {

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

		if (pressed.booleanValue()) {

			BarState barState = Kernel.kernel().chairState().barState().getValue();

			// Only turn off if the bar is raised.
			if (barState == BarState.RAISED) {
				Kernel.kernel().message("Pouring libations...\n");
				((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.BAR_PUMP);
			}
		} else {

			// Turn off at any time the button is released.
			Kernel.kernel().message("Libation dispensing complete!\n");
			((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(RelayId.BAR_PUMP);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Static utility method to add the listener to a JavaFX node.
	 * 
	 * @param node Node to add the listener to.
	 */
	public static void add(Node node) {
		node.pressedProperty().addListener(new TapButtonListener());
	}
}