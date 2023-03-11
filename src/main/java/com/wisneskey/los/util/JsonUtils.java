package com.wisneskey.los.util;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Methods for reading and writing JSON to and from Java objects.
 * 
 * @author paul.wisneskey@gmail.com
 */
public class JsonUtils {

	/**
	 * Object mapper used for serialization and deserialization. Configured to
	 * allow inline comments in the JSON.
	 */
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	static {
		JSON_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	}

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

	/**
	 * Serializes an object into JSON.
	 * 
	 * @param object
	 *          Object to serialize into JSON.
	 * @return String container serialized JSON.
	 */
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

	/**
	 * Deserializes JSON into an object of the specified type.
	 * 
	 * @param <T>
	 *          Type of object.
	 * @param source
	 *          String containing source JSON to deserialize.
	 * @param objectClass
	 *          Class of the object to create from JSON.
	 * @return Object of the specified class created from deserialized JSON.
	 */
	public static <T> T toObject(String source, Class<T> objectClass) {
		try {
			return JSON_MAPPER.readValue(source, objectClass);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse JSON string to object.", e);
		}
	}

	/**
	 * Deserializes JSON into an object of the specified type.
	 * 
	 * @param <T>
	 *          Type of object.
	 * @param input
	 *          Input stream to read JSON from.
	 * @param objectClass
	 *          Class of the object to create from JSON.
	 * @return Object of the specified class created from deserialized JSON.
	 */

	public static <T> T toObject(InputStream input, Class<T> objectClass) {
		try {
			return JSON_MAPPER.readValue(input, objectClass);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse JSON string to object.", e);
		}
	}

}
