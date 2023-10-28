package com.wisneskey.los.service.lighting.driver.wled.client;

import java.util.List;

import org.apache.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.service.lighting.driver.wled.client.model.Effects;
import com.wisneskey.los.service.lighting.driver.wled.client.model.Palettes;
import com.wisneskey.los.service.lighting.driver.wled.client.model.Summary;
import com.wisneskey.los.service.lighting.driver.wled.client.model.UpdateStateResult;
import com.wisneskey.los.service.lighting.driver.wled.client.model.info.Info;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;
import com.wisneskey.los.service.lighting.driver.wled.client.request.GetEffectsRequest;
import com.wisneskey.los.service.lighting.driver.wled.client.request.GetInfoRequest;
import com.wisneskey.los.service.lighting.driver.wled.client.request.GetPalettesRequest;
import com.wisneskey.los.service.lighting.driver.wled.client.request.GetStateRequest;
import com.wisneskey.los.service.lighting.driver.wled.client.request.GetSummaryRequest;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request.RequestParameter;
import com.wisneskey.los.service.lighting.driver.wled.client.request.Request.RequestType;
import com.wisneskey.los.service.lighting.driver.wled.client.request.UpdateStateRequest;

/**
 * Rest client for accessing a WLED controller application.
 * 
 * @author paul.wisneskey@bigbear.ai
 */
public class WledClient {

	/**
	 * Request to use to get the available effects from the controller.
	 */
	private static final GetEffectsRequest GET_EFFECTS_REQUEST = new GetEffectsRequest();

	/**
	 * Request to use to get the info from the controller.
	 */
	private static final GetInfoRequest GET_INFO_REQUEST = new GetInfoRequest();

	/**
	 * Request to use to get the palettes from the controller.
	 */
	private static final GetPalettesRequest GET_PALETTES_REQUEST = new GetPalettesRequest();

	/**
	 * Request to use to get the state from the controller.
	 */
	private static final GetStateRequest GET_STATE_REQUEST = new GetStateRequest();

	/**
	 * Request to use to get the summary of all information from the controller.
	 */
	private static final GetSummaryRequest GET_SUMMARY_REQUEST = new GetSummaryRequest();

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
	 * @param endpoint URL of the WLED instance.
	 */
	private WledClient(String endpoint) {
		this.endpoint = endpoint;
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Return the effects available from the controller.
	 * 
	 * @return Effects object with list of all effects supported by the
	 *         controller.
	 */
	public Effects getEffects() {
		return request(GET_EFFECTS_REQUEST);
	}

	/**
	 * Return the info for the controller.
	 * 
	 * @return Info object with information returned by the controller.
	 */
	public Info getInfo() {
		return request(GET_INFO_REQUEST);
	}

	/**
	 * Return the preset palettes available from the controller.
	 * 
	 * @return Palettes object with list of available palettes supported by the
	 *         controller.
	 */
	public Palettes getPalettes() {
		return request(GET_PALETTES_REQUEST);
	}

	/**
	 * Return the state of the controller.
	 * 
	 * @return State object representing the current state of the controller.
	 */
	public State getState() {
		return request(GET_STATE_REQUEST);
	}

	/**
	 * Return the summary of the controller.
	 * 
	 * @return Summary object representing all information from the controller.
	 */
	public Summary getSummary() {
		return request(GET_SUMMARY_REQUEST);
	}

	/**
	 * Send updates for the state to the controller.
	 * 
	 * @param  stateUpdates State object containing values only for the properties
	 *                        to update.
	 * 
	 * @return              Result from updating the state of the controller.
	 */
	public UpdateStateResult updateState(State stateUpdates) {
		return request(new UpdateStateRequest(stateUpdates));
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Make a request to the WLED instance.
	 * 
	 * @param  request          Request object for the function to be called.
	 * @return                  The result object parsed from the response to the
	 *                          request.
	 * @throws LaissezException If the request fails.
	 */
	private <T> T request(Request<T> request) throws LaissezException {

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
	 * @param  endpoint URL for instance.
	 * 
	 * @return          Client configured for the designated endpoint.
	 */
	public static WledClient create(String endpoint) {

		return new WledClient(endpoint);
	}
}