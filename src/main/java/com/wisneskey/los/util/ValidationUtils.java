package com.wisneskey.los.util;

import java.util.Collection;

import com.wisneskey.los.error.LOSException;

/**
 * Utility class providing static methods to validate values.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class ValidationUtils {

	// ---------------------------------------------------------------------
	// Constructors.
	// ---------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation.
	 */
	private ValidationUtils() {
	}

	// ---------------------------------------------------------------------
	// Property getters/setters.
	// ---------------------------------------------------------------------

	/**
	 * Checks the specified value to make sure it meets the following conditions:
	 * <ul>
	 * <li>Not-null.
	 * <li>If it is a string, it is not empty.
	 * <li>If it is a collection, it has at least one element.
	 * </ul>
	 * 
	 * @param value
	 *          Value to verify.
	 * @param failureMessage
	 *          Message to use in thrown exception if validation fails.
	 */
	public static void requireValue(Object value, String failureMessage) {

		boolean valid = false;
		if (value == null) {
			valid = false;
		} else {
			if (value instanceof String) {
				valid = String.class.cast(value).length() > 0;
			} else if (value instanceof Collection<?>) {
				valid = Collection.class.cast(value).size() > 0;
			} else {
				valid = true;
			}
		}

		if (!valid) {
			throw new LOSException(failureMessage);
		}
	}
}
