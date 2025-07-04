package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.impl.utils.ReflectionUtils;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OptionImpl<T> implements Option<T> {
	@Nullable
	private final Identifier id;
	private final Text name;
	private final List<Text> description;
	private final OptionBinding<T> binding;
	private final Controller<T> controller;
	private final boolean modifiable;
	private final List<OptionFlag> flags;
	private final List<OptionListener<T>> listeners;
	private final Class<T> type;

	@SuppressWarnings("unchecked")
	protected OptionImpl(@Nullable Identifier id, Text name, List<Text> description, OptionBinding<T> binding, Controller<T> controller, boolean modifiable, List<OptionFlag> flags, List<OptionListener<T>> listeners) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.binding = Objects.requireNonNull(binding, "binding must not be null");
		this.controller = Objects.requireNonNull(controller, "controller must not be null");
		this.modifiable = modifiable;
		this.flags = Objects.requireNonNull(flags, "flags must not be null");
		this.listeners = Objects.requireNonNull(listeners, "listeners must not be null");
		this.type = (Class<T>) ReflectionUtils.getActualClass(Objects.requireNonNull(this.binding.defaultValue(), "the default value of an option must not be null"));
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
	public OptionBinding<T> binding() {
		return this.binding;
	}

	@Override
	public Controller<T> controller() {
		return this.controller;
	}

	@Override
	public boolean modifiable() {
		return this.modifiable;
	}

	@Override
	public List<OptionFlag> flags() {
		return this.flags;
	}

	@Override
	public List<OptionListener<T>> listeners() {
		return this.listeners;
	}

	@Override
	public Class<T> type() {
		return this.type;
	}

	public static class OptionBuilderImpl<T> implements Option.Builder<T> {
		private Identifier id = null;
		private Text name = Text.empty();
		private List<Text> description = List.of();
		private OptionBinding<T> binding = null;
		private Controller<T> controller = null;
		private boolean modifiable = true;
		private List<OptionFlag> flags = List.of();
		private List<OptionListener<T>> listeners = new ArrayList<>();

		@Override
		public Builder<T> id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder<T> name(Text name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder<T> description(Text... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public Builder<T> binding(T defaultValue, Supplier<T> getter, Consumer<T> setter) {
			this.binding = new OptionBindingImpl<>(defaultValue, getter, setter);
			return this;
		}

		@Override
		public Builder<T> controller(Controller<T> controller) {
			this.controller = controller;
			return this;
		}

		@Override
		public Builder<T> modifiable(boolean modifiable) {
			this.modifiable = modifiable;
			return this;
		}

		@Override
		public Builder<T> flags(OptionFlag... flags) {
			this.flags = List.of(flags);
			return this;
		}

		@Override
		public Builder<T> listener(OptionListener<T> listener) {
			this.listeners.add(listener);
			return this;
		}

		@Override
		public Option<T> build() {
			return new OptionImpl<>(this.id, this.name, this.description, this.binding, this.controller, this.modifiable, this.flags, List.copyOf(this.listeners));
		}
	}
}
