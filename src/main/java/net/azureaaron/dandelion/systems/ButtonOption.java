package net.azureaaron.dandelion.systems;

import java.util.function.Consumer;

import net.azureaaron.dandelion.impl.ButtonOptionImpl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface ButtonOption extends Option<Consumer<Screen>> {

	static Builder createBuilder() {
		return new ButtonOptionImpl.ButtonOptionBuilderImpl();
	}

	Text prompt();

	Consumer<Screen> action();

	//TODO consider allowing to "gray out" the option
	interface Builder {
		Builder id(Identifier id);

		Builder name(Text name);

		Builder description(Text... texts);

		Builder tags(Text... tags);

		Builder prompt(Text prompt);

		Builder action(Consumer<Screen> action);

		ButtonOption build();
	}
}
