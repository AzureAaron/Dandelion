package net.azureaaron.dandelion.impl.yacl;

import java.util.ArrayList;
import java.util.List;

import dev.isxander.yacl3.api.OptionDescription;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ListOption;
import net.azureaaron.dandelion.api.OptionGroup;
import net.azureaaron.dandelion.impl.ConfigManagerImpl;

public class YACLOptionGroupAdapter {

	protected static List<dev.isxander.yacl3.api.OptionGroup> toYaclOptionGroups(ConfigManager<?> manager, List<net.azureaaron.dandelion.api.OptionGroup> groups) {
		List<dev.isxander.yacl3.api.OptionGroup> yaclGroups = new ArrayList<>();

		for (var group : groups) {
			yaclGroups.add(toYaclOptionGroup(manager, group));
		}

		return yaclGroups;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static dev.isxander.yacl3.api.OptionGroup toYaclOptionGroup(ConfigManager<?> manager, net.azureaaron.dandelion.api.OptionGroup group) {
		return switch (group) {
			case ListOption listOption -> dev.isxander.yacl3.api.ListOption.createBuilder()
					.name(listOption.name())
					.description(OptionDescription.createBuilder()
							.text(listOption.description())
							.build())
					.binding((List<Object>) listOption.binding().defaultValue(),
							() -> (List<Object>) listOption.binding().get(),
							listOption.binding()::set)
					.controller(yaclOption -> listOption.entryController().controllerYACL(yaclOption, listOption.entryType()))
					.initial(listOption.initialValue())
					.collapsed(listOption.collapsed())
					.available(listOption.modifiable() && !((ConfigManagerImpl<?>) manager).isOptionPatched(listOption.id()))
					.flags(YACLOptionAdapter.toYaclOptionFlags(listOption))
					.addListeners(YACLOptionAdapter.toYaclOptionEventListener(listOption))
					.build();

			case OptionGroup optionGroup -> {
				var yaclGroup = dev.isxander.yacl3.api.OptionGroup.createBuilder()
						.name(optionGroup.name())
						.description(OptionDescription.createBuilder()
								.text(optionGroup.description())
								.build())
						.collapsed(optionGroup.collapsed());

				if (!optionGroup.options().isEmpty()) {
					yaclGroup = yaclGroup.options(YACLOptionAdapter.toYaclOptions(manager, optionGroup.options()));
				}

				yield yaclGroup.build();
			}
		};
	}
}
