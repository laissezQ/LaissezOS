package com.wisneskey.los.service.remote.driver;

import com.wisneskey.los.service.remote.RemoteEventHandler;

/**
 * Dummy remote driver for simulating remote events when running off chair.
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
public class DummyRemoteDriver implements RemoteDriver {

	@Override
	public void initialize(RemoteEventHandler eventHandler) {
		// Nothing to do currently.

	}

	@Override
	public void terminate() {
		// Nothing to do currently.

	}
}
