package com.wisneskey.los.service.lighting.driver.wled.client.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the info returned for a WLED controller.  This information
 * is all considered to be read only from the controller.
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

	/**
	 * Model object for the file system information. Considered to be a read only
	 * object from the controller.
	 */
	public static class FileSystemInfo {

		@JsonProperty("pmt")
		private Integer pmt;

		@JsonProperty("t")
		private Integer total;

		@JsonProperty("u")
		private Integer used;

		// ----------------------------------------------------------------------------------------
		// Property getters.
		// ----------------------------------------------------------------------------------------

		public Integer getPmt() {
			return pmt;
		}

		public Integer getTotal() {
			return total;
		}

		public Integer getUsed() {
			return used;
		}
	}

	/**
	 * Model object for the information about the configuration and state of the
	 * LEDs connected to the controller. Considered to be a read only object from
	 * the controller.
	 */
	public static class LedInfo {

		@JsonProperty("cct")
		private Integer cct;

		@JsonProperty("count")
		private Integer count;

		@JsonProperty("fps")
		private Integer framesPerSecond;

		@JsonProperty("rgbw")
		private Boolean isRGBW;

		@JsonProperty("maxpwr")
		private Integer maxPower;

		@JsonProperty("maxseg")
		private Integer maxSegments;

		@JsonProperty("lc")
		private Integer lc;

		@JsonProperty("pwr")
		private Integer power;

		@JsonProperty("seglc")
		private Integer[] segmentLC;

		@JsonProperty("wv")
		private Integer wv;

		// ----------------------------------------------------------------------------------------
		// Property getters.
		// ----------------------------------------------------------------------------------------

		public Integer getCct() {
			return cct;
		}

		public Integer getCount() {
			return count;
		}

		public Integer getFramesPerSecond() {
			return framesPerSecond;
		}

		public Boolean getIsRGBW() {
			return isRGBW;
		}

		public Integer getMaxPower() {
			return maxPower;
		}

		public Integer getMaxSegments() {
			return maxSegments;
		}

		public Integer getLc() {
			return lc;
		}

		public Integer getPower() {
			return power;
		}

		public Integer[] getSegmentLC() {
			return segmentLC;
		}

		public Integer getWv() {
			return wv;
		}
	}

	/**
	 * Model object for the WIFI connection information.
	 */
	public static class WifiInfo {

		@JsonProperty("channel")
		private Integer channel;

		@JsonProperty("bssid")
		private String bssid;

		@JsonProperty("rssi")
		private Integer rssi;

		@JsonProperty("signal")
		private Integer signal;

		// ----------------------------------------------------------------------------------------
		// Property getters.
		// ----------------------------------------------------------------------------------------

		public Integer getChannel() {
			return channel;
		}

		public String getBssid() {
			return bssid;
		}

		public Integer getRssi() {
			return rssi;
		}

		public Integer getSignal() {
			return signal;
		}
	}

}