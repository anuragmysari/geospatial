package com.springboot.controller;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.service.LocationServiceImpl;

@RestController
@RequestMapping("/")
public class LocationController {

	private static final String UNKNOWN_LOCATION = "Unable to locate the given coordinates.";
	@Autowired
	private LocationServiceImpl service;

	@PostMapping
	public Object post(@QueryParam("longitude") double longitude, @QueryParam("latitude") double latitude) {

		if (!service.getState(longitude, latitude).isEmpty()) {
			return service.getState(longitude, latitude);
		}
		return UNKNOWN_LOCATION;

	}

}
