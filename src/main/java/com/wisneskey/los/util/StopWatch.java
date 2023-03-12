package com.wisneskey.los.util;

/**
 * Utility class to time operations. Inspired by the Apache Commons Lang class
 * but cruder.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class StopWatch {

	private long start;

	public StopWatch() {
		this.start = System.currentTimeMillis();
	}

	public long elapsed() {
		return System.currentTimeMillis() - start;
	}

	public String elapsedAsString() {
		return elapsed() + " ms.";
	}
}
