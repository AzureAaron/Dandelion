package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class OptionGroupImpl implements OptionGroup {
	private final @Nullable Identifier id;
	private final Component name;
	private final List<Component> description;
	private final List<Component> tags;
	private final boolean collapsed;
	private final List<Option<?>> options;

	protected OptionGroupImpl(@Nullable Identifier id, Component name, List<Component> description, List<Component> tags, boolean collapsed, List<Option<?>> options) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.tags = Objects.requireNonNull(tags, "tags must not be null");
		this.collapsed = collapsed;
		this.options = Objects.requireNonNull(options, "options must not be null");
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
	public boolean collapsed() {
		return this.collapsed;
	}

	@Override
	public List<Option<?>> options() {
		return this.options;
	}

	public static class OptionGroupBuilderImpl implements OptionGroup.Builder {
		private @Nullable Identifier id = null;
		private Component name = Component.empty();
		private List<Component> description = List.of();
		private List<Component> tags = List.of();
		private boolean collapsed = false;
		private List<Option<?>> options = new ArrayList<>();

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
		public Builder description(Component... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public Builder tags(Component... tags) {
			this.tags = List.of(tags);
			return this;
		}

		@Override
		public Builder collapsed(boolean collapsed) {
			this.collapsed = collapsed;
			return this;
		}

		@Override
		public Builder option(Option<?> option) {
			this.options.add(option);
			return this;
		}

		@Override
		public OptionGroup build() {
			return new OptionGroupImpl(this.id, this.name, this.description, this.tags, this.collapsed, List.copyOf(this.options));
		}
	}
}
