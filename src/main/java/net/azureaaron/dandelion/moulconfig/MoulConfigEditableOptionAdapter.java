package net.azureaaron.dandelion.moulconfig;

import java.lang.reflect.Type;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorButton;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.moulconfig.editor.DandelionLabelEditor;
import net.azureaaron.dandelion.systems.ButtonOption;
import net.azureaaron.dandelion.systems.LabelOption;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.controllers.IntegerController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MoulConfigEditableOptionAdapter {

	public static <T> BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createEditableOptionFactory(Option<T> option) {
		return switch (option) {
			case ButtonOption button -> createButtonOption(button);
			case LabelOption label -> createLabelOption(label);
			case Option<T> _option when option.controller() instanceof IntegerController -> (accordionId, configDefinition) -> new DandelionProcessedEditableOption<T>(option, accordionId, configDefinition) {
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
			case Option<T> _option -> (accordionId, configDefinition) -> new DandelionProcessedEditableOption<T>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return option.controller().controllerMoulConfig(option, this, configDefinition);
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createButtonOption(ButtonOption button) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Consumer<Screen>>(button, accordionId, configDefinition) {
				@Override
				public Type getType() {
					return Runnable.class;
				}

				@Override
				public Object get() {
					Runnable action = () -> {
						((ButtonOption) this.option).action().accept(MinecraftClient.getInstance().currentScreen);
					};

					return action;
				}

				@Override
				protected GuiOptionEditor createEditor() {
					ButtonOption instance = (ButtonOption) this.option;
					return new GuiOptionEditorButton(this, -1, MoulConfigPlatform.wrap(instance.prompt()), this.getConfig());
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createLabelOption(LabelOption label) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Text>(label, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new DandelionLabelEditor(this, MoulConfigPlatform.wrap(((LabelOption) this.option).label()));
				}
			};
		};
	}
}
