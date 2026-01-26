package com.wisneskey.los.service.lighting.driver.wled.client.model;

import java.util.ArrayList;

/**
 * Model object representing the list of color palettes available from the WLED
 * application. This list is considered to be read only from the controller.
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
 *
 */
public class Palettes extends ArrayList<String> {

	/**
	 * Version id for serialization.
	 */
	private static final long serialVersionUID = 1L;

}
