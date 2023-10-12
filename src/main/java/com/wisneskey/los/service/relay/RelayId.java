package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	// Driver board A relays:
	RELAY_0(0, "Raise backrest."),
	RELAY_1(1, "Lower backrest."),
	RELAY_2(2, "Lower footrest."),
	RELAY_3(3, "Raise footrest."),
	BAR_LOWER(4, "Lower armrest bar.."),
	BAR_RAISE(5, "Raise armest bar."),
	RELAY_7(6, "Seventh relay."),
	RELAY_8(7, "Eight relay."),

	// Driver board B relays:
	RELAY_9(8, "Ninth relay."),
	RELAY_10(9, "Tenth relay."),
	RELAY_11(10, "Eleventh relay."),
	RELAY_12(11, "Twelf relay."),
	RELAY_13(12, "Thirteenth relay."),
	RELAY_14(13, "Fourteenth relay."),
	RELAY_15(14, "Fifteenth relay."),
	RELAY_16(15, "Sixteenth relay.");

	// ----------------------------------------------------------------------------------------
	// Variables.
	// ----------------------------------------------------------------------------------------

	private int index;
	private String description;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	private RelayId(int index, String description) {
		this.index = index;
		this.description = description;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public int getIndex() {
		return index;
	}

	public String getDescription() {
		return description;
	}
}
