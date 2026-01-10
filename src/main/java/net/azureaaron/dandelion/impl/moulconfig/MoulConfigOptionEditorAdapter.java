package net.azureaaron.dandelion.impl.moulconfig;

import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorBoolean;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorDropdown;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorText;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.controllers.BooleanController;
import net.azureaaron.dandelion.api.controllers.ColourController;
import net.azureaaron.dandelion.api.controllers.EnumController;
import net.azureaaron.dandelion.api.controllers.FloatController;
import net.azureaaron.dandelion.api.controllers.IntegerController;
import net.azureaaron.dandelion.api.controllers.ItemController;
import net.azureaaron.dandelion.api.controllers.StringController;
import net.azureaaron.dandelion.impl.moulconfig.editor.DandelionColourEditor;
import net.azureaaron.dandelion.impl.moulconfig.editor.DandelionItemEditor;
import net.azureaaron.dandelion.impl.moulconfig.editor.DandelionNumberFieldEditor;
import net.azureaaron.dandelion.impl.moulconfig.editor.DandelionNumberSliderEditor;
import net.azureaaron.dandelion.mixins.GuiOptionEditorDropdownAccessor;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class MoulConfigOptionEditorAdapter {
	public static <T> GuiOptionEditor createMoulConfigEditor(Option<T> option, ProcessedOption moulConfigOption, MoulConfigDefinition configDefinition) {
		return switch (option.controller()) {
			case BooleanController booleanController -> new GuiOptionEditorBoolean(moulConfigOption, -1, configDefinition);
			case ColourController colourController -> new DandelionColourEditor(moulConfigOption, colourController.hasAlpha());
			case @SuppressWarnings("rawtypes") EnumController enumController -> {
				T[] constants = option.type().getEnumConstants();
				@SuppressWarnings("unchecked")
				String[] displayValues = (String[]) Arrays.stream(constants)
					.map(enumController.formatter())
					.map(t -> ((Component) t).getString())
					.toArray(String[]::new);
				GuiOptionEditorDropdown editor = new GuiOptionEditorDropdown(moulConfigOption, displayValues, true);
				((GuiOptionEditorDropdownAccessor) editor).setConstants((Enum<?>[]) constants);
				yield editor;
			}
			case FloatController floatController when !floatController.slider() -> new DandelionNumberFieldEditor(moulConfigOption, floatController.min(), floatController.max(), (Float) option.binding().defaultValue());
			case FloatController floatController when floatController.slider() -> new DandelionNumberSliderEditor(moulConfigOption, floatController.min(), floatController.max(), floatController.step());
			case IntegerController integerController when !integerController.slider() -> new DandelionNumberFieldEditor(moulConfigOption, integerController.min(), integerController.max(), (Integer) option.binding().defaultValue());
			case IntegerController integerController when integerController.slider() -> new DandelionNumberSliderEditor(moulConfigOption, integerController.min(), integerController.max(), integerController.step());
			case ItemController itemController -> new DandelionItemEditor(moulConfigOption);
			case StringController stringController -> new GuiOptionEditorText(moulConfigOption);
			default -> throw new UnsupportedOperationException(String.format("The controller %s is not supported by the MoulConfig backend.", option.controller().getClass().getName()));
		};
	}
}
