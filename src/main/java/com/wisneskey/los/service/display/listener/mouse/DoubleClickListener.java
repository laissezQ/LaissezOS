package com.wisneskey.los.service.display.listener.mouse;

import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Event handler for mouse events that performs an action upon a double click.
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
public class DoubleClickListener implements EventHandler<MouseEvent> {
	
	private Consumer<MouseEvent> consumer;
	
	public DoubleClickListener(Consumer<MouseEvent> consumer) {
		this.consumer = consumer;
	}
	
	/**
	 * Mouse event handler that exits the secret system menu if the logo is
	 * double-clicked.
	 */

	public void handle(MouseEvent mouseEvent) {

		if ((mouseEvent.getButton().equals(MouseButton.PRIMARY)) && (mouseEvent.getClickCount() == 2)) {

			consumer.accept(mouseEvent);
		}
	}
}
