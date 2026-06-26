package net.azureaaron.dandelion.impl.moulconfig.editor;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import io.github.notenoughupdates.moulconfig.GuiTextures;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.KeyboardConstants;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class DandelionKeyMappingEditor extends ComponentEditor {
	private final GuiComponent component;

	public DandelionKeyMappingEditor(ProcessedOption option, KeyMapping keyMapping) {
		super(option);
		this.component = this.wrapComponent(new KeyMappingComponent(keyMapping));
	}

	@Override
	public GuiComponent getDelegate() {
		return this.component;
	}

	protected static class KeyMappingComponent extends GuiComponent {
		private final KeyMapping keyMapping;
		private boolean editingKeycode = false;

		protected KeyMappingComponent(KeyMapping keyMapping) {
			this.keyMapping = keyMapping;
		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public int getHeight() {
			return 30;
		}

		@Override
		public void render(GuiImmediateContext context) {
			RenderContext renderContext = context.getRenderContext();
			int width = this.getWidth();
			int height = this.getHeight();

			renderContext.drawTexturedRect(GuiTextures.BUTTON, width / 6 - 24, height - 7 - 14, 48, 16);

			StructuredText keyName = MoulConfigPlatform.wrap(this.keyMapping.getTranslatedKeyMessage());
			StructuredText text = this.editingKeycode ? StructuredText.of("> ").append(keyName).append(" <") : keyName;
			renderContext.drawStringCenteredScaledMaxWidth(text, IMinecraft.INSTANCE.getDefaultFontRenderer(), width / 6, height - 7 - 6, false, 38, 0xFF303030);

			int resetX = width / 6 - 24 + 48 + 3;
			int resetY = height - 7 - 14 + 3;
			renderContext.drawTexturedRect(GuiTextures.RESET, resetX, resetY, 10, 11);

			int mouseX = context.getMouseX();
			int mouseY = context.getMouseY();

			if (mouseX >= resetX && mouseX < resetX + 10 && mouseY >= resetY && mouseY < resetY + 11) {
				List<StructuredText> tooltip = List.of(MoulConfigPlatform.wrap(Component.translatable("text.dandelion.option.keyMapping.reset")).red());
				renderContext.scheduleDrawTooltip(mouseX, mouseY, tooltip);
			}
		}

		@Override
		public boolean mouseEvent(MouseEvent mouseEvent, GuiImmediateContext context) {
			if (mouseEvent instanceof MouseEvent.Click click) {
				if (click.getMouseState() && click.getMouseButton() != -1 && this.editingKeycode) {
					this.editingKeycode = false;
					int mouseButton = click.getMouseButton();
					this.setKey(InputConstants.Type.MOUSE.getOrCreate(mouseButton));
					return true;
				}

				if (click.getMouseState() && click.getMouseButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					int height = getHeight();
					int width = getHeight();
					int mouseX = context.getMouseX();
					int mouseY = context.getMouseY();

					if (mouseX > width / 6 - 24 && mouseX < width / 6 + 16 && mouseY > height - 7 - 14 && mouseY < height - 7 + 2) {
						this.editingKeycode = true;
						return true;
					}

					if (mouseX > width / 6 - 24 + 48 - 3 && mouseX < width / 6 - 24 + 48 + 13 - 5 && mouseY > height - 7 - 14 + 3 && mouseY < height - 7 - 14 + 3 + 11) {
						this.setKey(this.keyMapping.getDefaultKey());
						return true;
					}
				}
			}

			return false;
		}

		@Override
		public boolean keyboardEvent(KeyboardEvent keyboardEvent, GuiImmediateContext context) {
			if (keyboardEvent instanceof KeyboardEvent.KeyPressed keyPressed) {
				if (this.editingKeycode) {
					if (keyPressed.getPressed()) {
						return true;
					}

					this.editingKeycode = false;
					int keycode = keyPressed.getKeycode();

					if (keycode == KeyboardConstants.INSTANCE.getEscape() || keycode == 0) {
						keycode = KeyboardConstants.INSTANCE.getNone();
					}

					this.setKey(InputConstants.Type.KEYSYM.getOrCreate(keycode));
					return true;
				} else {
					return false;
				}
			}

			return this.editingKeycode;
		}

		/// Updates the {@link #keyMapping} to be the {@code key} and refreshes all key mappings.
		private void setKey(InputConstants.Key key) {
			this.keyMapping.setKey(key);
			KeyMapping.resetMapping();
		}
	}
}
