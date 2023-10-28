package com.wisneskey.los.service.display.listener.script;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.script.ScriptId;
import com.wisneskey.los.service.script.ScriptService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * Listener for a pressed event that starts a script relay when a control is
 * pressed.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class RunScriptWhenPressedListener implements ChangeListener<Boolean> {

	/**
	 * Id of the script to run.
	 */
	private ScriptId scriptId;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public RunScriptWhenPressedListener(ScriptId scriptId) {
		this.scriptId = scriptId;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean pressed) {

		if (pressed.booleanValue()) {
			((ScriptService) Kernel.kernel().getService(ServiceId.SCRIPT)).runScript(scriptId);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Static utility method to add the listener to a JavaFX node.
	 * 
	 * @param node
	 *          Node to add the listener to.
	 * @param scriptId
	 *          Id of the relay to toggle.
	 */
	public static void add(Node node, ScriptId scriptId) {
		node.pressedProperty().addListener(new RunScriptWhenPressedListener(scriptId));
	}

}