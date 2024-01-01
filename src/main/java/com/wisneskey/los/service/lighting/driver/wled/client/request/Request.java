package com.wisneskey.los.service.lighting.driver.wled.client.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisneskey.los.error.LaissezException;

/**
 * Abstract base class for requests to the WLED controller application.
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
 * @author     paul.wisneskey@gmail.com
 * 
 * @param  <T> Type of response from request.
 */
public abstract class Request<T> {

	/**
	 * Content type for JSON requests and responses.
	 */
	private static final String CONTENT_TYPE = "application/json";

	/**
	 * Type of the result returned from this call.
	 */
	private Class<? extends T> responseType;

	/**
	 * Type of the request (GET or POST).
	 */
	private RequestType requestType;

	/**
	 * Path component of the request URL.
	 */
	private String requestPath;

	/**
	 * Optional list of request parameters.
	 */
	private List<RequestParameter> requestParameters = null;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	protected Request(RequestType requestType, String requestPath, Class<? extends T> itemType) {
		this.requestType = requestType;
		this.requestPath = requestPath;
		this.responseType = itemType;
	}

	// ----------------------------------------------------------------------------------------
	// Protected methods.
	// ----------------------------------------------------------------------------------------

	protected void setRequestParameters() {
		// By default do not register any request parameters.
	}

	protected void setRequestParameter(String parameterName, Object value) {
		if (value == null) {
			return;
		}

		if (requestParameters == null) {
			requestParameters = new ArrayList<>();
		}

		if (value instanceof Enum<?>) {
			value = value.toString();
		}

		if (value instanceof Collection<?>) {

			Collection<?> valueCollection = Collection.class.cast(value);
			for (Object singleValue : valueCollection) {
				requestParameters.add(new RequestParameter(parameterName, singleValue));
			}
		} else {
			requestParameters.add(new RequestParameter(parameterName, value));
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	public RequestType getRequestType() {
		return requestType;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public String getContentType() {
		return CONTENT_TYPE;
	}

	public final List<RequestParameter> getRequestParameters() {

		// Let the request implementations set their parameters.
		setRequestParameters();
		return requestParameters;
	}

	public String getPostBody() {
		if (requestType == RequestType.GET) {
			throw new LaissezException("Can not generate a POST body for a GET request.");
		}

		return null;
	}

	public T processResponse(String responseBody) {

		if (Void.class.equals(responseType)) {
			return null;
		}

		if (responseBody.isEmpty()) {
			return null;
		}

		if (String.class.equals(responseType)) {
			return responseType.cast(responseBody);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(responseBody, responseType);
		} catch (Exception e) {
			throw new LaissezException("Failed to parse response.", e);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[]";
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Type of request (GET or POST).
	 */
	public enum RequestType {
		GET,
		POST
	}

	/**
	 * Class representing the information for a request parameter in a request.
	 */
	public static class RequestParameter {

		private String name;

		private Object value;

		public RequestParameter(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}
	}
}
