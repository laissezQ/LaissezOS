package com.wisneskey.los.service.display.controller.cp;

import java.util.List;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.controller.AbstractController;
import com.wisneskey.los.service.display.listener.label.UpdateLabelListener;
import com.wisneskey.los.service.display.listener.mouse.DoubleClickListener;
import com.wisneskey.los.service.music.MusicService;
import com.wisneskey.los.service.music.Track;
import com.wisneskey.los.service.remote.RemoteButtonId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.state.MusicState;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Controller for the music playing screen.
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
public class MusicScreen extends AbstractController {

	/**
	 * Laissez Boy logo.
	 */
	@FXML
	private ImageView logo;

	/**
	 * Slider for controlling the music volume.
	 */
	@FXML
	private Slider musicVolumeSlider;

	/**
	 * Vertical box the tracks will be in.
	 */
	@FXML
	private VBox tracksBox;

	/**
	 * Label for name of artist currently playing.
	 */
	@FXML
	private Label lblArtist;

	/**
	 * Label for tile of the track that is currently playing.
	 */
	@FXML
	private Label lblTitle;

	/**
	 * Checkbox for enabling shuffle play.
	 */
	@FXML
	private CheckBox chkShuffle;

	/**
	 * Choice box for selecting playlist.
	 */
	@FXML
	private ChoiceBox<String> choicePlaylist;

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Initializes the controller.
	 */
	@FXML
	public void initialize() {

		MusicState musicState = chairState().getServiceState(ServiceId.MUSIC);
		popuateTrackList(musicState.currentPlaylistName().get());

		logo.setOnMouseClicked(new DoubleClickListener(e -> resumePressed()));

		musicState.currentTrackArtist().addListener(new UpdateLabelListener<>(lblArtist));
		musicState.currentTrackName().addListener(new UpdateLabelListener<>(lblTitle));

		MusicService service = ((MusicService) Kernel.kernel().getService(ServiceId.MUSIC));
		List<String> playlists = service.getPlaylists();
		for (String playlist : playlists) {
			choicePlaylist.getItems().add(playlist);
		}

		choicePlaylist.valueProperty().bindBidirectional(musicState.currentPlaylistName());
		choicePlaylist.valueProperty().addListener(new PlaylistChangedListener());
		
		// Bind the volume control.
		musicVolumeSlider.valueProperty().bindBidirectional(musicState.volume());
	}

	@Override
	public void remoteButtonPressed(RemoteButtonId buttonId) {

		// Allow remote button A to leave the lighting screen.
		if (buttonId == RemoteButtonId.REMOTE_BUTTON_A) {
			resumePressed();
		} else {
			super.remoteButtonPressed(buttonId);
		}
	}

	/**
	 * Method invoked when the stop button is pressed.
	 */
	public void stopPressed() {
		((MusicService) kernel().getService(ServiceId.MUSIC)).stopPlaying();
	}

	/**
	 * Method invoked if the shuffled checkbox is toggled.
	 */
	public void shufflePressed() {

		((MusicService) kernel().getService(ServiceId.MUSIC)).shufflePlay(chkShuffle.isSelected());

	}

	/**
	 * Method invoked by the resume operation button.
	 */
	public void resumePressed() {
		runScript(ScriptId.MUSIC_SCREEN_CLOSE);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void popuateTrackList(String playlistName) {

		// Remove any current tracks listed.
		tracksBox.getChildren().clear();

		if (playlistName == null) {
			return;
		}

		List<Track> tracklist = ((MusicService) Kernel.kernel().getService(ServiceId.MUSIC))
				.getPlaylistTracks(playlistName);
		for (Track track : tracklist) {

			Button trackButton = createListButton(track.getTitle());
			trackButton.setOnAction(e -> playTrack(track));
			tracksBox.getChildren().add(trackButton);

		}
	}

	/**
	 * Method invoked to play a track in response to the press of a track's
	 * button.
	 * 
	 * @param track Track to be played.
	 */
	private void playTrack(Track track) {

		((MusicService) kernel().getService(ServiceId.MUSIC)).playTrack(track.getTrackId());
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	private class PlaylistChangedListener implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			popuateTrackList(newValue);
		}
	}
}