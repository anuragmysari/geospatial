package com.springboot.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.model.State;

@Configuration
public class LocationMapper {

	private static final Logger log = LoggerFactory.getLogger(LocationMapper.class);

	private static File statesFile = new File("src/main/resources/static/states.json");

	protected static final Map<String, List<Double[]>> map = new HashMap<>();

	public static Map<String, List<Double[]>> getMap() {
		return map;
	}

	public LocationMapper() throws IOException {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(statesFile));

			JsonFactory jf = new JsonFactory();
			JsonParser jp = jf.createParser(reader);
			jp.setCodec(new ObjectMapper());
			jp.nextToken();
			while (jp.hasCurrentToken()) {
				State s = jp.readValueAs(State.class);
				map.put(s.getName(), s.getBorders());
				jp.nextToken();
			}
			reader.close();
			jp.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

}