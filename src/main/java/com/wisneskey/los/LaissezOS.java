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
import com.wisneskey.los.kernel.LOSKernel.RunMode;

/**
 * Laissez Boy Operating System
 * 
 * A JavaFX application for monitoring and controlling the operation of Q's
 * Laissez Boy in a theatrical fashion.
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
	 * @param args
	 *          Command line arguments to pass into the boot loader.
	 */
	public static void main(String[] args) {

		LOGGER.info("LaissezOS starting: args={}", (Object[]) args);

		// Set up the configuration for the boot loader.
		BootConfiguration bootConfig = new BootConfiguration();

		// Parse the CLI to set the boot configuration
		try {
			parseCommandLine(args, bootConfig);
		} catch (Throwable t) {
			LOGGER.error("Failed to parse command line: " + t.getMessage());
			return;
		}

		BootLoader loader = new BootLoader();

		loader.boot(bootConfig);

		LOGGER.info("LaissesOS shutdown normally.");
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private static void parseCommandLine(String[] args, BootConfiguration bootConfig) throws ParseException {

		Options options = new Options();
		options.addOption("m", true, "Set the run mode: valid values=DEV, CHAIR (default is DEV)");
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
