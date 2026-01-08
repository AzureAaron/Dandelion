package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.api.ConfigCategory;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.OptionGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ConfigCategoryImpl implements ConfigCategory {
	private final Identifier id;
	private final Component name;
	private final Component description;
	private final OptionGroup rootGroup;
	private final List<OptionGroup> groups;

	protected ConfigCategoryImpl(Identifier id, Component name, Component description, OptionGroup rootGroup, List<OptionGroup> groups) {
		this.id = Objects.requireNonNull(id, "id must not be null");
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.rootGroup = Objects.requireNonNull(rootGroup, "rootGroup must not be null");
		this.groups = Objects.requireNonNull(groups, "groups must not be null");
	}

	@Override
	public Identifier id() {
		return this.id;
	}

	@Override
	public Component name() {
		return this.name;
	}

	@Override
	public Component description() {
		return this.description;
	}

	@Override
	public @Nullable OptionGroup rootGroup() {
		return this.rootGroup;
	}

	@Override
	public List<OptionGroup> groups() {
		return this.groups;
	}

	public static class ConfigCategoryBuilderImpl implements ConfigCategory.Builder {
		private @Nullable Identifier id = null;
		private Component name = Component.empty();
		private Component description = Component.empty();
		private List<Option<?>> rootOptions = new ArrayList<>();
		private List<OptionGroup> groups = new ArrayList<>();

		@Override
		public Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder name(Component name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder description(Component description) {
			this.description = description;
			return this;
		}

		@Override
		public Builder option(Option<?> option) {
			this.rootOptions.add(option);
			return this;
		}

		@Override
		public Builder group(OptionGroup group) {
			this.groups.add(group);
			return this;
		}

		@Override
		public ConfigCategory build() {
			Objects.requireNonNull(this.id, "an id is required.");
			return new ConfigCategoryImpl(this.id, this.name, this.description, new OptionGroupImpl(null, Component.empty(), List.of(), List.of(), false, List.copyOf(this.rootOptions)), List.copyOf(this.groups));
		}
	}
}
