package com.wisneskey.los.service.script.command;

import java.util.List;
import java.util.Random;

import com.wisneskey.los.kernel.Kernel;

/**
 * Command to write a message to be distributed to the displays.
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
public class Message extends AbstractScriptCommand {

	/**
	 * Default time between writing messages in seconds.
	 */
	public static final double DEFAULT_MESSAGE_INTERVAL = 2.0;

	/**
	 * Default maximum amount to randomly vary the message interval by.
	 */
	public static final double DEFAULT_MESSAGE_INTERVAL_VARIANCE = 0.25;

	/**
	 * Random generator to use to generate randomized message output intervals.
	 */
	private Random random = new Random();

	/**
	 * Single message to write (takes precedence over multiple messages.)
	 */
	private String singleMessage;

	/**
	 * List of messages to write one after another with a configurable delay
	 * between them.
	 */
	private List<String> messages;

	/**
	 * Time in seconds between message writes for multiple messages.
	 */
	private double messageInterval = DEFAULT_MESSAGE_INTERVAL;

	/**
	 * Maximum time in seconds to randomly vary the message interval.
	 */
	private double messageIntervalVariance = DEFAULT_MESSAGE_INTERVAL_VARIANCE;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public String getMessage() {
		return singleMessage;
	}

	public void setMessage(String message) {
		this.singleMessage = message;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public double getMessageInterval() {
		return messageInterval;
	}

	public void setMessageInterval(double messageInterval) {
		this.messageInterval = messageInterval;
	}

	public double getMessageIntervalVariance() {
		return messageIntervalVariance;
	}

	public void setMessageIntervalVariance(double messageIntervalVariance) {
		this.messageIntervalVariance = messageIntervalVariance;
	}

	// ----------------------------------------------------------------------------------------
	// ScriptCommand methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void perform() {

		if (getMessage() != null) {
			Kernel.kernel().message(getMessage());
		} else if (getMessages() != null) {

			for (int index = 0; index < messages.size(); index++) {

				Kernel.kernel().message(messages.get(index));

				// Only pause between messages; do not pause after last message.
				if (index < messages.size() - 1) {
					double variance = random.doubles(getMessageIntervalVariance() * -1.0, getMessageIntervalVariance())
							.findFirst().getAsDouble();
					sleepForSeconds(getMessageInterval() + variance);
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	// Object methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		if (getMessage() != null) {
			return "Message[" + getMessage() + "]";
		} else if (getMessages() != null) {
			return "Message[" + getMessages() + " messages]";
		} else {
			return "Message[]";
		}
	}
}
