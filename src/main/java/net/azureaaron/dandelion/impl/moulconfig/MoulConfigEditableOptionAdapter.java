package net.azureaaron.dandelion.impl.moulconfig;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorButton;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.api.ButtonOption;
import net.azureaaron.dandelion.api.LabelOption;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.controllers.IntegerController;
import net.azureaaron.dandelion.impl.moulconfig.editor.DandelionLabelEditor;
import net.minecraft.client.Minecraft;

public class MoulConfigEditableOptionAdapter {

	public static <T> BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createEditableOptionFactory(Option<T> option) {
		return switch (option) {
			case ButtonOption button -> createButtonOption(button);
			case LabelOption label -> createLabelOption(label);
			case Option<T> _option when option.controller() instanceof IntegerController -> (accordionId, configDefinition) -> new DandelionProcessedEditableOption<>(option, accordionId, configDefinition) {
				@Override
				public Object get() {
					return ((Integer) super.get()).floatValue();
				}

				@Override
				public boolean set(Object value) {
					return super.set(((Float) value).intValue());
				}

				@Override
				protected GuiOptionEditor createEditor() {
					return option.controller().controllerMoulConfig(option, this, configDefinition);
				}
			};
			case Option<T> _option -> (accordionId, configDefinition) -> new DandelionProcessedEditableOption<>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return option.controller().controllerMoulConfig(option, this, configDefinition);
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createButtonOption(ButtonOption button) {
		return (accordionId, configDefinition) -> new DandelionProcessedEditableOption<>(button, accordionId, configDefinition) {
			@Override
			public Type getType() {
				return Runnable.class;
			}

			@Override
			public Object get() {
				return (Runnable) () -> ((ButtonOption) this.option).action().accept(Minecraft.getInstance().screen);
			}

			@Override
			protected GuiOptionEditor createEditor() {
				ButtonOption instance = (ButtonOption) this.option;
				return new GuiOptionEditorButton(this, -1, MoulConfigPlatform.wrap(instance.prompt()), this.getConfig());
			}
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createLabelOption(LabelOption label) {
		return (accordionId, configDefinition) -> new DandelionProcessedEditableOption<>(label, accordionId, configDefinition) {
			@Override
			protected GuiOptionEditor createEditor() {
				return new DandelionLabelEditor(this, MoulConfigPlatform.wrap(((LabelOption) this.option).label()));
			}
		};
	}
}
