package net.azureaaron.dandelion.impl;

import java.awt.Color;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.slf4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;

import net.azureaaron.dandelion.api.ConfigSerializer;
import net.azureaaron.dandelion.impl.utils.CodecTypeAdapter;
import net.azureaaron.dandelion.impl.utils.CodecUtils;
import net.azureaaron.dandelion.impl.utils.ItemTypeAdapter;
import net.azureaaron.dandelion.impl.utils.StyleTypeAdapter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class GsonConfigSerializer<T> extends ConfigSerializer<T> {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final Gson gson;

	protected GsonConfigSerializer(ConfigManagerImpl<T> configManager, Path path, Gson gson) {
		super(configManager, path);
		this.gson = Objects.requireNonNull(gson, "gson must not be null");;
	}

	/// Saves the config file.
	///
	/// Uses the atomic move pattern (when available) to reduce the chance of corrupting the config file.
	@Override
	public boolean save() {
		T unpatchedInstance = this.configManager.unpatchedInstance();

		// We can't save a null config instance since that would not work at all and shouldn't be done in general
		if (unpatchedInstance == null) {
			LOGGER.warn("[Dandelion] Cannot save a null instance of {} to {}! Skipping save.", this.configManager.configClass(), this.path);
			return false;
		}

		Path temporaryFile = null;
		try {
			String json = this.gson.toJson(unpatchedInstance, this.configManager.configClass());

			// Write to temporary file
			temporaryFile = Files.createTempFile("dandelion_config", null);
			Files.writeString(temporaryFile, json, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

			// Create the directories that the config file needs if necessary
			Files.createDirectories(this.path.getParent());

			try {
				// Atomically move the file to the proper config location
				Files.move(temporaryFile, this.path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException _) {
				// Fall back to non-atomic move if it is not supported (moving across partitions/drives)
				Files.move(temporaryFile, this.path, StandardCopyOption.REPLACE_EXISTING);
			}

			LOGGER.info("[Dandelion] Successfully saved {} to {}.", this.configManager.configClass(), this.path);

			return true;
		} catch (Exception e) {
			LOGGER.error(LogUtils.FATAL_MARKER, "[Dandelion] Failed to save {} to {}!", this.configManager.configClass(), this.path, e);

			// Delete temporary file so it doesn't persist (not needed normally since it gets moved)
			try {
				if (temporaryFile != null) {
					Files.deleteIfExists(temporaryFile);
				}
			} catch (Exception _) {}
		}

		return false;
	}

	@Override
	public boolean load() {
		if (!Files.exists(this.path)) {
			LOGGER.info("[Dandelion] No config found for {} at {}. Initializing and saving default config.", this.configManager.configClass(), this.path);
			this.configManager.setUnpatchedInstance(this.configManager.createNewConfigInstance());
			this.save();

			return true;
		}

		try {
			String config = Files.readString(this.path);
			T instance = this.gson.fromJson(JsonParser.parseString(config), this.configManager.configClass());

			this.configManager.setUnpatchedInstance(instance);
			LOGGER.info("[Dandelion] Successfully loaded {} from {}.", this.configManager.configClass(), this.path);

			return true;
		} catch (Exception e) {
			LOGGER.error(LogUtils.FATAL_MARKER, "[Dandelion] Failed to load the {} from {}!", this.configManager.configClass(), this.path, e);
		}

		return false;
	}

	@Override
	public T copyObject(T source) {
		// Copies the object by converting it to JSON and back
		return this.gson.fromJson(this.gson.toJson(source, this.configManager.configClass()), this.configManager.configClass());
	}

	protected static GsonBuilder createDefaultGsonBuilder() {
		return new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
				.serializeNulls()
				.registerTypeHierarchyAdapter(Color.class, new CodecTypeAdapter<>(CodecUtils.COLOUR_CODEC))
				.registerTypeHierarchyAdapter(Component.class, new CodecTypeAdapter<>(ComponentSerialization.CODEC))
				.registerTypeHierarchyAdapter(Style.class, new StyleTypeAdapter())
				.registerTypeHierarchyAdapter(Identifier.class, new CodecTypeAdapter<>(Identifier.CODEC))
				.registerTypeHierarchyAdapter(Item.class, new ItemTypeAdapter())
				.setPrettyPrinting();
	}
}
