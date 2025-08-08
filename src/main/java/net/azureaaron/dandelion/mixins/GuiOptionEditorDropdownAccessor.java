package net.azureaaron.dandelion.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorDropdown;

@Mixin(GuiOptionEditorDropdown.class)
public interface GuiOptionEditorDropdownAccessor {

	@Accessor
	void setConstants(Enum<?>[] constants);
}
