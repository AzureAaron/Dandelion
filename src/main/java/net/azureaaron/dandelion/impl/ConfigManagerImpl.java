package net.azureaaron.dandelion.impl;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import org.jspecify.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ConfigSerializer;
import net.azureaaron.dandelion.api.patching.ConfigPatch;
import net.azureaaron.dandelion.impl.patching.ConfigPatcher;
import net.azureaaron.dandelion.impl.utils.ReflectionUtils;
import net.minecraft.resources.Identifier;

public class ConfigManagerImpl<T> implements ConfigManager<T> {
	private final Class<T> configClass;
	private final Path path;
	private final Gson gson;
	private final ConfigSerializer<T> serializer;
	private List<ConfigPatch> patches = List.of();
	private T instance;
	private T unpatchedInstance;

	public ConfigManagerImpl(Class<T> configClass, Path path, UnaryOperator<GsonBuilder> gsonBuilder) {
		this.configClass = Objects.requireNonNull(configClass, "configClass must not be null");
		this.path = Objects.requireNonNull(path, "path must not be null");
		this.gson = Objects.requireNonNull(gsonBuilder.apply(GsonConfigSerializer.createDefaultGsonBuilder()).create(), "gsonBuilder must not be null");
		this.serializer = new GsonConfigSerializer<>(this, this.path, this.gson);
		this.instance = this.createNewConfigInstance();
		this.unpatchedInstance = this.createNewConfigInstance();
	}

	protected T createNewConfigInstance() {
		return ReflectionUtils.createNewDefaultInstance(this.configClass);
	}

	protected void setUnpatchedInstance(T newInstance) {
		this.unpatchedInstance = newInstance;
		this.updatePatchedInstance();
	}

	@Override
	public void updatePatchedInstance() {
		this.instance = this.serializer.copyObject(this.unpatchedInstance);
		ConfigPatcher.applyPatches(this.instance, this.patches);
	}

	/// {@return whether the option with the corresponding {@code id} is patched}
	public boolean isOptionPatched(@Nullable Identifier id) {
		// Short circuit for options without an id
		if (id == null) {
			return false;
		}

		for (ConfigPatch patch : this.patches) {
			if (patch.optionId().isPresent() && patch.optionId().get().equals(id)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setPatches(List<ConfigPatch> patches) {
		this.patches = Objects.requireNonNull(patches, "patches must not be null");
		this.updatePatchedInstance();
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
	public T unpatchedInstance() {
		return this.unpatchedInstance;
	}

	@Override
	public T defaults() {
		return this.createNewConfigInstance();
	}

	@Override
	public boolean save() {
		boolean result = this.serializer.save();
		this.updatePatchedInstance();

		return result;
	}

	@Override
	public boolean load() {
		// No need to update the patched instance since that will be done automatically by the serializer's call to setUnpatchedInstance
		return this.serializer.load();
	}
}
