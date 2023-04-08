package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	// Driver board A relays:
	BAR_LOWER(0, "Lower armrest bar."),
	BAR_RAISE(1, "Raise armest bar."),
	BACKREST_RAISE(2, "Raise backrest."),
	BACKREST_LOWER(3, "Lower backrest."),
	FOOTREST_LOWER(4, "Lower footrest."),
	FOOTREST_RAISE(5, "Raise footrest."),
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
