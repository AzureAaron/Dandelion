package net.azureaaron.dandelion.impl.yacl;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;

import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionEventListener;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.OptionListener;
import net.azureaaron.dandelion.impl.ConfigManagerImpl;
import net.azureaaron.dandelion.mixins.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

public class YACLOptionAdapter {

	protected static List<dev.isxander.yacl3.api.Option<?>> toYaclOptions(ConfigManager<?> manager, List<? extends net.azureaaron.dandelion.api.Option<?>> options) {
		List<dev.isxander.yacl3.api.Option<?>> yaclOptions = new ArrayList<>();

		for (var option : options) {
			yaclOptions.add(toYaclOption(manager, option));
		}

		return yaclOptions;
	}

	@SuppressWarnings("unchecked")
	private static <T> dev.isxander.yacl3.api.Option<T> toYaclOption(ConfigManager<?> manager, net.azureaaron.dandelion.api.Option<T> option) {
		return switch (option) {
			case net.azureaaron.dandelion.api.ButtonOption button -> {
				yield (dev.isxander.yacl3.api.Option<T>) dev.isxander.yacl3.api.ButtonOption.createBuilder()
				.name(button.name())
				.description(OptionDescription.createBuilder()
						.text(button.description())
						.build())
				.text(button.prompt())
				.action((screen, _) -> button.action().accept(screen))
				.build();
			}

			case net.azureaaron.dandelion.api.KeyMappingOption keyMapping -> {
				yield (dev.isxander.yacl3.api.Option<T>) dev.isxander.yacl3.api.Option.<InputConstants.Key>createBuilder()
				.name(keyMapping.name())
				.description(OptionDescription.createBuilder()
						.text(keyMapping.description())
						.build())
				.binding(keyMapping.keyMapping().getDefaultKey(),
						() -> ((KeyMappingAccessor) keyMapping.keyMapping()).getKey(),
						newValue -> {
							keyMapping.keyMapping().setKey(newValue);
							KeyMapping.resetMapping();
						})
				.customController(KeyMappingController::new)
				.build();
			}

			case net.azureaaron.dandelion.api.LabelOption label -> {
				yield (dev.isxander.yacl3.api.Option<T>) dev.isxander.yacl3.api.LabelOption.createBuilder()
				.line(label.label())
				.build();
			}

			case net.azureaaron.dandelion.api.Option<T> _ -> {
				yield dev.isxander.yacl3.api.Option.<T>createBuilder()
				.name(option.name())
				.description(OptionDescription.createBuilder()
						.text(option.description())
						.build())
				.binding(option.binding().defaultValue(),
						option.binding()::get,
						option.binding()::set)
				.addListeners(toYaclOptionEventListener(option))
				.controller(yaclOption -> option.controller().controllerYACL(yaclOption, option.type()))
				.available(option.modifiable() && !((ConfigManagerImpl<?>) manager).isOptionPatched(option.id()))
				.flags(toYaclOptionFlags(option))
				.build();
			}
		};
	}

	protected static <T> List<OptionEventListener<T>> toYaclOptionEventListener(net.azureaaron.dandelion.api.Option<T> option) {
		List<OptionEventListener<T>> yaclOptionEventListeners = new ArrayList<>();

		for (OptionListener<T> listener : option.listeners()) {
			OptionEventListener<T> yaclListener = (_, event) -> {
				listener.onUpdate(option, convertUpdateType(event));
			};

			yaclOptionEventListeners.add(yaclListener);
		}

		return yaclOptionEventListeners;
	}

	private static OptionListener.UpdateType convertUpdateType(OptionEventListener.Event event) {
		return switch (event) {
			case OptionEventListener.Event.STATE_CHANGE -> OptionListener.UpdateType.VALUE_CHANGE;
			default -> OptionListener.UpdateType.YACL_UNSUPPORTED;
		};
	}

	protected static List<dev.isxander.yacl3.api.OptionFlag> toYaclOptionFlags(net.azureaaron.dandelion.api.Option<?> option) {
		List<dev.isxander.yacl3.api.OptionFlag> yaclOptionFlags = new ArrayList<>();

		for (var flag : option.flags()) {
			//Convert Dandelion's OptionFlag to YACL's OptionFlag
			dev.isxander.yacl3.api.OptionFlag yaclFlag = flag::accept;

			yaclOptionFlags.add(yaclFlag);
		}

		return yaclOptionFlags;
	}
}
