package com.wisneskey.los.error;

/**
 * Basic exception class for LBOS exceptions.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LOSException extends RuntimeException {

	/**
	 * Version id for serialization.
	 */
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public LOSException() {
		super();
	}

	public LOSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LOSException(String message, Throwable cause) {
		super(message, cause);
	}

	public LOSException(String message) {
		super(message);
	}

	public LOSException(Throwable cause) {
		super(cause);
	}
}
