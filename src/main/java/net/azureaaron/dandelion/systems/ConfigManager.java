package net.azureaaron.dandelion.systems;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

import com.google.gson.GsonBuilder;

import net.azureaaron.dandelion.impl.ConfigManagerImpl;

public interface ConfigManager<T> {

	static <T> ConfigManager<T> create(Class<T> configClass, Path path, UnaryOperator<GsonBuilder> gsonBuilder) {
		return new ConfigManagerImpl<>(configClass, path, gsonBuilder);
	}

	/**
	 * @return the class backing this config.
	 */
	Class<T> configClass();

	/**
	 * Returns the current instance of the {@code configClass}. Will return a default instance until the config
	 * has been successfully loaded.
	 */
	T instance();

	/**
	 * @return an instance of the {@code configClass} representing the default initialized values.
	 */
	T defaults();

	/**
	 * @return whether the config saved successfully.
	 */
	boolean save();

	/**
	 * @return whether the config loaded successfully.
	 */
	boolean load();
}
