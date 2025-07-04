package net.azureaaron.dandelion.impl.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.JsonOps;

import net.minecraft.text.Style;

/**
 * This separate serializer with fallback values is required to avoid errors/crashes in unit tests.
 */
public class StyleTypeAdapter implements JsonSerializer<Style>, JsonDeserializer<Style> {

	@Override
	public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Style.Codecs.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(Style.EMPTY);
	}

	@Override
	public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
		return Style.Codecs.CODEC.encodeStart(JsonOps.INSTANCE, src).result().orElse(JsonNull.INSTANCE);
	}
}
