package net.azureaaron.dandelion.impl.controllers;

import java.util.Objects;
import java.util.function.Function;

import net.azureaaron.dandelion.api.controllers.EnumController;
import net.minecraft.network.chat.Component;

public class EnumControllerImpl<T extends Enum<T>> implements EnumController<T> {
	private static final Function<? extends Enum<?>, Component> TO_STRING_FORMATTER = v -> Component.nullToEmpty(v.toString());
	private final boolean dropdown;
	private final Function<T, Component> formatter;

	protected EnumControllerImpl(boolean dropdown, Function<T, Component> formatter) {
		this.dropdown = dropdown;
		this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");;
	}

	@Override
	public boolean dropdown() {
		return this.dropdown;
	}

	@Override
	public Function<T, Component> formatter() {
		return this.formatter;
	}

	public static class EnumControllerBuilderImpl<T extends Enum<T>> implements EnumController.Builder<T> {
		private boolean dropdown = false;
		@SuppressWarnings("unchecked")
		private Function<T, Component> formatter = (Function<T, Component>) TO_STRING_FORMATTER;

		@Override
		public EnumController.Builder<T> dropdown(boolean dropdown) {
			this.dropdown = dropdown;
			return this;
		}

		@Override
		public EnumController.Builder<T> formatter(Function<T, Component> formatter) {
			this.formatter = formatter;
			return this;
		}

		@Override
		public EnumController<T> build() {
			return new EnumControllerImpl<>(this.dropdown, this.formatter);
		}
	}
}
