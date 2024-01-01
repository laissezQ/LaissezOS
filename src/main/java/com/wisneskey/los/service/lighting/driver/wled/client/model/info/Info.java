package com.wisneskey.los.service.lighting.driver.wled.client.model.info;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the info returned for a WLED controller. This
 * information is all considered to be read only from the controller.
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
 *
 */
public class Info {

	@JsonProperty("arch")
	private String architecture;

	@JsonProperty("brand")
	private String brand;

	@JsonProperty("vid")
	private Integer buildId;

	@JsonProperty("core")
	private String coreVersion;

	@JsonProperty("cpalcount")
	private Integer customPaletteCount;

	@JsonProperty("fxcount")
	private Integer effectCount;

	@JsonProperty("freeheap")
	private Long freeHeap;

	@JsonProperty("fs")
	private FileSystemInfo fileSystemInfo;

	@JsonProperty("ip")
	private String ipAddress;

	@JsonProperty("leds")
	private LedInfo ledInfo;

	@JsonProperty("lip")
	private String lip;

	@JsonProperty("live")
	private Boolean live;

	@JsonProperty("liveseg")
	private Integer liveSeg;

	@JsonProperty("lm")
	private String lm;

	@JsonProperty("lwip")
	private Integer lwip;

	@JsonProperty("mac")
	private String macAddress;

	@JsonProperty("maps")
	private List<Map<String, Object>> maps;

	@JsonProperty("name")
	private String name;

	@JsonProperty("ndc")
	private Integer ndc;

	@JsonProperty("opt")
	private Integer options;

	@JsonProperty("palcount")
	private Integer paletteCount;

	@JsonProperty("product")
	private String product;

	@JsonProperty("str")
	private Boolean str;

	@JsonProperty("time")
	private String timeStamp;

	@JsonProperty("udpport")
	private Integer udpPort;

	@JsonProperty("uptime")
	private Long upTime;

	@JsonProperty("ver")
	private String version;

	@JsonProperty("wifi")
	private WifiInfo wifiInfo;

	@JsonProperty("ws")
	private Integer ws;

	// ----------------------------------------------------------------------------------------
	// Property getters.
	// ----------------------------------------------------------------------------------------

	public String getArchitecture() {
		return architecture;
	}

	public String getBrand() {
		return brand;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public String getCoreVersion() {
		return coreVersion;
	}

	public Integer getCustomPaletteCount() {
		return customPaletteCount;
	}

	public Integer getEffectCount() {
		return effectCount;
	}

	public Long getFreeHeap() {
		return freeHeap;
	}

	public FileSystemInfo getFileSystemInfo() {
		return fileSystemInfo;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public LedInfo getLedInfo() {
		return ledInfo;
	}

	public String getLip() {
		return lip;
	}

	public Boolean getLive() {
		return live;
	}

	public Integer getLiveSeg() {
		return liveSeg;
	}

	public String getLm() {
		return lm;
	}

	public Integer getLwip() {
		return lwip;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public List<Map<String, Object>> getMaps() {
		return maps;
	}

	public String getName() {
		return name;
	}

	public Integer getNdc() {
		return ndc;
	}

	public Integer getOptions() {
		return options;
	}

	public Integer getPaletteCount() {
		return paletteCount;
	}

	public String getProduct() {
		return product;
	}

	public Boolean getStr() {
		return str;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public Integer getUdpPort() {
		return udpPort;
	}

	public Long getUpTime() {
		return upTime;
	}

	public String getVersion() {
		return version;
	}

	public WifiInfo getWifiInfo() {
		return wifiInfo;
	}

	public Integer getWs() {
		return ws;
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

}