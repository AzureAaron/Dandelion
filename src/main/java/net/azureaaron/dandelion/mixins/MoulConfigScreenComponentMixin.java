package net.azureaaron.dandelion.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import io.github.notenoughupdates.moulconfig.gui.CloseEventListener;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Mixin(MoulConfigScreenComponent.class)
public abstract class MoulConfigScreenComponentMixin extends Screen {
	@Shadow
	@Final
	GuiContext guiContext;

	protected MoulConfigScreenComponentMixin(Text title) {
		super(title);
	}

	@Overwrite
	@Override
	public void close() {
		if (this.guiContext.onBeforeClose() == CloseEventListener.CloseAction.NO_OBJECTIONS_TO_CLOSE) {
			this.guiContext.requestClose();
		}
	}
}
