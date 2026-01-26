package com.wisneskey.los.service.display.controller.hud;

import java.util.Random;

import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffectId;
import com.wisneskey.los.service.display.controller.AbstractController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Controller for the heads up display game screen.
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
public class GameScreen extends AbstractController {

	/**
	 * Image of the knife.
	 */
	@FXML
	private ImageView knife;

	/**
	 * Image of the king cake.
	 */
	@FXML
	private ImageView kingCake;

	/**
	 * Actual play area for the game.
	 */
	@FXML
	private Pane gamePane;

	/**
	 * Label used for Paul's score.
	 */
	@FXML
	private Label paulScoreLabel;

	/**
	 * Starting X position of the knife when it is dragged.
	 */
	private double baseX = 0.0d;

	/**
	 * Starting Y position of the knife when it is dragged.
	 */
	private double baseY = 0.0d;

	/**
	 * Paul's score.
	 */
	private int paulScore = 0;

	/**
	 * Random number generator for relocating the king cake after the knife is
	 * dropped on it.
	 */
	private Random random = new Random();

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Method invoked when the mouse is pressed on the knife to start dragging it.
	 * 
	 * @param event Mouse press event.
	 */
	public void knifePressed(MouseEvent event) {

		baseX = knife.getLayoutX() - event.getSceneX();
		baseY = knife.getLayoutY() - event.getSceneY();
	}

	/**
	 * Method invoked when the knife image is dragged.
	 * 
	 * @param event Mouse drag event.
	 */
	public void knifeDragged(MouseEvent event) {

		double newX = event.getSceneX() + baseX;
		double newY = event.getSceneY() + baseY;

		newX = Math.max(0.0d, newX);
		newY = Math.max(0.0d, newY);

		newX = Math.min(newX, gamePane.getWidth() - knife.getFitWidth());
		newY = Math.min(newY, gamePane.getHeight() - knife.getFitHeight());

		knife.setLayoutX(newX);
		knife.setLayoutY(newY);
	}

	/**
	 * Method invoked when the knife image is released.
	 * 
	 * @param event Mouse release event.
	 */
	public void knifeReleased(MouseEvent event) {

		// Process final position for release.
		knifeDragged(event);

		if (knifeOnKingCake()) {

			// Play the sound effect and move the king cake.
			((AudioService) kernel().getService(ServiceId.AUDIO)).playEffect(SoundEffectId.MISC_KNIFE_BOX, false);

			while (knifeOnKingCake()) {

				kingCake.setLayoutX(random.nextDouble() * (gamePane.getWidth() - kingCake.getFitWidth()));
				kingCake.setLayoutY(random.nextDouble() * (gamePane.getHeight() - kingCake.getFitHeight()));
			}

			paulScore += 1;
			paulScoreLabel.setText(String.valueOf(paulScore));
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Function to check to see if the knife image overlaps the king cake image at all.
	 * 
	 * @return True if and only if knife image overlaps the king cake.
	 */
	private boolean knifeOnKingCake() {

		// Now see if knife overlaps king cake at all.
		if (((knife.getLayoutX() + knife.getFitWidth()) < kingCake.getLayoutX())
				|| ((knife.getLayoutX() > kingCake.getLayoutX() + kingCake.getFitWidth()))) {
			return false;
		}

		if (((knife.getLayoutY() + knife.getFitHeight()) < kingCake.getLayoutY())
				|| ((knife.getLayoutY() > kingCake.getLayoutY() + kingCake.getFitHeight()))) {
			return false;
		}

		return true;
	}
}