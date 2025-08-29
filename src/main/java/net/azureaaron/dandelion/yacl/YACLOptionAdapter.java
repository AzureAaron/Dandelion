package net.azureaaron.dandelion.yacl;

import java.util.ArrayList;
import java.util.List;

import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionEventListener;
import net.azureaaron.dandelion.systems.OptionListener;

public class YACLOptionAdapter {

	protected static List<dev.isxander.yacl3.api.Option<?>> toYaclOptions(List<? extends net.azureaaron.dandelion.systems.Option<?>> options) {
		List<dev.isxander.yacl3.api.Option<?>> yaclOptions = new ArrayList<>();

		for (var option : options) {
			yaclOptions.add(toYaclOption(option));
		}

		return yaclOptions;
	}

	@SuppressWarnings("unchecked")
	private static <T> dev.isxander.yacl3.api.Option<T> toYaclOption(net.azureaaron.dandelion.systems.Option<T> option) {
		return switch (option) {
			case net.azureaaron.dandelion.systems.ButtonOption button -> {
				yield (dev.isxander.yacl3.api.Option<T>) dev.isxander.yacl3.api.ButtonOption.createBuilder()
				.name(button.name())
				.description(OptionDescription.createBuilder()
						.text(button.description())
						.build())
				.text(button.prompt())
				.action((screen, opt) -> button.action().accept(screen))
				.build();
			}

			case net.azureaaron.dandelion.systems.LabelOption label -> {
				yield (dev.isxander.yacl3.api.Option<T>) dev.isxander.yacl3.api.LabelOption.createBuilder()
				.line(label.label())
				.build();
			}

			case net.azureaaron.dandelion.systems.Option<T> _option -> {
				yield dev.isxander.yacl3.api.Option.<T>createBuilder()
				.name(option.name())
				.description(OptionDescription.createBuilder()
						.text(option.description())
						.build())
				.binding(option.binding().defaultValue(),
						option.binding()::get,
						option.binding()::set)
				.addListeners(toYaclOptionEventListener(option))
				.controller(yaclOption -> YACLControllerAdapter.toYaclControllerBuilder(yaclOption, option.type(), option.controller()))
				.available(option.modifiable())
				.flags(toYaclOptionFlags(option))
				.build();
			}
		};
	}

	protected static <T> List<OptionEventListener<T>> toYaclOptionEventListener(net.azureaaron.dandelion.systems.Option<T> option) {
		List<OptionEventListener<T>> yaclOptionEventListeners = new ArrayList<>();

		for (OptionListener<T> listener : option.listeners()) {
			OptionEventListener<T> yaclListener = (yaclOption, event) -> {
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

	protected static List<dev.isxander.yacl3.api.OptionFlag> toYaclOptionFlags(net.azureaaron.dandelion.systems.Option<?> option) {
		List<dev.isxander.yacl3.api.OptionFlag> yaclOptionFlags = new ArrayList<>();

		for (var flag : option.flags()) {
			//Convert Dandelion's OptionFlag to YACL's OptionFlag
			dev.isxander.yacl3.api.OptionFlag yaclFlag = flag::accept;

			yaclOptionFlags.add(yaclFlag);
		}

		return yaclOptionFlags;
	}
}
