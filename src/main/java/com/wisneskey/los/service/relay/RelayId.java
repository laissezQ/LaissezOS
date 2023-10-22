package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	// Driver board A relays:
	RELAY_0(0, "First relay."),
	RELAY_1(1, "Second relaye."),
	RELAY_2(2, "Third relay."),
	RELAY_3(3, "Fouth relay."),
	BAR_LOWER(4, "Lower armrest bar.."),
	BAR_RAISE(5, "Raise armest bar."),
	RELAY_6(6, "Seventh relay."),
	RELAY_7(7, "Eight relay."),

	// Driver board B relays:
	RELAY_8(8, "Ninth relay."),
	RELAY_9(9, "Tenth relay."),
	RELAY_10(10, "Eleventh relay."),
	RELAY_11(11, "Twelf relay."),
	RELAY_12(12, "Thirteenth relay."),
	RELAY_13(13, "Fourteenth relay."),
	RELAY_14(14, "Fifteenth relay."),
	RELAY_15(15, "Sixteenth relay.");

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
