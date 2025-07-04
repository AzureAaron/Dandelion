package net.azureaaron.dandelion.moulconfig;

import org.lwjgl.glfw.GLFW;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.GuiElement;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Re-implementation of MoulConfig's {@code GuiElementWrapper} that provides support for parent screens and a save action.
 */
public class DandelionGuiElementWrapper extends Screen {
	private final GuiElement element;
	private final Screen parent;
	private final Runnable saveAction;

	protected DandelionGuiElementWrapper(GuiElement element, Text title, Screen parent, Runnable saveAction) {
		super(title);
		this.parent = parent;
		this.saveAction = saveAction;
		this.element = element;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		this.element.render();
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		this.element.keyboardInput(new KeyboardEvent.CharTyped(chr));
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.element.keyboardInput(new KeyboardEvent.KeyPressed(keyCode, true))) {
			return true;
		} else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			this.close();
			return true;
		}

		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return this.element.keyboardInput(new KeyboardEvent.KeyPressed(keyCode, false));
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		int xInt = (int) mouseX;
		int yInt = (int) mouseY;

		MouseEvent event = new MouseEvent.Move((float) mouseX - IMinecraft.instance.getMouseX(), (float) mouseY - IMinecraft.instance.getMouseY());
		this.element.mouseInput(xInt, yInt, event);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.element.mouseInput((int) mouseX, (int) mouseY, new MouseEvent.Click(button, true));
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.element.mouseInput((int) mouseX, (int) mouseY, new MouseEvent.Click(button, false));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		return this.element.mouseInput((int) mouseX, (int) mouseY, new MouseEvent.Scroll((float) verticalAmount));
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return true;
	}

	@Override
	public void close() {
		this.saveAction.run();
		this.client.setScreen(this.parent);
	}
}
