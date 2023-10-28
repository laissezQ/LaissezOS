package com.wisneskey.los.service.relay;

/**
 * Enumerated type designating the various relays that can be turned on and off.
 * 
 * @author paul.wisneskey@gmail.com
 */
public enum RelayId {

	// Driver board A relays:
	A_RETRACT(0, "A retract."),
	A_EXTEND(1, "A extend."),
	B_RETRACT(2, "B retract."),
	B_EXTEND(3, "B extend."),
	BAR_LOWER(4, "Lower armrest bar.."),
	BAR_RAISE(5, "Raise armest bar."),
	RELAY_6(6, "Seventh relay."),
	RELAY_7(7, "Eight relay."),

	// Driver board B relays:
	AMPLIFIER(8, "Audio amplifier on."),
	RELAY_9(9, "Tenth relay."),
	RELAY_10(10, "Eleventh relay."),
	RELAY_11(11, "Twelf relay."),
	RELAY_12(12, "Thirteenth relay."),
	RELAY_13(13, "Fourteenth relay."),
	LIGHTING_A(14, "LED Lighting for A side."),
	LIGHTING_B(15, "LED Lighting for B side.");

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
