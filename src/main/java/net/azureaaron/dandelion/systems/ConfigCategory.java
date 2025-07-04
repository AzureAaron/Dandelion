package net.azureaaron.dandelion.systems;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.impl.ConfigCategoryImpl;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface ConfigCategory {

	static Builder createBuilder() {
		return new ConfigCategoryImpl.ConfigCategoryBuilderImpl();
	}

	Identifier id();

	Text name();

	Text description();

	@Nullable
	OptionGroup rootGroup();

	List<OptionGroup> groups();

	non-sealed interface Builder extends OptionAdder {
		Builder id(Identifier id);

		Builder name(Text name);

		Builder description(Text description);

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
