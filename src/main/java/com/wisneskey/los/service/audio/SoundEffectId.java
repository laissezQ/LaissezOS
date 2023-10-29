package com.wisneskey.los.service.audio;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
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
public enum SoundEffectId {

	MISC_WINDOWS_XP_STARTUP("misc/windows_xp_startup.wav", "Windows XP startup sound."),
	MISC_PIXABAY_SHUTDOWN("misc/pixabay_shutdown.wav", "Shut it all down."),

	MOVIE_ALIEN_GAME_OVER("movie/alien_game_over.wav", "Game over man!"),
	MOVIE_BLADE_RUNNER_TIME_TO_DIE("movie/blade_runner_time_to_die.wav", "...time to die."),
	MOVIE_ESPRESSO("movie/young_frankensten_espresso.wav", "I was going to make espressso!"),
	MOVIE_HAL_OPERATIONAL("movie/hal_operational.wav", "I'm completely operational..."),
	MOVIE_HAL_SORRY_DAVE("movie/hal_sorry_dave.wav", "I'm afraid I can't do that..."),
	MOVIE_HARRY_POTTER_CHAMBER_OF_SECRETS("movie/harry_potter_chamber_of_secrets.wav", "The chamber of secrets has been opened..."),
	MOVIE_IJ_CHOSEN_WISELY("movie/indianajones_chosen_wisely.wav", "You have chosen wisely..."),
	MOVIE_ITS_ALIVE("movie/young_frankenstein_alive.wav", "It's alive!"),
	MOVIE_MACHINE_GOES_PING("movie/machine_goes_ping.wav", "Machine that goes ping!"),
	MOVIE_MATRIX_SEE_IT_FOR_YOURSELF("movie/matrix_see_it_for_yourself.wav","You have to see it for yourself..."),
	MOVIE_MATRIX_THE_DOOR("movie/matrix_the_door.wav", "I can only show you the door..."),
	MOVIE_MATRIX_WELCOME_TO_THE_REAL_WORLD("movie/matrix_welcome_to_real_world.wav", "Welcome to the real world..."),
	MOVIE_WHAT_IS_THY_BIDDING("movie/what_is_thy_bidding_master.wav", "What is thy bidding, Master?"),
	MOVIE_WORKING("movie/young_frankenstein_working.wav", "Never interrupt me while I'm working!"),
	MOVIE_SNOW_WHITE_HI_HO("movie/snow_white_off_to_work_we_go.wav", "Hi Ho, Hi Ho, its off to work we go!"),

	TV_COMPLETELY_DIFFERENT("tv/monty_python_completely_different.wav", "And now for something completely different..."),
	TV_COMFY_CHAIR("tv/monty_python_get_the_comfy_chair.wav", "Get the comfy chair!"),
	TV_THATS_ALL_FOLKS("tv/looney_tunes_thats_all_folks.wav", "Th-th-that's all folks!");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String resourcePath;
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffectId(String resourcePath, String description) {
		this.resourcePath = resourcePath;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public String getResourcePath() {
		return resourcePath;
	}

	public String getDescription() {
		return description;
	}
}
