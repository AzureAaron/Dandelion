package net.azureaaron.dandelion.systems;

import java.util.List;

public sealed interface OptionAdder permits ConfigCategory.Builder, OptionGroup.Builder {

	OptionAdder option(Option<?> option);

	default OptionAdder optionIf(boolean condition, Option<?> option) {
		return condition ? option(option) : this;
	}

	default OptionAdder options(List<? extends Option<?>> options) {
		for (Option<?> option : options) {
			option(option);
		}

		return this;
	}
}
