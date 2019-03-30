package com.springboot.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class State {

	@JsonProperty("state")
	String name;

	@JsonProperty("border")
	List<Double[]> borders;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Double[]> getBorders() {
		borders = (borders == null) ? new ArrayList<>() : borders;
		return borders;
	}

	public void setBorders(List<Double[]> borders) {
		this.borders = borders;
	}

}
