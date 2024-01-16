package com.wisneskey.los.service.display.listener.bar;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.state.ChairState.BarState;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * Change listener that monitors the bar state and updates the icon on a bar
 * button based on whether it is raised or not.
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
public class BarStateListener implements ChangeListener<BarState> {

	/**
	 * Button to update the icon for.
	 */
	private Button barButton;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a lister that updates the specified button.
	 * 
	 * @param barButton Button to update icon of.
	 */
	public BarStateListener(Button barButton) {
		this.barButton = barButton;
		
		updateButtonState(Kernel.kernel().chairState().barState().getValue());
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends BarState> observable, BarState oldValue, BarState newValue) {

		Platform.runLater(() -> {
			updateButtonState(newValue);
		});

	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void updateButtonState(BarState state) {

		switch (state) {

		case LOWERED:
			barButton.setTextFill(Color.WHITE);
			break;

		case RAISING:
			barButton.setTextFill(Color.RED);
			break;

		case RAISED:
			barButton.setTextFill(Color.LIGHTGREEN);
			break;

		case LOWERING:
			barButton.setTextFill(Color.RED);
			break;
		}
	}
}