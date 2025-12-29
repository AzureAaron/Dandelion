package net.azureaaron.dandelion.moulconfig;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorBoolean;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorButton;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorDropdown;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.mixins.GuiOptionEditorDropdownAccessor;
import net.azureaaron.dandelion.moulconfig.editor.DandelionColourEditor;
import net.azureaaron.dandelion.moulconfig.editor.DandelionItemEditor;
import net.azureaaron.dandelion.moulconfig.editor.DandelionLabelEditor;
import net.azureaaron.dandelion.moulconfig.editor.DandelionNumberFieldEditor;
import net.azureaaron.dandelion.moulconfig.editor.DandelionNumberSliderEditor;
import net.azureaaron.dandelion.systems.ButtonOption;
import net.azureaaron.dandelion.systems.LabelOption;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.controllers.BooleanController;
import net.azureaaron.dandelion.systems.controllers.ColourController;
import net.azureaaron.dandelion.systems.controllers.EnumController;
import net.azureaaron.dandelion.systems.controllers.FloatController;
import net.azureaaron.dandelion.systems.controllers.IntegerController;
import net.azureaaron.dandelion.systems.controllers.ItemController;
import net.azureaaron.dandelion.systems.controllers.StringController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class MoulConfigEditableOptionAdapter {

	@SuppressWarnings("unchecked")
	public static <T> BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createEditableOptionFactory(Option<T> option) {
		return switch (option) {
			case ButtonOption button -> createButtonOption(button);
			case LabelOption label -> createLabelOption(label);
			case Option<T> _option -> switch (option.controller()) {
				case BooleanController booleanController -> createBooleanOption((Option<Boolean>) option);
				case ColourController colourController -> createColourOption((Option<Color>) option);
				case EnumController<?> enumController -> createEnumOption(Option.class.cast(option));
				case FloatController floatController when !floatController.slider() -> createFloatFieldOption((Option<Float>) option);
				case FloatController floatController when floatController.slider() -> createFloatSliderOption((Option<Float>) option);
				case IntegerController integerController when !integerController.slider() -> createIntegerFieldOption((Option<Integer>) option);
				case IntegerController integerController when integerController.slider() -> createIntegerSliderOption((Option<Integer>) option);
				case ItemController itemController -> createItemOption((Option<Item>) option);
				case StringController stringController -> createStringOption((Option<String>) option);
				default -> throw new UnsupportedOperationException(String.format("The controller %s is not supported by the MoulConfig backend.", option.controller().getClass().getName()));
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
						((ButtonOption) this.option).action().accept(Minecraft.getInstance().screen);
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
			return new DandelionProcessedEditableOption<Component>(label, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new DandelionLabelEditor(this, MoulConfigPlatform.wrap(((LabelOption) this.option).label()));
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createBooleanOption(Option<Boolean> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Boolean>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new GuiOptionEditorBoolean(this, -1, configDefinition);
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createColourOption(Option<Color> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Color>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new DandelionColourEditor(this, ((ColourController) this.option.controller()).hasAlpha());
				}
			};
		};
	}

	private static <T extends Enum<T>> BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createEnumOption(Option<T> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<T>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					T[] constants = this.option.type().getEnumConstants();
					String[] displayValues = Arrays.stream(constants)
							.map(((EnumController<T>) this.option.controller()).formatter()::apply)
							.map(Component::getString)
							.toArray(String[]::new);
					GuiOptionEditorDropdown editor = new GuiOptionEditorDropdown(this, displayValues, true);
					((GuiOptionEditorDropdownAccessor) editor).setConstants(constants);

					return editor;
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createFloatFieldOption(Option<Float> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Float>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					FloatController controller = (FloatController) this.option.controller();
					return new DandelionNumberFieldEditor(this, controller.min(), controller.max(), this.option.binding().defaultValue());
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createFloatSliderOption(Option<Float> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Float>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					FloatController controller = (FloatController) this.option.controller();
					return new DandelionNumberSliderEditor(this, controller.min(), controller.max(), controller.step());
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createIntegerFieldOption(Option<Integer> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Integer>(option, accordionId, configDefinition) {
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
					IntegerController controller = (IntegerController) this.option.controller();
					return new DandelionNumberFieldEditor(this, controller.min(), controller.max(), this.option.binding().defaultValue());
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createIntegerSliderOption(Option<Integer> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Integer>(option, accordionId, configDefinition) {
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
					IntegerController controller = (IntegerController) this.option.controller();
					return new DandelionNumberSliderEditor(this, controller.min(), controller.max(), controller.step());
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createItemOption(Option<Item> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<Item>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new DandelionItemEditor(this);
				}
			};
		};
	}

	private static BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>> createStringOption(Option<String> option) {
		return (accordionId, configDefinition) -> {
			return new DandelionProcessedEditableOption<String>(option, accordionId, configDefinition) {
				@Override
				protected GuiOptionEditor createEditor() {
					return new GuiOptionEditorText(this);
				}
			};
		};
	}
}
