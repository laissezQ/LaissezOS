package com.wisneskey.los;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.boot.BootConfiguration;
import com.wisneskey.los.boot.BootLoader;
import com.wisneskey.los.kernel.RunMode;

/**
 * Laissez Boy Operating System
 * 
 * A JavaFX application for monitoring and controlling the operation of Q's
 * Laissez Boy in a theatrical fashion.
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
public class LaissezOS {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaissezOS.class);

	// ----------------------------------------------------------------------------------------
	// Main method.
	// ----------------------------------------------------------------------------------------

	/**
	 * Main entry point for starting the application.
	 * 
	 * @param args Command line arguments to pass into the boot loader.
	 */
	public static void main(String[] args) {

		LOGGER.info("LaissezOS starting: args={}", (Object[]) args);

		// Set up the configuration for the boot loader.
		BootConfiguration bootConfig = new BootConfiguration();

		// Parse the CLI to set the boot configuration
		try {
			parseCommandLine(args, bootConfig);
		} catch (Exception t) {
			LOGGER.error("Failed to parse command line: {}", t.getMessage());
			return;
		}

		BootLoader loader = new BootLoader();
		loader.boot(bootConfig);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Parses the command line option to set the initial boot configuration.
	 * 
	 * @param  args           Command line arguments to parse.
	 * @param  bootConfig     Boot configuration set from any supplied command
	 *                          line options.
	 * @throws ParseException If the supplied options were invalid.
	 */
	private static void parseCommandLine(String[] args, BootConfiguration bootConfig) throws ParseException {

		Options options = new Options();
		options.addOption("m", true, "Set the run mode (default is DEV)");
		options.addOption("p", true, "Use profile in place of last active profile.");

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = parser.parse(options, args);

		if (commandLine.hasOption('m')) {
			RunMode mode = RunMode.valueOf(commandLine.getOptionValue('m'));
			bootConfig.setRunMode(mode);
		}

		if (commandLine.hasOption('p')) {
			bootConfig.setProfileName(commandLine.getOptionValue('p'));
		}
	}
}
