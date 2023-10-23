package com.wisneskey.los.service.lighting.driver.wled.client;

import java.util.List;

import org.apache.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request.RequestParameter;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request.RequestType;

/**
 * Rest client for accessing a WLED controller application.
 * 
 * @author paul.wisneskey@bigbear.ai
 */
public class WLEDClient {

	/**
	 * URL of the WLED instance the client is for.
	 */
	private String endpoint;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to force use of static constructor method.
	 * 
	 * @param endpoint
	 *          URL of the WLED instance.
	 */
	private WLEDClient(String endpoint) {
		this.endpoint = endpoint;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Make a request to the WLED instance.
	 * 
	 * @param request
	 *          Request object for the function to be called.
	 * @return The result object parsed from the response to the request.
	 * @throws LaissezException
	 *           If the request fails.
	 */
	public <T> T request(Request<T> request) throws LaissezException {

		// Set up the final request path.
		String requestPath = endpoint + request.getRequestPath();

		// Construct request based on the request type (GET or POST).
		HttpRequest httpRequest = null;
		if (request.getRequestType() == RequestType.GET) {
			httpRequest = Unirest.get(requestPath);
		} else {
			httpRequest = Unirest.post(requestPath);
		}

		if (request.getContentType() != null) {
			httpRequest.header("Content-Type", request.getContentType()) //
					.header("Accept", "application/json");
		}

		// Add any request parameters.
		List<RequestParameter> requestParameters = request.getRequestParameters();

		// If we have request parameters, add them to the request.
		if (requestParameters != null && !requestParameters.isEmpty()) {
			for (RequestParameter parameter : requestParameters) {
				httpRequest = httpRequest.queryString(parameter.getName(), parameter.getValue());
			}
		}

		// For post requests, we need to try to set body and add any file uploads.
		if (request.getRequestType() == RequestType.POST) {

			// No file uploads to set body.
			String postBody = request.getPostBody();
			if (postBody != null) {
				((HttpRequestWithBody) httpRequest).body(postBody);
			}
		}

		// Now call the server and get the JSON response.
		HttpResponse<String> response = null;
		try {
			response = httpRequest.asString();
		} catch (UnirestException e) {
			throw new LaissezException("Request failed.", e);
		}

		if (response.getStatus() != HttpStatus.SC_OK) {
			// Try to parse the response body into the server error details object.
			throw new LaissezException("Failure returned by call: " + response.getBody());
		}

		// Finally, let the request turn the JSON response back into the response
		// object.
		return request.processResponse(response.getBody());

	}

	// ----------------------------------------------------------------------------------------
	// Static methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Create a client for a given instance.
	 *
	 * @param endpoint
	 *          URL for instance.
	 * 
	 * @return Client configured for the designated endpoint.
	 */
	public static WLEDClient create(String endpoint) {

		return new WLEDClient(endpoint);
	}
}
