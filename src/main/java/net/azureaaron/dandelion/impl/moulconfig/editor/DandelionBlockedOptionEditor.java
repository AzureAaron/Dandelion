package net.azureaaron.dandelion.impl.moulconfig.editor;

import io.github.notenoughupdates.moulconfig.common.IFontRenderer;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.Dandelion;
import net.minecraft.network.chat.Component;

public class DandelionBlockedOptionEditor extends GuiOptionEditor {
	/// Special thanks to nea for this asset.
	private static final MyResourceLocation BLOCKED_OVERLAY = MoulConfigPlatform.wrap(Dandelion.id("textures/gui/option_blocked.png"));
	private final GuiOptionEditor base;

	public DandelionBlockedOptionEditor(GuiOptionEditor base) {
		super(base.getOption());
		this.base = base;
	}

	@Override
	public void render(RenderContext context, int x, int y, int width) {
		this.base.render(context, x, y, width);

		// Darken the original option
		context.drawColoredRect(x, y, x + width, y + this.getHeight(), 0x80000000);

		// Draw blocked icon
		float iconWidth = this.getHeight() * 96f / 64;
		context.drawTexturedRect(BLOCKED_OVERLAY, x, y, iconWidth, this.getHeight());

		// Clarifying Text
		StructuredText text = MoulConfigPlatform.wrap(Component.translatable("text.dandelion.option.unavailable"));
		IFontRenderer font = IMinecraft.INSTANCE.getDefaultFontRenderer();
		context.drawStringScaledMaxWidth(text, font, (int) (x + iconWidth), (int) (y + this.getHeight() / 2f  - font.getHeight() / 2f), true, (int) (width - iconWidth), 0xFFFF4444);
	}

	@Override
	public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY, MouseEvent mouseEvent) {
		return false;
	}

	@Override
	public boolean keyboardInput(KeyboardEvent event) {
		return false;
	}
}
