package net.azureaaron.dandelion.api;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.impl.ConfigCategoryImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface ConfigCategory {

	static Builder createBuilder() {
		return new ConfigCategoryImpl.ConfigCategoryBuilderImpl();
	}

	Identifier id();

	Component name();

	Component description();

	@Nullable OptionGroup rootGroup();

	List<OptionGroup> groups();

	non-sealed interface Builder extends OptionAdder {
		Builder id(Identifier id);

		Builder name(Component name);

		Builder description(Component description);

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

		Builder group(OptionGroup group);

		default Builder groupIf(boolean condition, OptionGroup group) {
			return condition ? group(group) : this;
		}

		default Builder groups(List<OptionGroup> groups) {
			for (OptionGroup group : groups) {
				group(group);
			}

			return this;
		}

		ConfigCategory build();
	}
}
