package com.wisneskey.los.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Methods for reading and writing JSON to and from Java objects.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class JsonUtils {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	// ---------------------------------------------------------------------
	// Constructors.
	// ---------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation.
	 */
	private JsonUtils() {
	}

	// ---------------------------------------------------------------------
	// Static methods.
	// ---------------------------------------------------------------------

	public static String toJSONString(Object object) {

		if (object == null) {
			return null;
		}

		try {
			return JSON_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to write object as JSON string.", e);
		}
	}

	public static <T> T toObject(String source, Class<T> objectClass) {
		try {
			return JSON_MAPPER.readValue(source, objectClass);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse JSON string to object.", e);
		}
	}
}
