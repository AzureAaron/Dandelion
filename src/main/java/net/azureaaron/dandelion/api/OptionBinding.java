package net.azureaaron.dandelion.api;

public interface OptionBinding<T> {

	T defaultValue();

	T get();

	void set(T value);
}
