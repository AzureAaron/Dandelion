package net.azureaaron.dandelion.moulconfig.editor;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.GuiTextures;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.gui.component.ColorSelectComponent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

//GuiOptionEditorColour
public class DandelionColourEditor extends ComponentEditor {
	private static final MyResourceLocation ALPHA_BUTTON = new MyResourceLocation("dandelion", "textures/gui/button_alpha.png");
	private static final MyResourceLocation ALPHA_BUTTON_OVERLAY = new MyResourceLocation("dandelion", "textures/gui/button_alpha_overlay.png");
	private final GuiComponent component;

	@SuppressWarnings("unchecked")
	public DandelionColourEditor(ProcessedOption option, boolean hasAlpha) {
		super(option);
		this.component = this.wrapComponent(new ColourOverlayComponent(this, (GetSetter<Color>) option.intoProperty(), hasAlpha));
	}

	@Override
	@NotNull
	public GuiComponent getDelegate() {
		return this.component;
	}

	protected static class ColourOverlayComponent extends GuiComponent {
		private final DandelionColourEditor editor;
		private final GetSetter<Color> getSetter;
		private final boolean hasAlpha;

		protected ColourOverlayComponent(DandelionColourEditor editor, GetSetter<Color> getSetter, boolean hasAlpha) {
			this.editor = editor;
			this.getSetter = getSetter;
			this.hasAlpha = hasAlpha;
		}

		@Override
		public int getWidth() {
			return 48;
		}

		@Override
		public int getHeight() {
			return 16;
		}

		@Override
		public void render(@NotNull GuiImmediateContext context) {
			int colour = this.getSetter.get().getRGB();
			RenderContext renderContext = context.getRenderContext();

			if (this.hasAlpha) {
				//Draw alpha button
				renderContext.drawComplexTexture(ALPHA_BUTTON, 0, 0, context.getWidth(), context.getHeight(), block -> {
					block.color(Colors.WHITE);
				});

				//Draw tinted overlay
				renderContext.drawComplexTexture(ALPHA_BUTTON_OVERLAY, 0, 0, context.getWidth(), context.getHeight(), block -> {
					block.color(colour);
				});
			} else {
				renderContext.drawComplexTexture(GuiTextures.BUTTON_WHITE, 0, 0, context.getWidth(), context.getHeight(), block -> {
					block.color(ColorHelper.fullAlpha(colour));
				});
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean mouseEvent(@NotNull MouseEvent mouseEvent, @NotNull GuiImmediateContext context) {
			if (mouseEvent instanceof MouseEvent.Click click && click.getMouseState() && click.getMouseButton() == 0 && context.isHovered()) {
				ColorSelectComponent component = new ColorSelectComponent(0, 0, this.toChromaColour().toLegacyString(), this::fromChromaColourString, this.editor::closeOverlay, this.hasAlpha, true);
				//Clamp the Y so that the colour picker can't go off screen
				int scaledHeight = context.getRenderContext().getMinecraft().getScaledHeight();
				int clampedY;

				if (context.getAbsoluteMouseY() + component.getHeight() > scaledHeight) {
					clampedY = scaledHeight - component.getHeight();
				} else {
					clampedY = context.getAbsoluteMouseY();
				}

				this.editor.openOverlay(component, context.getAbsoluteMouseX(), clampedY);
				return true;
			}

			return false;
		}

		private ChromaColour toChromaColour() {
			Color colour = this.getSetter.get();
			return ChromaColour.fromStaticRGB(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
		}

		private void fromChromaColourString(String colourString) {
			@SuppressWarnings("deprecation")
			Color colour = new Color(ChromaColour.specialToSimpleRGB(colourString), true);
			this.getSetter.set(colour);
		}
	}
}
