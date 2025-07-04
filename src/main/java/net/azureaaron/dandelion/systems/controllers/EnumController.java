package net.azureaaron.dandelion.systems.controllers;

import java.util.function.Function;

import net.azureaaron.dandelion.impl.controllers.EnumControllerImpl;
import net.minecraft.text.Text;

public non-sealed interface EnumController<T extends Enum<T>> extends Controller<T> {

	static <T extends Enum<T>> Builder<T> createBuilder() {
		return new EnumControllerImpl.EnumControllerBuilderImpl<T>();
	}

	boolean dropdown();
	Function<T, Text> formatter();

	interface Builder<T extends Enum<T>> extends Controller.Builder<T, EnumController<T>> {
		Builder<T> dropdown(boolean dropdown);

		Builder<T> formatter(Function<T, Text> formatter);
	}
}
