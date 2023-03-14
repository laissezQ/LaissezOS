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
 * @author paul.wisneskey@gmail.com
 */
public class PlaySoundEffect extends AbstractScriptCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaySoundEffect.class);

	/**
	 * Id of a single sound effect to play.
	 */
	private SoundEffectId effectId;

	/**
	 * List of sound effects ids to pick one randomly to play.
	 */
	private List<SoundEffectId> effectIds;

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
				Random random = new Random();
				int randomIndex = random.nextInt(effectIds.size());
				playId = effectIds.get(randomIndex);
			}
		}

		if (playId != null) {
			((AudioService) Kernel.kernel().getService(ServiceId.AUDIO)).playEffect(playId);
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
