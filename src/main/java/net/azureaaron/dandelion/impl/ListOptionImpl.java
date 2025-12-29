package net.azureaaron.dandelion.impl;

import net.azureaaron.dandelion.impl.utils.ReflectionUtils;
import net.azureaaron.dandelion.systems.ListOption;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListOptionImpl<T> implements ListOption<T> {
	@Nullable
	private final Identifier id;
	private final Text name;
	private final List<Text> description;
	private final List<Text> tags;
	private final OptionBinding<List<T>> binding;
	private final Controller<T> controller;
	private final Supplier<T> initialValue;
	private final boolean collapsed;
	private final boolean modifiable;
	private final List<OptionFlag> flags;
	private final List<OptionListener<List<T>>> listeners;
	private final Class<List<T>> type;
	private final Class<T> entryType;
	private final List<Option<T>> entries;

	@SuppressWarnings("unchecked")
	public ListOptionImpl(@Nullable Identifier id, Text name, List<Text> description, List<Text> tags, OptionBinding<List<T>> binding, Controller<T> controller, Supplier<T> initialValue, boolean collapsed, boolean modifiable, List<OptionFlag> flags, List<OptionListener<List<T>>> listeners) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.tags = Objects.requireNonNull(tags, "tags must not be null");
		this.binding = Objects.requireNonNull(binding, "binding must not be null");
		this.controller = Objects.requireNonNull(controller, "controller must not be null");
		this.initialValue = Objects.requireNonNull(initialValue, "initialValue must not be null");
		this.collapsed = collapsed;
		this.modifiable = modifiable;
		this.flags = Objects.requireNonNull(flags, "flags must not be null");
		this.listeners = Objects.requireNonNull(listeners, "listeners must not be null");
		this.type = (Class<List<T>>) ReflectionUtils.getActualClass(Objects.requireNonNull(this.binding().defaultValue(), "the default value of an list option must not be null"));
		this.entryType = (Class<T>) ReflectionUtils.getActualClass(Objects.requireNonNull(this.initialValue.get(), "the initial value of an entry in an list option must not be null"));
		this.entries = createEntries(this.binding().get());
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
	public List<Text> tags() {
		return this.tags;
	}

	@Override
	public OptionBinding<List<T>> binding() {
		return this.binding;
	}

	@Override
	public Controller<List<T>> controller() {
		throw new UnsupportedOperationException("list options do not have a controller, see entryController() instead");
	}

	@Override
	public Controller<T> entryController() {
		return this.controller;
	}

	@Override
	public Supplier<T> initialValue() {
		return this.initialValue;
	}

	@Override
	public boolean collapsed() {
		return this.collapsed;
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
	public List<OptionListener<List<T>>> listeners() {
		return this.listeners;
	}

	@Override
	public Class<List<T>> type() {
		return this.type;
	}

	@Override
	public Class<T> entryType() {
		return this.entryType;
	}

	@Override
	public List<Option<T>> options() {
		return entries;
	}

	private List<Option<T>> createEntries(List<T> values) {
		ArrayList<Option<T>> entries = new ArrayList<>();
		for (int i = 0; i < values.size(); i++) {
			entries.add(createEntry(values, i));
		}
		return entries;
	}

	private Option<T> createEntry(List<T> values, int i) {
		return new OptionImpl<>(
				id,
				name(),
				description(),
				tags(),
				new OptionBindingImpl<>(initialValue().get(), () -> values.get(i), newValue -> values.set(i, newValue)),
				entryController(),
				modifiable(),
				flags(),
				List.of((entryOption, updateType) -> listeners().forEach(l -> l.onUpdate(this, updateType)))
		);
	}

	public static class BuilderImpl<T> implements ListOption.Builder<T> {
		private @Nullable Identifier id = null;
		private Text name = Text.empty();
		private List<Text> description = java.util.List.of();
		private List<Text> tags = List.of();
		private @Nullable OptionBinding<List<T>> binding = null;
		private @Nullable Controller<T> controller = null;
		private @Nullable Supplier<T> initialValue = null;
		private boolean collapsed = false;
		private boolean modifiable = true;
		private List<OptionFlag> flags = List.of();
		private final List<OptionListener<List<T>>> listeners = new ArrayList<>();

		@Override
		public ListOption.Builder<T> id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public ListOption.Builder<T> name(Text name) {
			this.name = name;
			return this;
		}

		@Override
		public ListOption.Builder<T> description(Text... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public ListOption.Builder<T> tags(Text... tags) {
			this.tags = List.of(tags);
			return this;
		}

		@Override
		public ListOption.Builder<T> binding(List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter) {
			this.binding = new OptionBindingImpl<>(defaultValue, getter, setter);
			return this;
		}

		@Override
		public ListOption.Builder<T> controller(Controller<T> controller) {
			this.controller = controller;
			return this;
		}

		@Override
		public ListOption.Builder<T> initial(Supplier<T> initialValue) {
			this.initialValue = initialValue;
			return this;
		}

		@Override
		public ListOption.Builder<T> initial(T initialValue) {
			this.initialValue = () -> initialValue;
			return this;
		}

		@Override
		public ListOption.Builder<T> collapsed(boolean collapsed) {
			this.collapsed = collapsed;
			return this;
		}

		@Override
		public ListOption.Builder<T> modifiable(boolean modifiable) {
			this.modifiable = modifiable;
			return this;
		}

		@Override
		public ListOption.Builder<T> flags(OptionFlag... flags) {
			this.flags = List.of(flags);
			return this;
		}

		@Override
		public ListOption.Builder<T> listener(OptionListener<List<T>> listener) {
			this.listeners.add(listener);
			return this;
		}

		@Override
		public ListOption<T> build() {
			Objects.requireNonNull(this.binding, "a binding is required.");
			Objects.requireNonNull(this.controller, "a controller is required.");
			Objects.requireNonNull(this.initialValue, "an initial value is required.");
			return new ListOptionImpl<>(this.id, this.name, this.description, this.tags, this.binding, this.controller, this.initialValue, this.collapsed, this.modifiable, this.flags, List.copyOf(this.listeners));
		}
	}
}
