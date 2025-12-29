package net.azureaaron.dandelion.systems;

import java.util.function.Consumer;

import net.azureaaron.dandelion.impl.ButtonOptionImpl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface ButtonOption extends Option<Consumer<Screen>> {

	static Builder createBuilder() {
		return new ButtonOptionImpl.ButtonOptionBuilderImpl();
	}

	Component prompt();

	Consumer<Screen> action();

	//TODO consider allowing to "gray out" the option
	interface Builder {
		Builder id(Identifier id);

		Builder name(Component name);

		Builder description(Component... texts);

		Builder tags(Component... tags);

		Builder prompt(Component prompt);

		Builder action(Consumer<Screen> action);

		ButtonOption build();
	}
}
