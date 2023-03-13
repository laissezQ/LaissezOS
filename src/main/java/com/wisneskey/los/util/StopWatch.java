package com.wisneskey.los.util;

/**
 * Utility class to time operations. Inspired by the Apache Commons Lang class
 * but cruder.
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
