package net.azureaaron.dandelion.systems;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.impl.OptionImpl;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface Option<T> {

	static <T> Builder<T> createBuilder() {
		return new OptionImpl.OptionBuilderImpl<>();
	}

	@Nullable
	Identifier id();

	Text name();

	List<Text> description();

	OptionBinding<T> binding();

	Controller<T> controller();

	boolean modifiable();

	List<OptionFlag> flags();

	List<OptionListener<T>> listeners();

	Class<T> type();

	interface Builder<T> {
		Builder<T> id(Identifier id);

		Builder<T> name(Text name);

		Builder<T> description(Text... texts);

		Builder<T> binding(T defaultValue, Supplier<T> getter, Consumer<T> setter);

		Builder<T> controller(Controller<T> controller);

		Builder<T> modifiable(boolean modifiable);

		Builder<T> flags(OptionFlag... flags);

		Builder<T> listener(OptionListener<T> listener);

		Option<T> build();
	}
}
