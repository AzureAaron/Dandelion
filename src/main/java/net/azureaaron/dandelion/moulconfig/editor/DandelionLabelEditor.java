package net.azureaaron.dandelion.moulconfig.editor;

import org.jetbrains.annotations.NotNull;

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

	public DandelionLabelEditor(ProcessedOption option, String label) {
		super(option);
		this.component = new AlignComponent(
				new TextComponent(
						label,
						100,
						TextComponent.TextAlignment.CENTER
						),
				GetSetter.constant(HorizontalAlign.CENTER),
				GetSetter.constant(VerticalAlign.CENTER)
				);
	}

	@Override
	public int getHeight() {
		//Same height as an accordion editor
		return 20;
	}

	@Override
	@NotNull
	public GuiComponent getDelegate() {
		return this.component;
	}
}
