package com.wisneskey.los.service.script.command;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.audio.AudioService;
import com.wisneskey.los.service.audio.SoundEffectId;

/**
 * Script command to play a sound effect. If a set of sound effect ids is
 * provided, then one is selected at random to be played.
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
public class PlaySoundEffect extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaySoundEffect.class);

	/**
	 * Default value of the wait for completion flag.
	 */
	private static final boolean DEFAULT_WAIT_FOR_COMPLETION = false;

	/**
	 * Random generator to use to randomly select audio clips.
	 */
	private Random random = new Random();

	/**
	 * Id of a single sound effect to play.
	 */
	private SoundEffectId effectId;

	/**
	 * List of sound effects ids to pick one randomly to play.
	 */
	private List<SoundEffectId> effectIds;

	/**
	 * Flag indicating if the command should wait for the audio clip playback to
	 * complete before ending execution.
	 */
	private boolean waitForCompletion = DEFAULT_WAIT_FOR_COMPLETION;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public SoundEffectId getEffectId() {
		return effectId;
	}

	public void setEffectId(SoundEffectId effectId) {
		this.effectId = effectId;
	}

	public List<SoundEffectId> getEffectIds() {
		return effectIds;
	}

	public void setEffectIds(List<SoundEffectId> effectIds) {
		this.effectIds = effectIds;
	}

	public boolean isWaitForCompletion() {
		return waitForCompletion;
	}

	public void setWaitForCompletion(boolean waitForCompletion) {
		this.waitForCompletion = waitForCompletion;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		SoundEffectId playId = getEffectId();
		if (playId == null) {
			// No single sound effect specified so pick one at random.
			if ((effectIds == null) || effectIds.isEmpty()) {
				LOGGER.warn("No sound effects configured to play.");
			} else {
				int randomIndex = random.nextInt(effectIds.size());
				playId = effectIds.get(randomIndex);
			}
		}

		if (playId != null) {
			((AudioService) Kernel.kernel().getService(ServiceId.AUDIO)).playEffect(playId, waitForCompletion);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return effectId != null ? "PlaySoundEffect[effect=" + effectId + "]" : //
				"PlaySoundEffect[randomEffectFrom=" + effectIds + "]";
	}
}
