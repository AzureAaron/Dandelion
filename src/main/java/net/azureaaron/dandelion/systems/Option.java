package net.azureaaron.dandelion.systems;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.impl.OptionImpl;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface Option<T> {

	static <T> Builder<T> createBuilder() {
		return new OptionImpl.OptionBuilderImpl<>();
	}

	@Nullable Identifier id();

	Component name();

	List<Component> description();

	List<Component> tags();

	OptionBinding<T> binding();

	Controller<T> controller();

	boolean modifiable();

	List<OptionFlag> flags();

	List<OptionListener<T>> listeners();

	Class<T> type();

	interface Builder<T> {
		Builder<T> id(Identifier id);

		Builder<T> name(Component name);

		Builder<T> description(Component... texts);

		Builder<T> tags(Component... tags);

		Builder<T> binding(T defaultValue, Supplier<T> getter, Consumer<T> setter);

		Builder<T> controller(Controller<T> controller);

		Builder<T> modifiable(boolean modifiable);

		Builder<T> flags(OptionFlag... flags);

		Builder<T> listener(OptionListener<T> listener);

		Option<T> build();
	}
}
