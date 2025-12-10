package net.azureaaron.dandelion.moulconfig.editor;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.component.TextFieldComponent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;

public class DandelionNumberFieldEditor extends ComponentEditor {
	private final GuiComponent component;
	/**
	 * This is needed for reasons similar to the {@code Item} editor.
	 */
	private String lastText = "";

	public DandelionNumberFieldEditor(ProcessedOption option, float minValue, float maxValue, float defaultValue) {
		super(option);

		GetSetter<String> getSetter = new GetSetter<>() {
			@Override
			public String get() {
				return DandelionNumberFieldEditor.this.lastText;
			}

			@Override
			public void set(String newValue) {
				DandelionNumberFieldEditor.this.lastText = newValue;
				option.set(parseNumber(newValue, minValue, maxValue, defaultValue));
			}
		};
		this.lastText = toString((Float) option.get());
		this.component = this.wrapComponent(new TextFieldComponent(
				getSetter,
				80,
				() -> true,
				"",
				IMinecraft.INSTANCE.getDefaultFontRenderer()));
	}

	/**
	 * Converts a {@link Float} to a string while dropping the decimal if possible.
	 * This is done since this is also used for integer fields.
	 */
	@ApiStatus.Experimental
	protected String toString(Float floatValue) {
		int intValue = floatValue.intValue();
		float convertedBackToFloat = (float) intValue;

		if (convertedBackToFloat == floatValue.floatValue()) {
			return Integer.toString(intValue);
		} else {
			return floatValue.toString();
		}
	}

	/**
	 * Attempts to parse then {@code input} as a float, if it fails then the {@code defaultValue} is returned.
	 */
	@ApiStatus.Experimental
	protected float parseNumber(String input, float minValue, float maxValue, float defaultValue) {
		try {
			return Math.clamp(Float.parseFloat(input), minValue, maxValue);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	@NotNull
	public GuiComponent getDelegate() {
		return this.component;
	}
}
