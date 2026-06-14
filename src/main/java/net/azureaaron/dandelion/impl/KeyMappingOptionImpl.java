package net.azureaaron.dandelion.impl;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.api.KeyMappingOption;
import net.azureaaron.dandelion.api.OptionBinding;
import net.azureaaron.dandelion.api.OptionFlag;
import net.azureaaron.dandelion.api.OptionListener;
import net.azureaaron.dandelion.api.controllers.Controller;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class KeyMappingOptionImpl implements KeyMappingOption {
	private final @Nullable Identifier id;
	private final Component name;
	private final List<Component> description;
	private final List<Component> tags;
	private final KeyMapping keyMapping;

	protected KeyMappingOptionImpl(@Nullable Identifier id, Component name, List<Component> description, List<Component> tags, KeyMapping keyMapping) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.tags = Objects.requireNonNull(tags, "tags must not be null");
		this.keyMapping = Objects.requireNonNull(keyMapping, "keyMapping must not be null");
	}

	@Override
	public @Nullable Identifier id() {
		return this.id;
	}

	@Override
	public Component name() {
		return this.name;
	}

	@Override
	public List<Component> description() {
		return this.description;
	}

	@Override
	public List<Component> tags() {
		return this.tags;
	}

	@Override
	public OptionBinding<KeyMapping> binding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Controller<KeyMapping> controller() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean modifiable() {
		// NYI
		return true;
	}

	@Override
	public List<OptionFlag> flags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<OptionListener<KeyMapping>> listeners() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<KeyMapping> type() {
		return KeyMapping.class;
	}

	@Override
	public KeyMapping keyMapping() {
		return this.keyMapping;
	}

	public static class KeyMappingOptionBuilder implements KeyMappingOption.Builder {
		private @Nullable Identifier id = null;
		private Component name = Component.empty();
		private List<Component> description = List.of();
		private List<Component> tags = List.of();
		private @Nullable KeyMapping keyMapping = null;

		@Override
		public KeyMappingOption.Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public KeyMappingOption.Builder name(Component name) {
			this.name = name;
			return this;
		}

		@Override
		public KeyMappingOption.Builder description(Component... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public KeyMappingOption.Builder tags(Component... tags) {
			this.tags = List.of(tags);
			return this;
		}

		@Override
		public KeyMappingOption.Builder keyMapping(KeyMapping keyMapping) {
			this.keyMapping = keyMapping;
			return this;
		}

		@Override
		public KeyMappingOption build() {
			return new KeyMappingOptionImpl(this.id, this.name, this.description, this.tags, this.keyMapping);
		}
	}
}
