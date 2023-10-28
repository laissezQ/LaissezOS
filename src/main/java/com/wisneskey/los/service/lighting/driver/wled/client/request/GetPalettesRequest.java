package com.wisneskey.los.service.lighting.driver.wled.client.request;

import com.wisneskey.los.service.lighting.driver.wled.client.model.Palettes;

/**
 * Request for getting the list of preset palettes from a WLED controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class GetPalettesRequest extends Request<Palettes> {

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	public GetPalettesRequest() {
		super(RequestType.GET, "/json/pal", Palettes.class);
	}
}
