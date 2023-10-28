package com.wisneskey.los.service.security;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.display.DisplayService;
import com.wisneskey.los.service.display.SceneId;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;
import com.wisneskey.los.state.ChairState.MasterState;
import com.wisneskey.los.state.DisplayState;
import com.wisneskey.los.state.SecurityState;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 * Service for managing the security of the chair operating system.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class SecurityService extends AbstractService<SecurityState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

	/**
	 * Default lock screen method.
	 */
	public static final String DEFAULT_LOCK_MESSAGE = "SYSTEM LOCKED";

	/**
	 * Object for the state of the audio service.
	 */
	private InternalSecurityState securityState;

	/**
	 * 4 digit code required to unlock chair.
	 */
	private String pinCode;

	/**
	 * Optional script to use for lock operation.
	 */
	private ScriptId unlockedScript;

	/**
	 * Optional script to use for when an unlock operation failes.
	 */
	private ScriptId unlockFailedScript;

	/**
	 * Optional scene to set HUD to it when unlocking (if no unlock script
	 * specified).
	 */
	private SceneId unlockSceneHUD;

	/**
	 * Optional scene to set control panel to when unlocking (if not unlock script
	 * specified).
	 */
	private SceneId unlockSceneCP;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private SecurityService() {
		super(ServiceId.SECURITY);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void terminate() {
		LOGGER.trace("Security service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public void lockChair(String lockMessage, ScriptId unlockScript, ScriptId unlockFailedScript, SceneId hudScene,
			SceneId cpScene) {

		if (Kernel.kernel().chairState().masterState().getValue() == MasterState.LOCKED) {
			// Chair already locked.
			LOGGER.info("Lock chair request: chair already locked.");
			return;
		}

		LOGGER.info("Lock chair request: locking chair...");

		// Set the lock message.
		securityState.setLockMessage(lockMessage);

		// Save the lock scripts
		this.unlockedScript = unlockScript;
		this.unlockFailedScript = unlockFailedScript;

		// If scenes are specified, use them as the unlock scenes to use if an
		// unlock
		// script is not provided. If no scenes are provided, the unlock scenes will
		// default to going back to the scenes active when the chair was locked
		// (unless
		// an unlock script is provided). An unlock script takes precedence over
		// these
		// scenes.
		DisplayState displayState = Kernel.kernel().chairState().getServiceState(ServiceId.DISPLAY);
		unlockSceneCP = cpScene != null ? cpScene : displayState.cpScene().getValue();
		unlockSceneHUD = hudScene != null ? hudScene : displayState.hudScene().getValue();

		// Lock the chair.
		Kernel.kernel().message("System locked.\n");
		Kernel.kernel().setMasterState(MasterState.LOCKED);

		// Now display lock screens.
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.CP_LOCK_SCREEN);
		((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(SceneId.HUD_LOCK_SCREEN);
	}

	public boolean unlockChair(String pinEntered) {

		if (Objects.equals(pinCode, pinEntered)) {

			// The PIN is correct so we can unlock the chair and play the unlock
			// script if there is one. If there is not one, we will just got back to
			// the main panel.
			Kernel.kernel().message("System unlocked.\n");
			Kernel.kernel().setMasterState(MasterState.RUNNING);
			if (unlockedScript != null) {

				LOGGER.info("Unlock chair request: unlocking chair with script...");
				((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(unlockedScript);

			} else {

				LOGGER.info("Unlock chair request: unlocking chair...");
				((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(unlockSceneCP);
				((DisplayService) Kernel.kernel().getService(ServiceId.DISPLAY)).showScene(unlockSceneHUD);
			}

			return true;
		} else {
			// PIN code was incorrect so play the unlock failed script (if there is
			// one).
			LOGGER.info("Unlock chair request: invalid PIN...");

			Kernel.kernel().message("System unlock denied: invalid PIN.\n");
			if (unlockFailedScript != null) {
				((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(unlockFailedScript);
			}

			return false;
		}
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates the initial state of the service using the supplied profile for
	 * configuration.
	 * 
	 * @param  profile Profile to use for configuring initial service state.
	 * @return         Configured state object for the service.
	 */
	private SecurityState createInitialState(Profile profile) {
		securityState = new InternalSecurityState();
		return securityState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the security service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  profile Profile to use for configuring initial state of the
	 *                   security service.
	 * @return         Security service instance and its initial state object.
	 */
	public static Pair<SecurityService, SecurityState> createService(Profile profile) {

		SecurityService service = new SecurityService();
		service.pinCode = profile.getPinCode();

		SecurityState state = service.createInitialState(profile);
		return new Pair<>(service, state);
	}

	/**
	 * Internal state object for the Security service.
	 */
	private static class InternalSecurityState implements SecurityState {

		private StringProperty lockMessage = new SimpleStringProperty(DEFAULT_LOCK_MESSAGE);

		// ----------------------------------------------------------------------------------------
		// SecurityState methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyStringProperty lockMessage() {

			return lockMessage;
		}

		// ----------------------------------------------------------------------------------------
		// Private methods.
		// ----------------------------------------------------------------------------------------

		/**
		 * Set the message to displayed on the lock screen.
		 * 
		 * @param message Message to display on the lock screen.
		 */
		private void setLockMessage(String message) {
			this.lockMessage.setValue(message);
		}
	}

}
