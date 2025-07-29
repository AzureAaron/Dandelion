package net.azureaaron.dandelion.moulconfig.editor;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import com.ibm.icu.text.NumberFormat;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.HorizontalAlign;
import io.github.notenoughupdates.moulconfig.gui.VerticalAlign;
import io.github.notenoughupdates.moulconfig.gui.component.AlignComponent;
import io.github.notenoughupdates.moulconfig.gui.component.RowComponent;
import io.github.notenoughupdates.moulconfig.gui.component.SliderComponent;
import io.github.notenoughupdates.moulconfig.gui.component.TextComponent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.util.Util;

public class DandelionNumberSliderEditor extends ComponentEditor {
	private static final NumberFormat FORMATTER = Util.make(NumberFormat.getInstance(Locale.CANADA), nf -> nf.setMaximumFractionDigits(2));
	protected final GuiComponent component;

	@SuppressWarnings("unchecked")
	public DandelionNumberSliderEditor(ProcessedOption option, float minValue, float maxValue, float minStep) {
		super(option);
		//if (minStep < 0) minStep = 0.01f;
		this.component = this.wrapComponent(
				new RowComponent(
						new AlignComponent(
								new TextComponent(IMinecraft.INSTANCE.getDefaultFontRenderer(),
										() -> StructuredText.of(FORMATTER.format(((Float) option.get()).doubleValue())),
										20,
										TextComponent.TextAlignment.CENTER, false, false),
								GetSetter.constant(HorizontalAlign.CENTER),
								GetSetter.constant(VerticalAlign.CENTER)
								),
						new SliderComponent((GetSetter<Float>) option.intoProperty(), minValue, maxValue, minStep, 40)
						)
				);
	}

	@Override
	@NotNull
	public GuiComponent getDelegate() {
		return this.component;
	}
}
