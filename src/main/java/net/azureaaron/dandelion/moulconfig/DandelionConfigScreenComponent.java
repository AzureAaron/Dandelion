package net.azureaaron.dandelion.moulconfig;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Extension of MoulConfig's {@code MoulConfigScreenComponent} that provides support for a save action.
 */
public class DandelionConfigScreenComponent extends MoulConfigScreenComponent {
	private final Runnable saveAction;

	protected DandelionConfigScreenComponent(Text title, GuiContext context, Screen parent, Runnable saveAction) {
		super(title, context, parent);
		this.saveAction = saveAction;
	}

	@Override
	public void close() {
		this.saveAction.run();
		this.client.setScreen(this.getPreviousScreen());
	}
}
