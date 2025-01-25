package com.wisneskey.los.state;

import com.wisneskey.los.service.profile.model.Profile;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * State object for the profile service.
 * 
 * Copyright (C) 2025 Paul Wisneskey
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
public interface ProfileState extends State {

	/**
	 * Return a read only property containing the active profile.
	 * 
	 * @return Read only property with active profile.
	 */
	ReadOnlyObjectProperty<Profile> activeProfile();
}
