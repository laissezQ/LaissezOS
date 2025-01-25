package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the info returned for a WLED controller. This
 * information is all considered to be read only from the controller.
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
 *
 */
@JsonIgnoreProperties(value = {"lwip", "maps"})
public class Info {

	/**
	 * Version name for WLED.
	 */
	@JsonProperty("ver")
	private String version;

	/**
	 * Build identifier.
	 */
	@JsonProperty("vid")
	private Integer buildId;

	/**
	 * Information about the LED strip.
	 */
	@JsonProperty("leds")
	private LedInfo ledInfo;

	/**
	 * Flag indicating if UI toggling sync toggle receive+send or send only.
	 */
	@JsonProperty("str")
	private boolean sendThenReceive;

	/**
	 * Display name for light.
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * UDP port to use for realtime packets and WLED broadcast.
	 */
	@JsonProperty("udpport")
	private Integer udpPort;

	/**
	 * Flag indicating if WLED is receiving realtime data.
	 */
	@JsonProperty("live")
	private boolean live;

	/**
	 * Flag indicating the number of live segments.
	 */
	@JsonProperty("liveseg")
	private Integer liveSegments;
	
	/**
	 * Info about the realtime data source.
	 */
	@JsonProperty("lm")
	private String liveMetadata;

	/**
	 * Realtime data source IP address.
	 */
	@JsonProperty("lip")
	private String liveIpAddress;

	/**
	 * Number of connected WebSocket clients.
	 */
	@JsonProperty("ws")
	private Integer wsClientCount;

	/**
	 * Number of effects available.
	 */
	@JsonProperty("fxcount")
	private Integer effectCount;

	/**
	 * Number of palettes configured.
	 */
	@JsonProperty("palcount")
	private Integer paletteCount;

	/**
	 * Number of custom palettes configured.
	 */
	@JsonProperty("cpalcount")
	private Integer customPaletteCount;
	
	/**
	 * Information about the WIFI state.
	 */
	@JsonProperty("wifi")
	private WifiInfo wifi;

	/**
	 * File system information.
	 */
	@JsonProperty("fs")
	private FileSystemInfo fileSystemInfo;

	/**
	 * Number of other devices discovered on network.
	 */
	@JsonProperty("ndc")
	private Integer numberOtherDevices;

	/**
	 * Name of the platform.
	 */
	@JsonProperty("arch")
	private String architecture;

	/**
	 * Version of the underlying SDK.
	 */
	@JsonProperty("core")
	private String coreVersion;

	/**
	 * Bytes of heap memory currently available.
	 */
	@JsonProperty("freeheap")
	private Long freeHeap;

	/**
	 * Time since last boot or reset in seconds.
	 */
	@JsonProperty("uptime")
	private Integer upTime;

	/**
	 * Time and date the ESP32 thinks it is.
	 */
	@JsonProperty("time")
	private String time;
	
	/**
	 * Used for debugging purposes only.
	 */
	@JsonProperty("opt")
	private Integer options;

	/**
	 * The product/vendor of the light.
	 */
	@JsonProperty("brand")
	private String brand;

	/**
	 * The product name.
	 */
	@JsonProperty("product")
	private String product;

	/**
	 * The hexadecimal MAC address of the light.
	 */
	@JsonProperty("mac")
	private String macAddress;

	/**
	 * THe IP address of the instance.
	 */
	@JsonProperty("ip")
	private String ipAddress;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public String getVersion() {
		return version;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public LedInfo getLedInfo() {
		return ledInfo;
	}

	public boolean isSendThenReceive() {
		return sendThenReceive;
	}

	public String getName() {
		return name;
	}

	public Integer getUdpPort() {
		return udpPort;
	}

	public boolean getLive() {
		return live;
	}

	public Integer getLiveSegments() {
		return liveSegments;
	}
	
	public String getLiveMetadata() {
		return liveMetadata;
	}

	public String getLiveIpAddress() {
		return liveIpAddress;
	}

	public Integer getWsClientCount() {
		return wsClientCount;
	}

	public Integer getEffectCount() {
		return effectCount;
	}

	public Integer getPaletteCount() {
		return paletteCount;
	}

	public Integer getCustomPaletteCount() {
		return customPaletteCount;
	}

	public WifiInfo getWifi() {
		return wifi;
	}

	public FileSystemInfo getFileSystemInfo() {
		return fileSystemInfo;
	}

	public Integer getNumberOtherDevices() {
		return numberOtherDevices;
	}

	public String getArchitecture() {
		return architecture;
	}

	public String getCoreVersion() {
		return coreVersion;
	}

	public Long getFreeHeap() {
		return freeHeap;
	}

	public Integer getUpTime() {
		return upTime;
	}

	public String getTime() {
		return time;
	}
	
	public Integer getOptions() {
		return options;
	}

	public String getBrand() {
		return brand;
	}

	public String getProduct() {
		return product;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}


}