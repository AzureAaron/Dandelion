package net.azureaaron.dandelion.impl.controllers;

import java.util.Objects;
import java.util.function.Function;

import net.azureaaron.dandelion.systems.controllers.EnumController;
import net.minecraft.text.Text;

public class EnumControllerImpl<T extends Enum<T>> implements EnumController<T> {
	private static final Function<? extends Enum<?>, Text> TO_STRING_FORMATTER = v -> Text.of(v.toString());
	private final boolean dropdown;
	private final Function<T, Text> formatter;

	protected EnumControllerImpl(boolean dropdown, Function<T, Text> formatter) {
		this.dropdown = dropdown;
		this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");;
	}

	@Override
	public boolean dropdown() {
		return this.dropdown;
	}

	@Override
	public Function<T, Text> formatter() {
		return this.formatter;
	}

	public static class EnumControllerBuilderImpl<T extends Enum<T>> implements EnumController.Builder<T> {
		private boolean dropdown = false;
		@SuppressWarnings("unchecked")
		private Function<T, Text> formatter = (Function<T, Text>) TO_STRING_FORMATTER;

		@Override
		public Builder<T> dropdown(boolean dropdown) {
			this.dropdown = dropdown;
			return this;
		}

		@Override
		public Builder<T> formatter(Function<T, Text> formatter) {
			this.formatter = formatter;
			return this;
		}

		@Override
		public EnumController<T> build() {
			return new EnumControllerImpl<>(this.dropdown, this.formatter);
		}
	}
}
