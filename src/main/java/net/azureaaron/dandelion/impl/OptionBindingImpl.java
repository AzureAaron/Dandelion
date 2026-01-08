package net.azureaaron.dandelion.impl;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.azureaaron.dandelion.api.OptionBinding;

public class OptionBindingImpl<T> implements OptionBinding<T> {
	private final T defaultValue;
	private final Supplier<T> getter;
	private final Consumer<T> setter;

	protected OptionBindingImpl(T defaultValue, Supplier<T> getter, Consumer<T> setter) {
		this.defaultValue = defaultValue;
		this.getter = Objects.requireNonNull(getter, "getter must not be null");
		this.setter = Objects.requireNonNull(setter, "setter must not be null");
	}

	@Override
	public T defaultValue() {
		return this.defaultValue;
	}

	@Override
	public T get() {
		return this.getter.get();
	}

	@Override
	public void set(T value) {
		this.setter.accept(value);
	}
}
