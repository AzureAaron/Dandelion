package net.azureaaron.dandelion.api.controllers;

import java.util.function.Function;

import net.azureaaron.dandelion.impl.controllers.EnumControllerImpl;
import net.minecraft.network.chat.Component;

public non-sealed interface EnumController<T extends Enum<T>> extends Controller<T> {

	static <T extends Enum<T>> Builder<T> createBuilder() {
		return new EnumControllerImpl.EnumControllerBuilderImpl<T>();
	}

	boolean dropdown();
	Function<T, Component> formatter();

	interface Builder<T extends Enum<T>> extends Controller.Builder<T, EnumController<T>> {
		Builder<T> dropdown(boolean dropdown);

		Builder<T> formatter(Function<T, Component> formatter);
	}
}
