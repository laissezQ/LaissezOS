package com.wisneskey.los.service.audio;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
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
public enum SoundEffectId {

	MISC_CAR_ALARM_ARMING( //
			"Chirp",
			"No need to be alarmed!",
			true,
			"misc/car_alarm_arming.wav"),

	MISC_KNIFE_BOX( //
			"Kife Box",
			"The Knife Goes In The Box",
			false,
			"misc/knife_in_box.wav"),

	MISC_STAND_UP( //
			"Stang Up",
			"Stand Up and Get Crunk",
			true,
			"misc/get_crunk.wav"),

	MISC_WHO_DAT( //
			"Who Dat",
			"Who Dat Say They Gonna Beat the Saints",
			true,
			"misc/who_dat.wav"),

	MISC_WINDOWS_XP_STARTUP( //
			"Startup",
			"Windows XP startup sound.",
			true,
			"misc/windows_xp_startup.wav"),

	MOVIE_ALL_GO_TO_ELEVEN( //
			"Eleven",
			"The numbers all go to eleven.",
			true,
			"movie/numbers_all_go_to_eleven.wav"),

	MOVIE_ALIEN_GAME_OVER( //
			"Game Over",
			"Game over man!",
			true,
			"movie/alien_game_over.wav"),

	MOVIE_BLADE_RUNNER_TIME_TO_DIE( //
			"Time To Die",
			"...time to die.",
			false,
			"movie/blade_runner_time_to_die.wav"),

	MOVIE_CANDLE_BACK( //
			"Candle Back",
			"Put the candle back!",
			true,
			"movie/put_the_candle_back.wav"),

	MOVIE_ESPRESSO( //
			"Espresso",
			"I was going to make espressso!",
			true,
			"movie/young_frankensten_espresso.wav"),

	MOVIE_HAL_OPERATIONAL( //
			"Hal",
			"I'm completely operational...",
			true,
			"movie/hal_operational.wav"),

	MOVIE_HAL_SORRY_DAVE( //
			"Sorry",
			"I'm afraid I can't do that...",
			true,
			"movie/hal_sorry_dave.wav"),

	MOVIE_HARRY_POTTER_CHAMBER_OF_SECRETS( //
			"Chamber",
			"The chamber of secrets has been opened...",
			true,
			"movie/harry_potter_chamber_of_secrets.wav"),

	MOVIE_IJ_CHOSEN_WISELY( //
			"Chosen",
			"You have chosen wisely...",
			true,
			"movie/indianajones_chosen_wisely.wav"),

	MOVIE_ITS_ALIVE( //
			"Alive",
			"It's alive!",
			true,
			"movie/young_frankenstein_alive.wav"),

	MOVIE_ITS_ONE_LOUDER( //
			"Louder",
			"It's one louder...",
			true,
			"movie/its_one_louder.wav"),

	MOVIE_MACHINE_GOES_PING( //
			"Ping",
			"Machine that goes ping!",
			true,
			"movie/machine_goes_ping.wav"),

	MOVIE_MATRIX_SEE_IT_FOR_YOURSELF( //
			"See It",
			"You have to see it for yourself...",
			false,
			"movie/matrix_see_it_for_yourself.wav"),

	MOVIE_MATRIX_THE_DOOR( //
			"The Door",
			"I can only show you the door...",
			false,
			"movie/matrix_the_door.wav"),

	MOVIE_MATRIX_WELCOME_TO_THE_REAL_WORLD( //
			"Real World",
			"Welcome to the real world...",
			false,
			"movie/matrix_welcome_to_real_world.wav"),

	MOVIE_NOT_MAD( //
			"Not Mad",
			"I'm not mad...I'm just disappointed.",
			true,
			"movie/not_mad.wav"),

	MOVIE_SHOOT_EYE( //
			"Shoot",
			"You'll shoot your eye out kid!",
			true,
			"movie/shoot_your_eye_out_kid.wav"),

	MOVIE_SNOW_WHITE_HI_HO( //
			"Hi Ho",
			"Hi Ho, Hi Ho, its off to work we go!",
			true,
			"movie/snow_white_off_to_work_we_go.wav"),

	MOVIE_THE_AUDIENCE_IS_NOW_DEAF( //
			"Deaf",
			"The audience is now deaf.",
			false,
			"movie/the_audience_is_now_deaf.wav"),

	MOVIE_WHAT_IS_THY_BIDDING( //
			"Bidding",
			"What is thy bidding, Master?",
			true,
			"movie/what_is_thy_bidding_master.wav"),

	MOVIE_WORKING( //
			"Working",
			"Never interrupt me while I'm working!",
			true,
			"movie/young_frankenstein_working.wav"),

	MOVIE_WRONG( //
			"Wrong",
			"Wrong!",
			true,
			"movie/willy_wonka_wrong.wav"),

	MOVIE_YOU_LOSE( //
			"Nothing",
			"You get nothing!",
			true,
			"movie/willy_wonka_you_lose.wav"),

	TV_COMPLETELY_DIFFERENT( //
			"Different",
			"Something completely different...",
			true,
			"tv/monty_python_completely_different.wav"),

	TV_COMFY_CHAIR( //
			"Comfy",
			"Get the comfy chair!",
			true,
			"tv/monty_python_get_the_comfy_chair.wav"),

	TV_NOT_STUPID( //
			"Not Stupid",
			"It's not stupid, it's advanced!",
			true,
			"tv/zim_not_stupid.wav"),

	TV_THATS_ALL_FOLKS( //
			"That's All",
			"Th-th-that's all folks!",
			true,
			"tv/looney_tunes_thats_all_folks.wav");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String shortName;
	private String description;
	private boolean allowInChapMode;
	private String resourcePath;
	private Float clipGain = Float.valueOf(0.0f);

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffectId(String shortName, String description, boolean allowInChapMode, String resourcePath) {
		this.shortName = shortName;
		this.description = description;
		this.allowInChapMode = allowInChapMode;
		this.resourcePath = resourcePath;
	}

	private SoundEffectId(String shortName, String description, boolean allowInChapMode, String resourcePath,
			Float clipGain) {
		this.shortName = shortName;
		this.description = description;
		this.allowInChapMode = allowInChapMode;
		this.resourcePath = resourcePath;
		this.clipGain = clipGain;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the short name for the sound effect.
	 * 
	 * @return Short name of sound effect.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Return a boolean indicating if the sound effect can be used in chap mode.
	 * 
	 * @return True iff sound effect can be used in chap mode.
	 */
	public boolean getAllowInChapMode() {
		return allowInChapMode;
	}

	/**
	 * Resource path to the WAV file for the sound effect.
	 * 
	 * @return Path to the WAV file for effect.
	 */
	public String getResourcePath() {
		return resourcePath;
	}

	/**
	 * Return the description for the sound effect.
	 * 
	 * @return Sound effect description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Return the optional clip specific gain.
	 * 
	 * @return
	 */
	public Float clipGain() {
		return clipGain;
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Returns the set of all effects allowed in chap mode.
	 * 
	 * @return Set of effect id's for all effects allowed in chap mode.
	 */
	public static Set<SoundEffectId> chapModeEffects() {

		return Arrays.stream(SoundEffectId.values()).filter(SoundEffectId::getAllowInChapMode).collect(Collectors.toSet());
	}
}