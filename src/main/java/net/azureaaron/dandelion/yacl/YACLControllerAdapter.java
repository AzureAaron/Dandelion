package net.azureaaron.dandelion.yacl;

import java.awt.Color;
import java.util.function.Function;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumDropdownControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.ItemControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import net.azureaaron.dandelion.systems.controllers.BooleanController;
import net.azureaaron.dandelion.systems.controllers.BooleanController.BooleanStyle;
import net.azureaaron.dandelion.systems.controllers.ColourController;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.azureaaron.dandelion.systems.controllers.EnumController;
import net.azureaaron.dandelion.systems.controllers.FloatController;
import net.azureaaron.dandelion.systems.controllers.IntegerController;
import net.azureaaron.dandelion.systems.controllers.ItemController;
import net.azureaaron.dandelion.systems.controllers.NumberController;
import net.azureaaron.dandelion.systems.controllers.StringController;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

public class YACLControllerAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> ControllerBuilder toYaclControllerBuilder(dev.isxander.yacl3.api.Option<T> yaclOption, Class<T> type, Controller<T> controller) {
		switch (controller) {
			case BooleanController booleanController -> {
				BooleanControllerBuilder yaclController = BooleanControllerBuilder.create((Option<Boolean>) yaclOption)
						.coloured(booleanController.coloured());

				yaclController = switch (booleanController.style()) {
					case BooleanStyle.ON_OFF -> yaclController.onOffFormatter();
					case BooleanStyle.YES_NO -> yaclController.yesNoFormatter();
					case BooleanStyle.TRUE_FALSE -> yaclController.trueFalseFormatter();
				};

				return yaclController;
			}

			case ColourController colourController -> {
				return ColorControllerBuilder.create((Option<Color>) yaclOption).allowAlpha(colourController.hasAlpha());
			}

			case EnumController<?> enumController -> {
				ValueFormatter<T> yaclFormatter = ((Function<T, Text>) enumController.formatter())::apply;

				if (!enumController.dropdown()) {
					return EnumControllerBuilder.create((Option<Enum>) yaclOption).enumClass(type).formatValue(yaclFormatter);
				} else {
					return EnumDropdownControllerBuilder.create((Option<Enum>) yaclOption).formatValue(yaclFormatter);
				}
			}

			case NumberController<?> numberController -> {
				switch (numberController) {
					case IntegerController i -> {
						if (i.slider()) {
							return IntegerSliderControllerBuilder.create((Option<Integer>) yaclOption).range(i.min(), i.max()).step(i.step());
						} else {
							return IntegerFieldControllerBuilder.create((Option<Integer>) yaclOption).range(i.min(), i.max());
						}
					}
					case FloatController f -> {
						if (f.slider()) {
							return FloatSliderControllerBuilder.create((Option<Float>) yaclOption).range(f.min(), f.max()).step(f.step());
						} else {
							return FloatFieldControllerBuilder.create((Option<Float>) yaclOption).range(f.min(), f.max());
						}
					}
				}
			}

			case ItemController itemController -> {
				return ItemControllerBuilder.create((Option<Item>) yaclOption);
			}

			case StringController stringController -> {
				return StringControllerBuilder.create((Option<String>) yaclOption);
			}
		}
	}
}
