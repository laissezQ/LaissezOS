package com.wisneskey.los.service.display.controller.hud;

import com.wisneskey.los.service.display.controller.AbstractController;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Controller for the heads up display main screen.
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
public class ChapScreen extends AbstractController {

	/**
	 * Canvas the drawing is done on.
	 */
	@FXML
	private Canvas canvas;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		clearDrawing();

		canvas.setOnMouseDragged((event) -> {
			GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			graphicsContext.setFill(Color.BLACK);
			graphicsContext.fillOval(event.getX(), event.getY(), 8, 8);
		});
	}

	/**
	 * Method invoked by the clear the Etch-a-Sketch drawing.  Triggered by a track on
	 * the top bar of the UI.
	 */
	@FXML
	public void clearDrawing() {

		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.LIGHTGRAY);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}