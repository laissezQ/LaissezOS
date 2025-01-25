package com.wisneskey.los.service.display.listener.bar;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * Listener for raising and lowering the pop up bar depending on the current
 * state of the bar.
 *
 * Copyright (C) 2025 Paul Wisneskey
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
public class BarButtonListener implements ChangeListener<Boolean> {

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

		if (pressed.booleanValue()) {

			ScriptId scriptId = null;

			switch (Kernel.kernel().chairState().barState().getValue()) {

			case LOWERED:
				scriptId = ScriptId.BAR_RAISE;
				break;

			case RAISED:
				scriptId = ScriptId.BAR_LOWER;
				break;

			default:
				// Don't do anything for intermediate states.
				scriptId = null;
			}

			if (scriptId != null) {
				((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(scriptId);
			}
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
		node.pressedProperty().addListener(new BarButtonListener());
	}
}