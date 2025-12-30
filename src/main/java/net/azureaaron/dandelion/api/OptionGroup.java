package net.azureaaron.dandelion.api;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.impl.OptionGroupImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface OptionGroup {

	static Builder createBuilder() {
		return new OptionGroupImpl.OptionGroupBuilderImpl();
	}

	@Nullable Identifier id();

	Component name();

	List<Component> description();

	List<Component> tags();

	boolean collapsed();

	List<? extends Option<?>> options();

	non-sealed interface Builder extends OptionAdder {
		Builder id(Identifier id);

		Builder name(Component name);

		Builder description(Component... texts);

		Builder tags(Component... tags);

		Builder collapsed(boolean collapsed);

		@Override
		Builder option(Option<?> option);

		@Override
		default Builder optionIf(boolean condition, Option<?> option) {
			OptionAdder.super.optionIf(condition, option);
			return this;
		}

		@Override
		default Builder options(List<? extends Option<?>> options) {
			OptionAdder.super.options(options);
			return this;
		}

		OptionGroup build();
	}
}
