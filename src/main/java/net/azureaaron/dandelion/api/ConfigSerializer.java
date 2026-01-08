package net.azureaaron.dandelion.api;

import java.nio.file.Path;
import java.util.Objects;

import net.azureaaron.dandelion.impl.ConfigManagerImpl;

public abstract class ConfigSerializer<T> {
	protected final ConfigManagerImpl<T> configManager;
	protected final Path path;

	protected ConfigSerializer(ConfigManagerImpl<T> configManager, Path path) {
		this.configManager = Objects.requireNonNull(configManager, "configManager must not be null");
		this.path = Objects.requireNonNull(path, "path must not be null");;
	}

	public abstract boolean save();

	public abstract boolean load();
}
