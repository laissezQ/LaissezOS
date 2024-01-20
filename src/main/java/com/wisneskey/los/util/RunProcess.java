package com.wisneskey.los.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Utility class for running a process and capturing its output to the debug
 * log.
 *
 * Copyright (C) 2024 Paul Wisneskey
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
public class RunProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(RunProcess.class);

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation.
	 */
	private RunProcess() {

	}

	// ----------------------------------------------------------------------------------------
	// Static methods.
	// ----------------------------------------------------------------------------------------

	public static void runCommand(String... command) {
		
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(System.getProperty("user.home")));
		
		Process process;
		try {
			process = processBuilder.start();
			new ProcessLogger(process.getInputStream());
		} catch( IOException e ) {
			LOGGER.error("Failed to run command.", e);
		} 
	}

	// ----------------------------------------------------------------------------------------
	// Inner Classes.
	// ----------------------------------------------------------------------------------------

	private static class ProcessLogger implements Runnable {
		private InputStream inputStream;

		public ProcessLogger(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public void run() {
			new BufferedReader( //
					new InputStreamReader(inputStream)) //
							.lines().forEach(LOGGER::info);
		}
	}
}
