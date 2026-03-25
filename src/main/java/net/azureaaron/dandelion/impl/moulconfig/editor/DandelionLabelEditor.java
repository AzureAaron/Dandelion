package net.azureaaron.dandelion.impl.moulconfig.editor;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.HorizontalAlign;
import io.github.notenoughupdates.moulconfig.gui.VerticalAlign;
import io.github.notenoughupdates.moulconfig.gui.component.AlignComponent;
import io.github.notenoughupdates.moulconfig.gui.component.TextComponent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;

public class DandelionLabelEditor extends ComponentEditor {
	private final GuiComponent component;

	public DandelionLabelEditor(ProcessedOption option, StructuredText label) {
		super(option);
		this.component = new AlignComponent(
			new TextComponent(
				IMinecraft.INSTANCE.getDefaultFontRenderer(),
				() -> label,
				250,
				TextComponent.TextAlignment.CENTER,
				true,
				true
			),
			GetSetter.constant(HorizontalAlign.CENTER),
			GetSetter.constant(VerticalAlign.CENTER)
		);
	}

	@Override
	public int getHeight() {
		return Math.max(20, component.getHeight());
	}

	@Override
	public GuiComponent getDelegate() {
		return this.component;
	}
}
