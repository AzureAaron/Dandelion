package net.azureaaron.dandelion.impl;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.UnaryOperator;

import org.jspecify.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ConfigSerializer;
import net.azureaaron.dandelion.impl.utils.ReflectionUtils;

public class ConfigManagerImpl<T> implements ConfigManager<T> {
	private final Class<T> configClass;
	private final Path path;
	private final Gson gson;
	private final ConfigSerializer<T> serializer;
	private @Nullable T instance;

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

	protected void setInstance(@Nullable T newInstance) {
		this.instance = newInstance;
	}

	@Override
	public Class<T> configClass() {
		return this.configClass;
	}

	@Override
	public @Nullable T instance() {
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
