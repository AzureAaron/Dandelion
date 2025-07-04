package net.azureaaron.dandelion.systems;

public interface OptionBinding<T> {

	T defaultValue();

	T get();

	void set(T value);
}
