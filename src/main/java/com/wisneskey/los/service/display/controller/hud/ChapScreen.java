package com.wisneskey.los.service.display.controller.hud;

import com.wisneskey.los.service.display.controller.AbstractController;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Controller for the heads up display main screen.
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
public class ChapScreen extends AbstractController {

	/**
	 * Constant used to indication no line being drawn.
	 */
	private static final double NO_POSITION = -1.0d;
	
	/**
	 * Canvas the drawing is done on.
	 */
	@FXML
	private Canvas canvas;

	/**
	 * Last known X position of line being drawn.
	 */
	private double lastX = NO_POSITION;

	/**
	 * Last known Y position of line being drawn.
	 */
	private double lastY = NO_POSITION;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		clearDrawing();

		canvas.setOnMouseDragged(this::dragEvent);
		canvas.setOnDragDone(e -> stopDrawing());
		canvas.setOnMouseReleased(e -> stopDrawing());
	}

	/**
	 * Method invoked by the clear the Etch-a-Sketch drawing. Triggered by a track
	 * on the top bar of the UI.
	 */
	@FXML
	public void clearDrawing() {

		stopDrawing();

		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.LIGHTGRAY);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Draws a line from the previous last known position to the new one if a line
	 * was actively being drawn. If a line was not being drawn, it starts one.
	 * 
	 * @param event Mouse drag event.
	 */
	private void dragEvent(MouseEvent event) {

		if ((lastX > NO_POSITION) && (lastY > NO_POSITION)) {
			GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			graphicsContext.setFill(Color.BLACK);
			graphicsContext.setLineWidth(8.0d);
			graphicsContext.strokeLine(lastX, lastY, event.getX(), event.getY());
		}

		lastX = event.getX();
		lastY = event.getY();
	}

	/**
	 * Set the last position variables to indicate that no active drawing is going
	 * on.
	 */
	private void stopDrawing() {
		lastX = NO_POSITION;
		lastY = NO_POSITION;
	}
}