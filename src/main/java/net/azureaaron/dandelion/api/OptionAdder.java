package net.azureaaron.dandelion.api;

import java.util.List;
import java.util.function.Supplier;

public sealed interface OptionAdder permits ConfigCategory.Builder, OptionGroup.Builder {

	OptionAdder option(Option<?> option);

	default OptionAdder optionIf(boolean condition, Option<?> option) {
		return condition ? option(option) : this;
	}

	/// Allows for an option to be created only when a condition is met.
	///
	/// Useful when working with optionally registered {@link net.minecraft.client.KeyMapping KeyMappings}.
	default OptionAdder optionIf(boolean condition, Supplier<Option<?>> option) {
		return condition ? option(option.get()) : this;
	}

	default OptionAdder options(List<? extends Option<?>> options) {
		for (Option<?> option : options) {
			option(option);
		}

		return this;
	}
}
