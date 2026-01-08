package net.azureaaron.dandelion.api.controllers;

import dev.isxander.yacl3.api.controller.ControllerBuilder;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigDefinition;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigOptionEditorAdapter;
import net.azureaaron.dandelion.impl.yacl.YACLControllerAdapter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public sealed interface Controller<T> permits BooleanController, ColourController, EnumController, NumberController, ItemController, StringController {

	interface Builder<T, I extends Controller<T>> {
		I build();
	}

	@ApiStatus.Experimental
	@Nullable
	default GuiOptionEditor controllerMoulConfig(Option<T> option, ProcessedOption moulConfigOption, MoulConfigDefinition configDefinition) {
		return MoulConfigOptionEditorAdapter.createMoulConfigEditor(option, moulConfigOption, configDefinition);
	}

	@ApiStatus.Experimental
	@SuppressWarnings("unchecked")
	default ControllerBuilder<T> controllerYACL(dev.isxander.yacl3.api.Option<T> yaclOption, Class<T> type) {
		return YACLControllerAdapter.toYaclControllerBuilder(yaclOption, type, this);
	}
}
