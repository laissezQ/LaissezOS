package com.wisneskey.los.service.audio;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumerated type designating the various sound effects that can be played by
 * the chair.
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
public enum SoundEffectId {

	MISC_CAR_ALARM_ARMING(//
			"Chirp",
			"No need to be alarmed!",
			true,
			"car_alarm_arming.mp3"),

	MISC_KNIFE_BOX(//
			"Kife Box",
			"The Knife Goes In The Box",
			false,
			"knife_in_box.mp3"),

	MISC_STAND_UP(//
			"Stand Up",
			"Stand Up and Get Crunk",
			true,
			"get_crunk.mp3"),

	MISC_WHO_DAT(//
			"Who Dat",
			"Who Dat Say They Gonna Beat the Saints",
			true,
			"who_dat.mp3"),

	MISC_WINDOWS_XP_STARTUP(//
			"Startup",
			"Windows XP startup sound.",
			true,
			"windows_xp_startup.mp3"),

	MOVIE_ALL_GO_TO_ELEVEN(//
			"Eleven",
			"The numbers all go to eleven.",
			true,
			"numbers_all_go_to_eleven.mp3"),

	MOVIE_ALIEN_GAME_OVER(//
			"Game Over",
			"Game over man!",
			true,
			"alien_game_over.mp3"),

	MOVIE_BLADE_RUNNER_TIME_TO_DIE(//
			"Time To Die",
			"...time to die.",
			false,
			"blade_runner_time_to_die.mp3"),

	MOVIE_CANDLE_BACK(//
			"Candle Back",
			"Put the candle back!",
			true,
			"put_the_candle_back.mp3"),

	MOVIE_ESPRESSO(//
			"Espresso",
			"I was going to make espressso!",
			true,
			"young_frankensten_espresso.mp3"),

	MOVIE_HAL_OPERATIONAL(//
			"Hal",
			"I'm completely operational...",
			true,
			"hal_operational.mp3"),

	MOVIE_HAL_SORRY_DAVE(//
			"Sorry",
			"I'm afraid I can't do that...",
			true,
			"hal_sorry_dave.mp3"),

	MOVIE_HARRY_POTTER_CHAMBER_OF_SECRETS(//
			"Chamber",
			"The chamber of secrets has been opened...",
			true,
			"harry_potter_chamber_of_secrets.mp3"),

	MOVIE_IJ_CHOSEN_WISELY(//
			"Chosen",
			"You have chosen wisely...",
			true,
			"indianajones_chosen_wisely.mp3"),

	MOVIE_ITS_ALIVE(//
			"Alive",
			"It's alive!",
			true,
			"young_frankenstein_alive.mp3"),

	MOVIE_ITS_ONE_LOUDER(//
			"Louder",
			"It's one louder...",
			true,
			"its_one_louder.mp3"),

	MOVIE_MACHINE_GOES_PING(//
			"Ping",
			"Machine that goes ping!",
			true,
			"machine_goes_ping.mp3"),

	MOVIE_MATRIX_SEE_IT_FOR_YOURSELF(//
			"See It",
			"You have to see it for yourself...",
			false,
			"matrix_see_it_for_yourself.mp3"),

	MOVIE_MATRIX_THE_DOOR(//
			"The Door",
			"I can only show you the door...",
			false,
			"matrix_the_door.mp3"),

	MOVIE_MATRIX_WELCOME_TO_THE_REAL_WORLD(//
			"Real World",
			"Welcome to the real world...",
			false,
			"matrix_welcome_to_real_world.mp3"),

	MOVIE_NOT_MAD(//
			"Not Mad",
			"I'm not mad...I'm just disappointed.",
			true,
			"not_mad.mp3"),

	MOVIE_SHOOT_EYE(//
			"Shoot",
			"You'll shoot your eye out kid!",
			true,
			"shoot_your_eye_out_kid.mp3"),

	MOVIE_SNOW_WHITE_HI_HO(//
			"Hi Ho",
			"Hi Ho, Hi Ho, its off to work we go!",
			true,
			"snow_white_off_to_work_we_go.mp3"),

	MOVIE_THE_AUDIENCE_IS_NOW_DEAF(//
			"Deaf",
			"The audience is now deaf.",
			false,
			"the_audience_is_now_deaf.mp3"),

	MOVIE_WHAT_IS_THY_BIDDING(//
			"Bidding",
			"What is thy bidding, Master?",
			true,
			"what_is_thy_bidding_master.mp3"),

	MOVIE_WORKING(//
			"Working",
			"Never interrupt me while I'm working!",
			true,
			"young_frankenstein_working.mp3"),

	MOVIE_WRONG(//
			"Wrong",
			"Wrong!",
			true,
			"willy_wonka_wrong.mp3"),

	MOVIE_YOU_LOSE(//
			"Nothing",
			"You get nothing!",
			true,
			"willy_wonka_you_lose.mp3"),

	TV_COMPLETELY_DIFFERENT(//
			"Different",
			"Something completely different...",
			true,
			"monty_python_completely_different.mp3"),

	TV_COMFY_CHAIR(//
			"Comfy",
			"Get the comfy chair!",
			true,
			"monty_python_get_the_comfy_chair.mp3"),

	TV_NOT_STUPID(//
			"Not Stupid",
			"It's not stupid, it's advanced!",
			true,
			"zim_not_stupid.mp3"),

	TV_THATS_ALL_FOLKS(//
			"That's All",
			"Th-th-that's all folks!",
			true,
			"looney_tunes_thats_all_folks.mp3");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private String shortName;
	private String description;
	private boolean allowInChapMode;
	private String fileName;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private SoundEffectId(String shortName, String description, boolean allowInChapMode, String fileName) {
		this.shortName = shortName;
		this.description = description;
		this.allowInChapMode = allowInChapMode;
		this.fileName = fileName;
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
	 * Resource name of the MP3 file for the sound effect.
	 * 
	 * @return Name of the MP3 file for effect.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Return the description for the sound effect.
	 * 
	 * @return Sound effect description.
	 */
	public String getDescription() {
		return description;
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