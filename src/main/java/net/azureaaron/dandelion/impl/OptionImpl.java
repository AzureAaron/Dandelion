package net.azureaaron.dandelion.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.OptionBinding;
import net.azureaaron.dandelion.api.OptionFlag;
import net.azureaaron.dandelion.api.OptionListener;
import net.azureaaron.dandelion.api.controllers.BooleanController;
import net.azureaaron.dandelion.api.controllers.ColourController;
import net.azureaaron.dandelion.api.controllers.Controller;
import net.azureaaron.dandelion.api.controllers.EnumController;
import net.azureaaron.dandelion.api.controllers.FloatController;
import net.azureaaron.dandelion.api.controllers.IntegerController;
import net.azureaaron.dandelion.api.controllers.ItemController;
import net.azureaaron.dandelion.api.controllers.StringController;
import net.azureaaron.dandelion.impl.utils.ReflectionUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class OptionImpl<T> implements Option<T> {
	private final @Nullable Identifier id;
	private final Component name;
	private final List<Component> description;
	private final List<Component> tags;
	private final OptionBinding<T> binding;
	private final Controller<T> controller;
	private final boolean modifiable;
	private final List<OptionFlag> flags;
	private final List<OptionListener<T>> listeners;
	private final Class<T> type;

	@SuppressWarnings("unchecked")
	protected OptionImpl(@Nullable Identifier id, Component name, List<Component> description, List<Component> tags, OptionBinding<T> binding, Controller<T> controller, boolean modifiable, List<OptionFlag> flags, List<OptionListener<T>> listeners) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.tags = Objects.requireNonNull(tags, "tags must not be null");
		this.binding = Objects.requireNonNull(binding, "binding must not be null");
		this.controller = Objects.requireNonNull(controller, "controller must not be null");
		this.modifiable = modifiable;
		this.flags = Objects.requireNonNull(flags, "flags must not be null");
		this.listeners = Objects.requireNonNull(listeners, "listeners must not be null");
		this.type = (Class<T>) ReflectionUtils.getActualClass(Objects.requireNonNull(this.binding.defaultValue(), "the default value of an option must not be null"));

		//Require that we receive the correct type, otherwise the library will likely crash later!
		checkType(this);
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

	/**
	 * Checks that the {@code type} of the option matches the type required by the {@code controller}.
	 */
	protected static <T> void checkType(Option<T> option) {
		boolean hasCorrectType = switch (option.controller()) {
			case BooleanController booleanController -> option.type() == boolean.class || option.type() == Boolean.class;
			case ColourController colourController -> option.type() == Color.class;
			case EnumController<?> enumController -> option.type().isEnum();
			case FloatController floatController -> option.type() == float.class || option.type() == Float.class;
			case IntegerController integerController -> option.type() == int.class || option.type() == Integer.class;
			case ItemController itemController -> Item.class.isAssignableFrom(option.type());
			case StringController stringController -> option.type() == String.class;
		};

		if (!hasCorrectType) {
			String name = option.id() != null ? option.id().toString() : option.name().getString();
			//All the controller interface classes implement their respective interface so this *should* always yield one (and the correct one at that!)
			Class<?> controllerInterfaceType = option.controller().getClass().getInterfaces()[0];
			String expected = switch (option.controller()) {
				case BooleanController booleanController -> "boolean";
				case ColourController colourController -> "Color";
				case EnumController<?> enumController -> "Enum";
				case FloatController floatController -> "float";
				case IntegerController integerController -> "int";
				case ItemController itemController -> "Item";
				case StringController stringController -> "String";
			};
			String message = String.format("[Dandelion] Option %s has mismatched type with controller (%s)! Expected a %s but got %s!", name, controllerInterfaceType, expected, option.type());

			throw new RuntimeException(message);
		}
	}

	public static class OptionBuilderImpl<T> implements Option.Builder<T> {
		private @Nullable Identifier id = null;
		private Component name = Component.empty();
		private List<Component> description = List.of();
		private List<Component> tags = List.of();
		private @Nullable OptionBinding<T> binding = null;
		private @Nullable Controller<T> controller = null;
		private boolean modifiable = true;
		private List<OptionFlag> flags = List.of();
		private List<OptionListener<T>> listeners = new ArrayList<>();

		@Override
		public Builder<T> id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder<T> name(Component name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder<T> description(Component... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public Builder<T> tags(Component... tags) {
			this.tags = List.of(tags);
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
			Objects.requireNonNull(this.binding, "a binding is required.");
			Objects.requireNonNull(this.controller, "a controller is required.");
			return new OptionImpl<>(this.id, this.name, this.description, this.tags, this.binding, this.controller, this.modifiable, this.flags, List.copyOf(this.listeners));
		}
	}
}
