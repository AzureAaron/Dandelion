package net.azureaaron.dandelion.yacl;

import java.util.ArrayList;
import java.util.List;

import dev.isxander.yacl3.api.OptionDescription;
import net.azureaaron.dandelion.systems.ListOption;
import net.azureaaron.dandelion.systems.OptionGroup;

public class YACLOptionGroupAdapter {

	protected static List<dev.isxander.yacl3.api.OptionGroup> toYaclOptionGroups(List<net.azureaaron.dandelion.systems.OptionGroup> groups) {
		List<dev.isxander.yacl3.api.OptionGroup> yaclGroups = new ArrayList<>();

		for (var group : groups) {
			yaclGroups.add(toYaclOptionGroup(group));
		}

		return yaclGroups;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
    private static dev.isxander.yacl3.api.OptionGroup toYaclOptionGroup(net.azureaaron.dandelion.systems.OptionGroup group) {
		return switch (group) {
			case ListOption listOption -> dev.isxander.yacl3.api.ListOption.createBuilder()
					.name(listOption.name())
					.description(OptionDescription.createBuilder()
							.text(listOption.description())
							.build())
					.binding((List<Object>) listOption.binding().defaultValue(),
                            () -> (List<Object>) listOption.binding().get(),
                            listOption.binding()::set)
					.controller(yaclOption -> YACLControllerAdapter.toYaclControllerBuilder(yaclOption, listOption.entryType(), listOption.entryController()))
					.initial(listOption.initialValue())
					.collapsed(listOption.collapsed())
					.available(listOption.modifiable())
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
					yaclGroup = yaclGroup.options(YACLOptionAdapter.toYaclOptions(optionGroup.options()));
				}

				yield yaclGroup.build();
			}
		};
	}
}
