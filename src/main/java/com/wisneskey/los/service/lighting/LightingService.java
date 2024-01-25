package com.wisneskey.los.service.lighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wisneskey.los.error.LaissezException;
import com.wisneskey.los.kernel.RunMode;
import com.wisneskey.los.service.AbstractService;
import com.wisneskey.los.service.ServiceId;
import com.wisneskey.los.service.lighting.driver.DummyLightingDriver;
import com.wisneskey.los.service.lighting.driver.LightingDriver;
import com.wisneskey.los.service.lighting.driver.wled.WledLightingDriver;
import com.wisneskey.los.service.profile.model.Profile;
import com.wisneskey.los.state.LightingState;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Service for running lighting animations.
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
public class LightingService extends AbstractService<LightingState> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightingService.class);

	/**
	 * Internal state object for tracking the state of the lighting system.
	 */
	private InternalLightingState lightingState;

	/**
	 * Driver to use to control the lighting.
	 */
	private LightingDriver lightingDriver;

	/**
	 * The last stored lighting state.
	 */
	private SavedLightingState savedState;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Private constructor to require use of static service creation method.
	 */
	private LightingService() {
		super(ServiceId.LIGHTING);
	}

	// ----------------------------------------------------------------------------------------
	// Service methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public LightingState getState() {
		return lightingState;
	}

	@Override
	public void terminate() {

		lightingDriver.terminate();
		LOGGER.info("Lighting service terminated.");
	}

	// ----------------------------------------------------------------------------------------
	// Public methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Start playing the designated lighting effect.
	 * 
	 * @param effectId           Id of the lighting effect to play.
	 * @param foregroundOverride Hex string to override current foreground color
	 *                             with.
	 * @param backgroundOverride Hex string to override current background color
	 *                             with.
	 */
	public void playEffect(LightingEffectId effectId) {

		LOGGER.info("Playing lighting effect: {}", effectId);
		
		lightingState.setCurrentEffect(effectId);
		lightingDriver.playEffect(effectId, lightingState);
	}

	/**
	 * Captures the current state so that it can be restored at a later point.
	 * Usually used to save lighting state before opening chap mode and then
	 * resetting it when chap mode is exited.
	 */
	public void storeCurrentState() {

		LOGGER.info("Storing lighting state.");

		SavedLightingState state = new SavedLightingState();
		state.setEffectId(lightingState.currentEffect().get());
		state.setBrightness(lightingState.brightness().get());
		state.setIntensity(lightingState.intensity().get());
		state.setReversed(lightingState.reversed().get());
		state.setSpeed(lightingState.speed().get());
		state.setFirstColor(lightingState.firstColor().get());
		state.setSecondColor(lightingState.secondColor().get());
		state.setThirdColor(lightingState.thirdColor().get());

		savedState = state;
	}

	/**
	 * Restores a previously saved lighting state.
	 */
	public void restoreState() {

		if (savedState == null) {
			LOGGER.warn("No lighting state stored.");
			return;
		}

		LOGGER.info("Restoring lighting state.");

		// First start playing the effect since it takes a bit of time to transition
		// and we can
		// update all the settings as the transition is starting.
		playEffect(savedState.getEffectId());
		lightingState.reversed().set(savedState.getReversed());
		lightingState.brightness().set(savedState.getBrightness());
		lightingState.intensity().set(savedState.getIntensity());
		lightingState.speed().set(savedState.getSpeed());
		lightingState.firstColor().set(savedState.getFirstColor());
		lightingState.secondColor().set(savedState.getSecondColor());
		lightingState.thirdColor().set(savedState.getThirdColor());
	}

	// ----------------------------------------------------------------------------------------
	// Supporting methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Sets the driver to use for communicating with the lights.
	 * 
	 * @param lightingDriver Lighting driver to use to communicate with the
	 *                         lighting strips.
	 */
	private void setLightingDriver(LightingDriver lightingDriver) {
		this.lightingDriver = lightingDriver;
	}

	/**
	 * Initializes the service and its lighting driver and returns the initial
	 * state.
	 * 
	 * @param  profile Profile to use to configure the relay state.
	 * @return         Configured display state object.
	 */
	private LightingState initialize(Profile profile) {

		if (lightingDriver == null) {
			throw new LaissezException("Lighting driver not set.");
		}

		lightingState = new InternalLightingState(profile);

		// Let the lighting driver initialize itself based on the profile.
		lightingDriver.initialize(profile, lightingState);

		// Monitor the state for changes to brightness and colors so we can apply
		// them immediately.
		lightingState.brightness.addListener(new BrightnessListener());
		lightingState.speed.addListener(new SpeedListener());
		lightingState.intensity.addListener(new IntensityListener());
		lightingState.reversed.addListener(new ReversedListener());
		lightingState.firstColor.addListener(new ColorListener());
		lightingState.secondColor.addListener(new ColorListener());
		lightingState.thirdColor.addListener(new ColorListener());

		return lightingState;
	}

	// ----------------------------------------------------------------------------------------
	// Static service creation methods.
	// ----------------------------------------------------------------------------------------

	/**
	 * Creates an instance of the lighting service along with its initial state as
	 * set from the supplied profile.
	 * 
	 * @param  runMode Run mode for the operating system.
	 * @param  profile Profile to use for configuring initial state of the
	 *                   lighting service.
	 * @return         Lighting service instance and its initial state object.
	 */
	public static Pair<LightingService, LightingState> createService(RunMode runMode, Profile profile) {

		LightingService service = new LightingService();

		if (profile.getUseRealLighting()) {

			// Use the ESP32 regardless of the mode.
			service.setLightingDriver(new WledLightingDriver());

		} else {

			// Set the lighting driver based on the run mode.
			switch (runMode) {
			case CHAIR:
				service.setLightingDriver(new WledLightingDriver());
				break;
			case DEV:
				service.setLightingDriver(new DummyLightingDriver());
				break;
			default:
				throw new LaissezException("Unknown run mode during lighting driver selection: " + runMode);
			}
		}

		// Give the service a chance to initialize.
		LightingState state = service.initialize(profile);

		return new Pair<>(service, state);
	}

	// ----------------------------------------------------------------------------------------
	// Inner classes.
	// ----------------------------------------------------------------------------------------

	/**
	 * Property listener for letting the LED controller know when the brightness
	 * has changed.
	 */
	private class BrightnessListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			lightingDriver.changeBrightness(newValue.intValue());
		}
	}

	/**
	 * Property listener for letting the LED controller know when the speed has
	 * changed.
	 */
	private class SpeedListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			lightingDriver.changeSpeed(newValue.intValue());
		}
	}

	/**
	 * Property listener for letting the LED controller know when the intensity
	 * has changed.
	 */
	private class IntensityListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			lightingDriver.changeIntensity(newValue.intValue());
		}
	}

	/**
	 * Property listener for letting the LED controller know when the intensity
	 * has changed.
	 */
	private class ReversedListener implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			lightingDriver.changeReversed(newValue.booleanValue());
		}
	}

	/**
	 * Property listener for letting the LED controller know when any of the
	 * colors have changed.
	 */
	private class ColorListener implements ChangeListener<Color> {

		@Override
		public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
			lightingDriver.changeColor(lightingState);
		}
	}

	/**
	 * Class used to save a lighting state.
	 */
	private static class SavedLightingState {

		private Integer brightness;
		private Integer speed;
		private Integer intensity;
		private Boolean reversed;
		private LightingEffectId effectId;
		private Color firstColor;
		private Color secondColor;
		private Color thirdColor;

		// ----------------------------------------------------------------------------------------
		// Property getters/setters.
		// ----------------------------------------------------------------------------------------

		public Integer getBrightness() {
			return brightness;
		}

		public void setBrightness(Integer brightness) {
			this.brightness = brightness;
		}

		public Integer getSpeed() {
			return speed;
		}

		public void setSpeed(Integer speed) {
			this.speed = speed;
		}

		public Integer getIntensity() {
			return intensity;
		}

		public void setIntensity(Integer intensity) {
			this.intensity = intensity;
		}

		public Boolean getReversed() {
			return reversed;
		}

		public void setReversed(Boolean reversed) {
			this.reversed = reversed;
		}

		public LightingEffectId getEffectId() {
			return effectId;
		}

		public void setEffectId(LightingEffectId effectId) {
			this.effectId = effectId;
		}

		public Color getFirstColor() {
			return firstColor;
		}

		public void setFirstColor(Color firstColor) {
			this.firstColor = firstColor;
		}

		public Color getSecondColor() {
			return secondColor;
		}

		public void setSecondColor(Color secondColor) {
			this.secondColor = secondColor;
		}

		public Color getThirdColor() {
			return thirdColor;
		}

		public void setThirdColor(Color thirdColor) {
			this.thirdColor = thirdColor;
		}
	}

	/**
	 * Internal state object for the lighting state.
	 */
	private static class InternalLightingState implements LightingState {

		private SimpleIntegerProperty brightness;

		private SimpleIntegerProperty speed;

		private SimpleIntegerProperty intensity;

		private SimpleBooleanProperty reversed;

		private SimpleObjectProperty<LightingEffectId> currentEffect;

		private SimpleObjectProperty<Color> firstColor;

		private SimpleObjectProperty<Color> secondColor;

		private SimpleObjectProperty<Color> thirdColor;

		// ----------------------------------------------------------------------------------------
		// Constructors.
		// ----------------------------------------------------------------------------------------

		private InternalLightingState(Profile profile) {
			this.currentEffect = new SimpleObjectProperty<>(LightingEffectId.ALL_OFF);
			this.brightness = new SimpleIntegerProperty(profile.getBrightness());
			this.speed = new SimpleIntegerProperty();
			this.intensity = new SimpleIntegerProperty();
			this.reversed = new SimpleBooleanProperty(false);
			this.firstColor = new SimpleObjectProperty<>(Color.web(profile.getFirstColor()));
			this.secondColor = new SimpleObjectProperty<>(Color.web(profile.getSecondColor()));
			this.thirdColor = new SimpleObjectProperty<>(Color.web(profile.getThirdColor()));
		}

		// ----------------------------------------------------------------------------------------
		// Lighting State methods.
		// ----------------------------------------------------------------------------------------

		@Override
		public ReadOnlyObjectProperty<LightingEffectId> currentEffect() {
			return currentEffect;
		}

		@Override
		public IntegerProperty brightness() {
			return brightness;
		}

		@Override
		public IntegerProperty speed() {
			return speed;
		}

		@Override
		public IntegerProperty intensity() {
			return intensity;
		}

		@Override
		public BooleanProperty reversed() {
			return reversed;
		}

		@Override
		public ObjectProperty<Color> firstColor() {
			return firstColor;
		}

		@Override
		public ObjectProperty<Color> secondColor() {
			return secondColor;
		}

		@Override
		public ObjectProperty<Color> thirdColor() {
			return thirdColor;
		}

		// ----------------------------------------------------------------------------------------
		// Private methods.
		// ----------------------------------------------------------------------------------------

		private void setCurrentEffect(LightingEffectId effectId) {
			currentEffect.set(effectId);
		}
	}
}