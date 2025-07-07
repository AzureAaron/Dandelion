package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.systems.ConfigCategory;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigCategoryImpl implements ConfigCategory {
	private final Identifier id;
	private final Text name;
	private final Text description;
	private final OptionGroup rootGroup;
	private final List<OptionGroup> groups;

	protected ConfigCategoryImpl(Identifier id, Text name, Text description, OptionGroup rootGroup, List<OptionGroup> groups) {
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
	public Text name() {
		return this.name;
	}

	@Override
	public Text description() {
		return this.description;
	}

	@Override
	@Nullable
	public OptionGroup rootGroup() {
		return this.rootGroup;
	}

	@Override
	public List<OptionGroup> groups() {
		return this.groups;
	}

	public static class ConfigCategoryBuilderImpl implements ConfigCategory.Builder {
		private Identifier id = null;
		private Text name = Text.empty();
		private Text description = Text.empty();
		private List<Option<?>> rootOptions = new ArrayList<>();
		private List<OptionGroup> groups = new ArrayList<>();

		@Override
		public Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder name(Text name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder description(Text description) {
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
			return new ConfigCategoryImpl(this.id, this.name, this.description, new OptionGroupImpl(null, Text.empty(), List.of(), List.of(), false, List.copyOf(this.rootOptions)), List.copyOf(this.groups));
		}
	}
}
