package com.wisneskey.los.service.lighting.driver.wled.client.model.state;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model object representing the state of a single lighting segment. This object
 * is read/write and can be used to update the state of the controller. It is
 * designed so that only state properties to change need to be set in a state
 * object that is being sent back to the controller.
 * 
 * @author paul.wisneskey@gmail.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SegmentState {

	@JsonProperty("bri")
	private Integer brightness;

	@JsonProperty("cct")
	private Integer cct;

	@JsonProperty("col")
	private List<List<Integer>> colors;

	@JsonProperty("c1")
	private Integer c1;

	@JsonProperty("c2")
	private Integer c2;

	@JsonProperty("c3")
	private Integer c3;

	@JsonProperty("frz")
	private Boolean frz;

	@JsonProperty("fx")
	private Integer fx;

	@JsonProperty("grp")
	private Integer group;

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("ix")
	private Integer ix;

	@JsonProperty("len")
	private Integer length;

	@JsonProperty("mi")
	private Boolean mi;

	@JsonProperty("m12")
	private Integer m12;

	@JsonProperty("of")
	private Integer offset;

	@JsonProperty("on")
	private Boolean on;

	@JsonProperty("o1")
	private Boolean o1;

	@JsonProperty("o2")
	private Boolean o2;

	@JsonProperty("o3")
	private Boolean o3;

	@JsonProperty("pal")
	private Integer palette;

	@JsonProperty("rev")
	private Boolean rev;

	@JsonProperty("sel")
	private Boolean sel;

	@JsonProperty("si")
	private Integer si;

	@JsonProperty("spc")
	private Integer spc;

	@JsonProperty("set")
	private Integer set;

	@JsonProperty("start")
	private Integer startIndex;

	@JsonProperty("stop")
	private Integer stopIndex;

	@JsonProperty("sx")
	private Integer sx;

	// ----------------------------------------------------------------------------------------
	// Property getters/setters.
	// ----------------------------------------------------------------------------------------

	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}

	public Integer getCct() {
		return cct;
	}

	public void setCct(Integer cct) {
		this.cct = cct;
	}

	public List<List<Integer>> getColors() {
		return colors;
	}

	public void setColors(List<List<Integer>> colors) {
		this.colors = colors;
	}

	public Integer getC1() {
		return c1;
	}

	public void setC1(Integer c1) {
		this.c1 = c1;
	}

	public Integer getC2() {
		return c2;
	}

	public void setC2(Integer c2) {
		this.c2 = c2;
	}

	public Integer getC3() {
		return c3;
	}

	public void setC3(Integer c3) {
		this.c3 = c3;
	}

	public Boolean getFrz() {
		return frz;
	}

	public void setFrz(Boolean frz) {
		this.frz = frz;
	}

	public Integer getFx() {
		return fx;
	}

	public void setFx(Integer fx) {
		this.fx = fx;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIx() {
		return ix;
	}

	public void setIx(Integer ix) {
		this.ix = ix;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Boolean getMi() {
		return mi;
	}

	public void setMi(Boolean mi) {
		this.mi = mi;
	}

	public Integer getM12() {
		return m12;
	}

	public void setM12(Integer m12) {
		this.m12 = m12;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Boolean getOn() {
		return on;
	}

	public void setOn(Boolean on) {
		this.on = on;
	}

	public Boolean getO1() {
		return o1;
	}

	public void setO1(Boolean o1) {
		this.o1 = o1;
	}

	public Boolean getO2() {
		return o2;
	}

	public void setO2(Boolean o2) {
		this.o2 = o2;
	}

	public Boolean getO3() {
		return o3;
	}

	public void setO3(Boolean o3) {
		this.o3 = o3;
	}

	public Integer getPalette() {
		return palette;
	}

	public void setPalette(Integer palette) {
		this.palette = palette;
	}

	public Boolean getRev() {
		return rev;
	}

	public void setRev(Boolean rev) {
		this.rev = rev;
	}

	public Boolean getSel() {
		return sel;
	}

	public void setSel(Boolean sel) {
		this.sel = sel;
	}

	public Integer getSi() {
		return si;
	}

	public void setSi(Integer si) {
		this.si = si;
	}

	public Integer getSpc() {
		return spc;
	}

	public void setSpc(Integer spc) {
		this.spc = spc;
	}

	public Integer getSet() {
		return set;
	}

	public void setSet(Integer set) {
		this.set = set;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getStopIndex() {
		return stopIndex;
	}

	public void setStopIndex(Integer stopIndex) {
		this.stopIndex = stopIndex;
	}

	public Integer getSx() {
		return sx;
	}

	public void setSx(Integer sx) {
		this.sx = sx;
	}
}