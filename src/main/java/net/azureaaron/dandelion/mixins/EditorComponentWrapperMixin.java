package net.azureaaron.dandelion.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import io.github.notenoughupdates.moulconfig.common.IFontRenderer;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;

@Mixin(ComponentEditor.EditorComponentWrapper.class)
public class EditorComponentWrapperMixin {

	@Redirect(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/azureaaron/dandelion/deps/moulconfig/common/RenderContext;drawStringCenteredScaledMaxWidth(Ljava/lang/String;Lnet/azureaaron/dandelion/deps/moulconfig/common/IFontRenderer;FFZII)V"))
	private void improveTitleSpace(RenderContext renderContext, String text, IFontRenderer fontRenderer, float x, float y, boolean shadow, int length, int colour, GuiImmediateContext context) {
		renderContext.drawStringScaledMaxWidth(text, fontRenderer, 5, 5, shadow, context.getWidth() - 10, colour);
	}

	@ModifyExpressionValue(method = { "renderDescription", "getHeight" }, at = @At(value = "INVOKE", target = "Lnet/azureaaron/dandelion/deps/moulconfig/processor/ProcessedOption;getDescription()Ljava/lang/String;"))
	private String addPadding(String original) {
		return !original.isBlank() ? "\n" + original : original;
	}
}
