package net.azureaaron.dandelion.systems;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.impl.OptionGroupImpl;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface OptionGroup {

	static Builder createBuilder() {
		return new OptionGroupImpl.OptionGroupBuilderImpl();
	}

	@Nullable
	Identifier id();

	Text name();

	List<Text> description();

	List<Text> tags();

	boolean collapsed();

	List<? extends Option<?>> options();

	non-sealed interface Builder extends OptionAdder {
		Builder id(Identifier id);

		Builder name(Text name);

		Builder description(Text... texts);

		Builder tags(Text... tags);

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
