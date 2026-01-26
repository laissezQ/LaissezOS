package com.wisneskey.los.util;

import java.util.Collection;

import com.wisneskey.los.error.LaissezException;

/**
 * Utility class providing static methods to validate values.
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
	 * <li>If it is a collection, it has at least one element.</ul>
	 * 
	 * @param value          Value to verify.
	 * @param failureMessage Message to use in thrown exception if validation
	 *                         fails.
	 */
	public static void requireValue(Object value, String failureMessage) {

		boolean valid = false;
		if (value == null) {
			valid = false;
		} else {
			if (value instanceof String) {
				valid = String.class.cast(value).length() > 0;
			} else if (value instanceof Collection<?>) {
				valid = !Collection.class.cast(value).isEmpty();
			} else {
				valid = true;
			}
		}

		if (!valid) {
			throw new LaissezException(failureMessage);
		}
	}
}
