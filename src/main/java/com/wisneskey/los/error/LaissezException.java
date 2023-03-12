package com.wisneskey.los.error;

/**
 * Basic exception class for LBOS exceptions.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class LaissezException extends RuntimeException {

	/**
	 * Version id for serialization.
	 */
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Constructor for an exception with just a message.
	 * 
	 * @param message
	 *          Message for the exception.
	 */
	public LaissezException(String message) {
		super(message);
	}

	/**
	 * Constructor for an exception with a wrapped exception.
	 * 
	 * @param message
	 *          Message for the exception.
	 * @param cause
	 *          Exception to wrap.
	 */
	public LaissezException(String message, Throwable cause) {
		super(message, cause);
	}
}
