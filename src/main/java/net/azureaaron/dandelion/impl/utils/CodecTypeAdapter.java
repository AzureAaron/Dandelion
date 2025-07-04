package net.azureaaron.dandelion.impl.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

/**
 * Creates a GSON type adapter from a {@link Codec}.
 */
public record CodecTypeAdapter<T>(Codec<T> codec) implements JsonSerializer<T>, JsonDeserializer<T> {

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return this.codec.parse(JsonOps.INSTANCE, json).getOrThrow(JsonParseException::new);
	}

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		return this.codec.encodeStart(JsonOps.INSTANCE, src).getOrThrow();
	}
}
