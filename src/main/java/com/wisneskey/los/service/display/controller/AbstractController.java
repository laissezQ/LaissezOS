package com.wisneskey.los.service.display.controller;

import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.state.ChairState;

/**
 * Abstract base classes for JavaFX controllers that provides utility methods
 * for accessing chair state and services.
 * 
 * @author paul.wisneskey@gmail.com
 */
public abstract class AbstractController {

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the kernel.
	 * 
	 * @return Kernel for the chair's operating system.
	 */
	protected Kernel kernel() {
		return Kernel.kernel();
	}

	/**
	 * Return the chair state object.
	 * 
	 * @return Top level state object for the chair.
	 */
	protected ChairState chairState() {
		return Kernel.kernel().chairState();
	}
}
