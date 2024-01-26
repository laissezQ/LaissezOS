package com.wisneskey.los.util;

import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Change listener for monitoring a property change and dispatching it to a
 * handler.
 */
public class PropertyChangeListener<T> implements ChangeListener<T> {

	private Consumer<T> consumer;

	public PropertyChangeListener(Consumer<T> consumer) {
		this.consumer = consumer;

	}

	@Override
	public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		consumer.accept(newValue);
	}
}