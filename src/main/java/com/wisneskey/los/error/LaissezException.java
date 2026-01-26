package com.wisneskey.los.error;

/**
 * Basic exception class for LBOS exceptions.
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
	 * @param message Message for the exception.
	 */
	public LaissezException(String message) {
		super(message);
	}

	/**
	 * Constructor for an exception with a wrapped exception.
	 * 
	 * @param message Message for the exception.
	 * @param cause   Exception to wrap.
	 */
	public LaissezException(String message, Throwable cause) {
		super(message, cause);
	}
}
