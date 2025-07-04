package net.azureaaron.dandelion.systems;

import net.azureaaron.dandelion.impl.LabelOptionImpl;
import net.minecraft.text.Text;

public interface LabelOption extends Option<Text> {

	static Builder createBuilder() {
		return new LabelOptionImpl.LabelOptionBuilderImpl();
	}

	Text label();

	interface Builder {
		Builder label(Text... texts);

		LabelOption build();
	}
}
