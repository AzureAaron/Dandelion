package net.azureaaron.dandelion.impl;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
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

	@Override
	public boolean save() {
		T instance = this.configManager.instance();

		//We can't save a null config instance since that would not work at all and shouldn't be done in general
		if (instance == null) {
			LOGGER.warn("[Dandelion] Cannot save a null instance of {} to {}! Skipping save.", this.configManager.configClass(), this.path);
			return false;
		}

		try {
			String json = this.gson.toJson(instance, this.configManager.configClass());

			Files.createDirectories(this.path.getParent());
			Files.writeString(this.path, json, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			LOGGER.info("[Dandelion] Successfully saved {} to {}.", this.configManager.configClass(), this.path);

			return true;
		} catch (Exception e) {
			LOGGER.error(LogUtils.FATAL_MARKER, "[Dandelion] Failed to save {} to {}!", this.configManager.configClass(), this.path, e);
		}

		return false;
	}

	@Override
	public boolean load() {
		if (!Files.exists(this.path)) {
			LOGGER.info("[Dandelion] No config found for {} at {}. Initializing and saving default config.", this.configManager.configClass(), this.path);
			this.configManager.setInstance(this.configManager.createNewConfigInstance());
			this.save();

			return true;
		}

		try {
			String config = Files.readString(this.path);
			T instance = this.gson.fromJson(JsonParser.parseString(config), this.configManager.configClass());

			this.configManager.setInstance(instance);
			LOGGER.info("[Dandelion] Successfully loaded {} from {}.", this.configManager.configClass(), this.path);

			return true;
		} catch (Exception e) {
			LOGGER.error(LogUtils.FATAL_MARKER, "[Dandelion] Failed to load the {} from {}!", this.configManager.configClass(), this.path, e);
		}

		return false;
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
