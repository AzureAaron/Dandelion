package net.azureaaron.dandelion.systems;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.azureaaron.dandelion.impl.ListOptionImpl;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface ListOption<T> extends Option<List<T>>, OptionGroup {
	static <T> Builder<T> createBuilder() {
		return new ListOptionImpl.BuilderImpl<>();
	}

	Supplier<T> initialValue();

	Controller<T> entryController();

	Class<T> entryType();

	interface Builder<T> {
		Builder<T> id(Identifier id);

		Builder<T> name(Component name);

		Builder<T> description(Component... texts);

		Builder<T> tags(Component... tags);

		Builder<T> binding(List<T> defaultValue, Supplier<List<T>> getter, Consumer<List<T>> setter);

		Builder<T> controller(Controller<T> controller);

		Builder<T> initial(Supplier<T> initialValue);

		Builder<T> initial(T initialValue);

		Builder<T> collapsed(boolean collapsed);

		Builder<T> modifiable(boolean modifiable);

		Builder<T> flags(OptionFlag... flags);

		Builder<T> listener(OptionListener<List<T>> listener);

		ListOption<T> build();
	}
}
