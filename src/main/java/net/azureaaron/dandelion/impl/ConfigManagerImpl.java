package net.azureaaron.dandelion.impl;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.UnaryOperator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.azureaaron.dandelion.impl.utils.ReflectionUtils;
import net.azureaaron.dandelion.systems.ConfigManager;
import net.azureaaron.dandelion.systems.ConfigSerializer;

public class ConfigManagerImpl<T> implements ConfigManager<T> {
	private final Class<T> configClass;
	private final Path path;
	private final Gson gson;
	private final ConfigSerializer<T> serializer;
	private T instance;

	//TODO maybe dont force GSON and abstract it
	public ConfigManagerImpl(Class<T> configClass, Path path, UnaryOperator<GsonBuilder> gsonBuilder) {
		this.configClass = Objects.requireNonNull(configClass, "configClass must not be null");
		this.path = Objects.requireNonNull(path, "path must not be null");
		this.gson = Objects.requireNonNull(gsonBuilder.apply(GsonConfigSerializer.createDefaultGsonBuilder()).create(), "gsonBuilder must not be null");
		this.serializer = new GsonConfigSerializer<>(this, this.path, this.gson);
		this.instance = this.createNewConfigInstance();
	}

	protected T createNewConfigInstance() {
		return ReflectionUtils.createNewDefaultInstance(this.configClass);
	}

	protected void setInstance(T newInstance) {
		this.instance = newInstance;
	}

	@Override
	public Class<T> configClass() {
		return this.configClass;
	}

	@Override
	public T instance() {
		return this.instance;
	}

	@Override
	public T defaults() {
		return this.createNewConfigInstance();
	}

	@Override
	public boolean save() {
		return this.serializer.save();
	}

	@Override
	public boolean load() {
		return this.serializer.load();
	}
}
