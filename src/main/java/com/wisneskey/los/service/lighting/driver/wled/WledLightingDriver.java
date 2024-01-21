package com.wisneskey.los.service.lighting.driver.wled;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.Kernel;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.LightingEffectId;
import com.wisneskey.los.service.lighting.driver.LightingDriver;
import com.wisneskey.los.service.lighting.driver.wled.client.WledClient;
import com.wisneskey.los.service.lighting.driver.wled.client.model.Summary;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.Segment;
import com.wisneskey.los.service.lighting.driver.wled.client.model.state.State;
import com.wisneskey.los.service.lighting.driver.wled.config.WledEffectConfig;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.service.relay.RelayId;
import com.wisneskey.los.service.relay.RelayService;
import com.wisneskey.los.state.LightingState;
import com.wisneskey.los.util.JsonUtils;

import javafx.scene.paint.Color;

/**
 * Driver for controlling lights with the WLED application running on a host.
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
 * @author paul.wisneskey@gmail.com
 */
public class WledLightingDriver implements LightingDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(WledLightingDriver.class);

	/**
	 * Base path where WLED effect configurations are saved in the resources.
	 */
	private static final String LIGHTING_CONFIG_BASE = "/lighting/wled/";

	/**
	 * Flag indicating if we found the WLED controller and could communicate with
	 * it.
	 */
	private boolean online = false;

	/**
	 * Client to use for communicating with the WLED controller via its JSON API.
	 */
	private WledClient controllerClient;

	/**
	 * Maximum brightness to allow for controller (e.g. 100% user brightness = max
	 * controller).
	 */
	private int maxControllerBrightness;

	/**
	 * Map of light effect ids to their corresponding WLED configurations.
	 */
	private EnumMap<LightingEffectId, WledEffectConfig> effectConfigMap = new EnumMap<>(LightingEffectId.class);

	// ----------------------------------------------------------------------------------------
	// LightingDriver methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void initialize(Profile profile, LightingState state) {

		if (profile.getWledHostAddress() == null) {
			throw new LaissezException("No WLED controller host address set.");
		}

		LOGGER.info("Loading WLED effect configurations...");
		loadEffectConfigurations();

		LOGGER.info("Initializing WLED lighting driver: host={}", profile.getWledHostAddress());

		// Set the maximum brightness we will use for the controller.
		maxControllerBrightness = profile.getMaxControllerBrightness();

		controllerClient = WledClient.create(profile.getWledHostAddress());

		Summary summary;
		try {
			// Verify the connection by requesting the info from the controller.
			summary = controllerClient.getSummary();
			online = true;
			LOGGER.info("Connected to WLED lighting driver: name={}", summary.getInfo().getName());
		} catch (Exception e) {
			LOGGER.error("Failed to initialize lighting driver: {}", e.getMessage());
			online = false;
		}

		if (!online) {
			return;
		}

		// TODO: Validate the information we obtained from the controller to make
		// sure its configuration matches what we expect in terms of lighting
		// segments, etc.

		// Immediately set the brightness of the lights.
		changeBrightness(state.brightness().intValue());

		// If we were able to talk to the controller, go ahead and energize
		// the relays to enable power to the LED lighting on both sides of the
		// chair.
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.SIDE_LIGHTING);
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOn(RelayId.UNDER_LIGHTING);
	}

	@Override
	public void terminate() {

		// Turn off the power to the LED strips.
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(RelayId.SIDE_LIGHTING);
		((RelayService) Kernel.kernel().getService(ServiceId.RELAY)).turnOff(RelayId.UNDER_LIGHTING);
	}

	@Override
	public void playEffect(LightingEffectId effectId, LightingState lightingState) {

		LOGGER.info("Switching lighting effect: effectId={}", effectId, lightingState);

		// First get the base WLED effect configuration.
		WledEffectConfig config = effectConfigMap.get(effectId);
		if (config == null) {
			LOGGER.error("WLED effect configuration not found in map: " + effectId);
			return;
		}

		Color firstColor = config.getColor1() == null ? lightingState.firstColor().getValue() : Color.web(config.getColor1());
		Color secondColor = config.getColor2() == null ? lightingState.secondColor().getValue() : Color.web(config.getColor2());
		Color thirdColor = config.getColor3() == null ? lightingState.thirdColor().getValue() : Color.web(config.getColor3());
		
		List<List<Integer>> colors = new ArrayList<>(3);
		colors.add(colorToRGB(firstColor));
		colors.add(colorToRGB(secondColor));
		colors.add(colorToRGB(thirdColor));

		Segment segment = new Segment();
		segment.setId(0);
		segment.setColors(colors);
		segment.setEffectId(config.getEffectId());
		segment.setLoadEffectDefaults(true);
		segment.setEffectIntensity(config.getIntensity());
		segment.setEffectSlider1(config.getSlider1());
		segment.setEffectSlider2(config.getSlider2());
		segment.setEffectSlider3(config.getSlider3());
		segment.setEffectOption1(config.getOption1());
		segment.setEffectOption2(config.getOption2());
		segment.setEffectOption3(config.getOption3());
		segment.setEffectSpeed(config.getSpeed());
		
		State state = new State();
		state.setOn(config.getOn());
		state.setSegments(Collections.singletonList(segment));

		controllerClient.updateState(state);

		// Apply and color changes to our internal state.
		lightingState.firstColor().setValue(firstColor);
		lightingState.secondColor().setValue(secondColor);
		lightingState.thirdColor().setValue(thirdColor);
	}

	@Override
	public void changeBrightness(int brightness) {

		int controllerBrightness = calculateControllerBrightness(brightness);

		LOGGER.info("Changing brightness: bightness={} controllerBrightness={}", brightness, controllerBrightness);

		State state = new State();
		state.setBrightness(controllerBrightness);

		controllerClient.updateState(state);
	}

	@Override
	public void changeColor(LightingState lightingState) {

		LOGGER.info("Changing colors: first={} second={} third={}", lightingState.firstColor().getValue(),
				lightingState.secondColor().getValue(), lightingState.thirdColor().getValue());

		List<List<Integer>> colors = new ArrayList<>(3);
		colors.add(colorToRGB(lightingState.firstColor().getValue()));
		colors.add(colorToRGB(lightingState.secondColor().getValue()));
		colors.add(colorToRGB(lightingState.thirdColor().getValue()));

		Segment segment = new Segment();
		segment.setId(0);
		segment.setColors(colors);

		State state = new State();
		state.setSegments(Collections.singletonList(segment));

		controllerClient.updateState(state);
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	private void loadEffectConfigurations() {

		for (LightingEffectId effectId : LightingEffectId.values()) {

			try {
				String configLocation = LIGHTING_CONFIG_BASE + effectId + ".json";
				InputStream inputStream = this.getClass().getResourceAsStream(configLocation);
				WledEffectConfig config = JsonUtils.toObject(inputStream, WledEffectConfig.class);
				effectConfigMap.put(effectId, config);
			} catch (Exception e) {
				throw new LaissezException("Failed to load WLED effect configuration: " + effectId, e);
			}
		}
	}

	private List<Integer> colorToRGB(Color color) {

		List<Integer> rgb = new ArrayList<>(3);
		rgb.add((int) (color.getRed() * 255.0));
		rgb.add((int) (color.getGreen() * 255.0));
		rgb.add((int) (color.getBlue() * 255.0));

		return rgb;
	}

	private int calculateControllerBrightness(int brightness) {

		return (int) Math.ceil((brightness / 100.0d) * maxControllerBrightness);
	}

}