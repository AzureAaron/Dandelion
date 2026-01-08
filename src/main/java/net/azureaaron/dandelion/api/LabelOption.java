package net.azureaaron.dandelion.api;

import net.azureaaron.dandelion.impl.LabelOptionImpl;
import net.minecraft.network.chat.Component;

public interface LabelOption extends Option<Component> {

	static Builder createBuilder() {
		return new LabelOptionImpl.LabelOptionBuilderImpl();
	}

	Component label();

	interface Builder {
		Builder label(Component... texts);

		LabelOption build();
	}
}
