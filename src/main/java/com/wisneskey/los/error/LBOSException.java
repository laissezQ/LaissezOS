package com.wisneskey.los.error;

/**
 * Basic exception class for LBOS exceptions.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LBOSException extends RuntimeException {

	/**
	 * Version id for serialization.
	 */
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public LBOSException() {
		super();
	}

	public LBOSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LBOSException(String message, Throwable cause) {
		super(message, cause);
	}

	public LBOSException(String message) {
		super(message);
	}

	public LBOSException(Throwable cause) {
		super(cause);
	}
}
