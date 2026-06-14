package net.azureaaron.dandelion.impl.yacl;

import com.mojang.blaze3d.platform.InputConstants;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/// Custom {@link KeyMapping} controller for YACL.
///
/// @author isXander (Adaptive Tooltips)
public class KeyMappingController implements Controller<InputConstants.Key> {
	private final Option<InputConstants.Key> option;

	protected KeyMappingController(Option<InputConstants.Key> option) {
		this.option = option;
	}

	@Override
	public Option<InputConstants.Key> option() {
		return this.option;
	}

	@Override
	public Component formatValue() {
		return this.option().stateManager().get().getDisplayName();
	}

	@Override
	public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
		return new KeyCodeControllerElement(this, screen, widgetDimension);
	}

	protected static class KeyCodeControllerElement extends ControllerWidget<KeyMappingController> {
		private boolean awaitingKeyPress = false;

		protected KeyCodeControllerElement(KeyMappingController control, YACLScreen screen, Dimension<Integer> dim) {
			super(control, screen, dim);
		}

		@Override
		public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
			if (!this.isMouseOver(event.x(), event.y()) || !this.isAvailable()) {
				return false;
			}

			if (this.awaitingKeyPress) {
				this.control.option().requestSet(InputConstants.Type.MOUSE.getOrCreate(event.button()));
			}

			this.awaitingKeyPress = !this.awaitingKeyPress;
			return true;
		}

		@Override
		public boolean keyPressed(KeyEvent event) {
			if (this.awaitingKeyPress) {
				this.control.option().requestSet(InputConstants.Type.KEYSYM.getOrCreate(event.key()));
				this.awaitingKeyPress = false;
				return true;
			}

			return false;
		}

		@Override
		protected Component getValueText() {
			if (this.awaitingKeyPress) {
				return Component.translatable("text.dandelion.option.keyMapping.awaitingPress").withStyle(ChatFormatting.ITALIC);
			}

			return super.getValueText();
		}

		@Override
		protected int getHoveredControlWidth() {
			return this.getUnhoveredControlWidth();
		}
	}
}
