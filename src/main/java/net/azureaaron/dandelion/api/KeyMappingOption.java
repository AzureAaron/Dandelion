package net.azureaaron.dandelion.api;

import net.azureaaron.dandelion.impl.KeyMappingOptionImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface KeyMappingOption extends Option<KeyMapping> {

	static Builder createBuilder() {
		return new KeyMappingOptionImpl.KeyMappingOptionBuilder();
	}

	KeyMapping keyMapping();

	interface Builder {
		Builder id(Identifier id);

		Builder name(Component name);

		Builder description(Component... texts);

		Builder tags(Component... tags);

		Builder keyMapping(KeyMapping keyMapping);

		KeyMappingOption build();
	}
}
