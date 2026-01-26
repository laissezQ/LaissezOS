package com.wisneskey.los.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.script.command.ScriptCommand;

/**
 * Methods for reading and writing JSON to and from Java objects.
 * 
 * Copyright (C) 2026 Paul Wisneskey
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
 */
public class JsonUtils {

	/**
	 * Object mapper used for serialization and deserialization. Configured to
	 * allow inline comments in the JSON.
	 */
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	static {
		// Allow Java style comments in the JSON.
		JSON_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

		// Register a custom deserializer for the script command objects.
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ScriptCommand.class, JsonUtils.createTypedDeserializer(ScriptCommand.class));
		JSON_MAPPER.registerModule(module);
	}

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to disallow instantiation.
	 */
	private JsonUtils() {
	}

	// ----------------------------------------------------------------------------------------
	// Static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Serializes an object into JSON.
	 * 
	 * @param  object Object to serialize into JSON.
	 * @return        String container serialized JSON.
	 */
	public static String toJSONString(Object object) {

		if (object == null) {
			return null;
		}

		try {
			return JSON_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new LaissezException("Failed to write object as JSON string.", e);
		}
	}

	/**
	 * Deserializes JSON into an object of the specified type.
	 * 
	 * @param  <T>         Type of object.
	 * @param  source      String containing source JSON to deserialize.
	 * @param  objectClass Class of the object to create from JSON.
	 * @return             Object of the specified class created from deserialized
	 *                     JSON.
	 */
	public static <T> T toObject(String source, Class<T> objectClass) {
		try {
			return JSON_MAPPER.readValue(source, objectClass);
		} catch (Exception e) {
			throw new LaissezException("Failed to parse JSON string to object.", e);
		}
	}

	/**
	 * Deserializes JSON into an object of the specified type.
	 * 
	 * @param  <T>         Type of object.
	 * @param  input       Input stream to read JSON from.
	 * @param  objectClass Class of the object to create from JSON.
	 * @return             Object of the specified class created from deserialized
	 *                     JSON.
	 */

	public static <T> T toObject(InputStream input, Class<T> objectClass) {
		try {
			return JSON_MAPPER.readValue(input, objectClass);
		} catch (Exception e) {
			throw new LaissezException("Failed to parse JSON string to object.", e);
		}
	}

	/**
	 * Creates a deserializer that use the @JsonSubType annotation to map from
	 * incoming source JSON to an subclass of a supplied target class.
	 * 
	 * @param  <T>         Base class of the result objects that the deserializer
	 *                       will produce.
	 * @param  targetClass Base class of the result objects (used to get sub-type
	 *                       annotations).
	 * @return             Deserializer that is auto-configured from annotations
	 *                     on specified base class.
	 */
	public static <T> JsonDeserializer<T> createTypedDeserializer(Class<T> targetClass) {

		return new TypedDeserializer<>(targetClass);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * JSON deserializer that uses the JsonSubTypes annotation to determine which
	 * subclass an instance should be in the JSON. The annotation should be on the
	 * root class and is used to map from a name to one of the defined subclasses.
	 *
	 * @param <R> Root class that is being deserialized.
	 */
	private static class TypedDeserializer<T> extends StdDeserializer<T> {

		/**
		 * Version id for serialization.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Map of the query name to the Java type that it should be deserialized to.
		 */
		private Map<String, Class<?>> nameToTypeMap;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		public TypedDeserializer(Class<T> targetClass) {
			super(targetClass);

			// Use the JsonSubType annotations on the main query interface to
			// determine all of the various
			// types that it could map to.
			JsonSubTypes.Type[] queryTypes = targetClass.getAnnotation(JsonSubTypes.class).value();

			// Now store these in a mapping from the type name to the Java class for
			// its query.
			nameToTypeMap = new LinkedHashMap<>();
			for (JsonSubTypes.Type queryType : queryTypes) {
				nameToTypeMap.put(queryType.name(), queryType.value());
			}
		}

		// ----------------------------------------------------------------------------------------
		// JsonDeserializer methods.
		// ----------------------------------------------------------------------------------------

		@Override
		@SuppressWarnings("unchecked")
		public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {

			ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();

			ObjectNode object = objectMapper.readTree(parser);

			for (Map.Entry<String, Class<?>> mapEntry : nameToTypeMap.entrySet()) {
				String propertyName = mapEntry.getKey();
				Class<?> targetClass = mapEntry.getValue();

				if (object.has(propertyName)) {
					JsonNode targetConfigNode = object.get(propertyName);
					return (T) objectMapper.treeToValue(targetConfigNode, targetClass);
				}
			}

			String name = object.fieldNames().next();
			throw new IllegalArgumentException("Unrecognized type '" + name + "'.");
		}
	}

}
