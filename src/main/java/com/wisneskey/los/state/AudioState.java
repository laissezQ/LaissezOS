package com.wisneskey.los.state;

import javafx.beans.property.IntegerProperty;

/**
 * Interface denoting the object providing read only access to the state of the
 * Audio service.
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
public interface AudioState extends State {
	
	/**
	 * Volume of the chair when it is in its normal state.
	 * 
	 * @return Volume of chair from 0 - 11.
	 */
	IntegerProperty volume();
	
	/**
	 * Volume of the chair when it is in chap mode.
	 * 
	 * @return Volume of chair from 0 - 11.
	 */
	IntegerProperty chapModeVolume();
}
