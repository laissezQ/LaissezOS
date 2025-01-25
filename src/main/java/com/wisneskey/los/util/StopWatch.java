package com.wisneskey.los.util;

/**
 * Utility class to time operations. Inspired by the Apache Commons Lang class
 * but cruder.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
public class StopWatch {

	/**
	 * Current time in milliseconds that the stop watch was created.
	 */
	private long start;

	public StopWatch() {
		this.start = System.currentTimeMillis();
	}

	/**
	 * Returns the number of milliseconds that have elapsed since the stop watch
	 * was created.
	 * 
	 * @return Number of milliseconds since stop watch creation.
	 */
	public long elapsed() {
		return System.currentTimeMillis() - start;
	}

	/**
	 * Returns the number of milliseconds that have elapsed since the stop watch
	 * was created as a string value.
	 * 
	 * @return String reporting the number of milliseconds since stop watch
	 *         creation.
	 */
	public String elapsedAsString() {
		return elapsed() + " ms.";
	}
}
