package com.springboot.service;

import java.util.List;

public interface LocationService {

	List<String> getState(double longitude, double latitude);

}
