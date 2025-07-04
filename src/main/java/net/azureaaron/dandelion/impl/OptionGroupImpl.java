package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OptionGroupImpl implements OptionGroup {
	@Nullable
	private final Identifier id;
	private final Text name;
	private final List<Text> description;
	private final boolean collapsed;
	private final List<Option<?>> options;

	protected OptionGroupImpl(@Nullable Identifier id, Text name, List<Text> description, boolean collapsed, List<Option<?>> options) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.collapsed = collapsed;
		this.options = Objects.requireNonNull(options, "options must not be null");
	}

	@Override
	@Nullable
	public Identifier id() {
		return this.id;
	}

	@Override
	public Text name() {
		return this.name;
	}

	@Override
	public List<Text> description() {
		return this.description;
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
		private Identifier id = null;
		private Text name = Text.empty();
		private List<Text> description = List.of();
		private boolean collapsed = false;
		private List<Option<?>> options = new ArrayList<>();

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
		public Builder description(Text... texts) {
			this.description = List.of(texts);
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
			return new OptionGroupImpl(this.id, this.name, this.description, this.collapsed, List.copyOf(this.options));
		}
	}
}
