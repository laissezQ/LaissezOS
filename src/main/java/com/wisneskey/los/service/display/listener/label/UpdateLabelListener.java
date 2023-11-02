package com.wisneskey.los.service.display.listener.label;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * Change listener that listens for changes to a numeric property and updates
 * the string property of a label when it changes.
 * 
 * Copyright (C) 2023 Paul Wisneskey
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
public class UpdateLabelListener<T extends Number> implements ChangeListener<T> {

	/**
	 * Value property of the label to update
	 */
	private StringProperty valueProperty;
	private Function<T, String> formatter = String::valueOf;

	// ----------------------------------------------------------------------------------------
	// Constructors.
	// ----------------------------------------------------------------------------------------

	/**
	 * Constructor that takes a label and uses the default string formatter.
	 * 
	 * @param label Label to update when value changes.
	 */
	public UpdateLabelListener(Label label) {
		this.valueProperty = label.textProperty();
	}

	/**
	 * Constructor that takes a label and a custom formatter.
	 * 
	 * @param label     Label to update when value changes.
	 * @param formatter Formatter to use to create the string value.
	 */
	public UpdateLabelListener(Label label, Function<T, String> formatter) {
		this.valueProperty = label.textProperty();
		this.formatter = formatter;
	}

	// ----------------------------------------------------------------------------------------
	// ChangeListener methods.
	// ----------------------------------------------------------------------------------------

	@Override
	public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		Platform.runLater(() -> valueProperty.set(formatter.apply(newValue)));
	}
}