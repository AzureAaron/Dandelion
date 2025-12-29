package net.azureaaron.dandelion.impl.utils;

import java.lang.reflect.Type;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemTypeAdapter implements JsonSerializer<Item>, JsonDeserializer<Item> {

	@Override
	public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return BuiltInRegistries.ITEM.getValue(Identifier.parse(json.getAsString().toLowerCase(Locale.CANADA)));
	}

	@Override
	public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(BuiltInRegistries.ITEM.getKey(src).toString());
	}
}
