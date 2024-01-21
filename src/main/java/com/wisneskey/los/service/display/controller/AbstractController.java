package com.wisneskey.los.service.display.controller;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.state.ChairState;
import com.wisneskey.los.state.ChairState.BarState;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.text.Font;

/**
 * Abstract base classes for JavaFX controllers that provides utility methods
 * for accessing chair state and services.
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
public abstract class AbstractController implements SceneController {

	/**
	 * Effect to use for list buttons.
	 */
	protected static final Effect LIST_BUTTON_EFFECT = new DropShadow();

	/**
	 * Font to use for the buttons in scrollable lists.
	 */
	protected static final Font LIST_BUTTON_FONT = new Font("System Bold", 14.0);

	/**
	 * Width to make the buttons when they are used in scrollable lists.
	 */
	protected static final double LIST_BUTTON_WIDTH = 350.0;

	/**
	 * Padding insets to use for buttons in scrollable lists.
	 */
	protected static final Insets LIST_BUTTON_PADDING = new Insets(10, 0, 10, 0);

	// ----------------------------------------------------------------------------------------
	// SceneController methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// By default, the B button on the remote works the bar.
		if( buttonId == RemoteButtonId.REMOTE_BUTTON_B) {

			BarState barState = chairState().barState().getValue();
			if( barState == BarState.LOWERED ) {
				runScript(ScriptId.BAR_RAISE);
			} else if (barState == BarState.RAISED) {
				runScript(ScriptId.BAR_LOWER);
			}
		}
	}

	@Override
	public void sceneShown() {
		// By default, don't do anything special for scene being shown.
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates a button to use in a scrollable list.
	 * 
	 * @param  title Title for the button.
	 * @return       List button with the supplied title.
	 */
	protected Button createListButton(String title) {

		Button listButton = new Button(title);
		listButton.setFont(LIST_BUTTON_FONT);
		listButton.setEffect(LIST_BUTTON_EFFECT);
		listButton.setMinWidth(LIST_BUTTON_WIDTH);
		listButton.setPadding(LIST_BUTTON_PADDING);

		return listButton;
	}

	/**
	 * Return the kernel.
	 * 
	 * @return Kernel for the chair's operating system.
	 */
	protected Kernel kernel() {
		return Kernel.kernel();
	}

	/**
	 * Return the chair state object.
	 * 
	 * @return Top level state object for the chair.
	 */
	protected ChairState chairState() {
		return Kernel.kernel().chairState();
	}
	
	/**
	 * Runs a specified script.
	 * 
	 * @param scriptId id of the script to run.
	 */
	protected void runScript(ScriptId scriptId) {

		((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(scriptId);
	}
}