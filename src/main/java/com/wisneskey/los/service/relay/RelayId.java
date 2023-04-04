package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	RELAY_1(0, "First relay."),
	RELAY_2(1, "Second relay."),
	RELAY_3(2, "Third relay."),
	RELAY_4(3, "Fourth relay."),
	RELAY_5(4, "Fifth relay."),
	RELAY_6(5, "Sixth relay."),
	RELAY_7(6, "Seventh relay."),
	RELAY_8(7, "Eight relay.");

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
