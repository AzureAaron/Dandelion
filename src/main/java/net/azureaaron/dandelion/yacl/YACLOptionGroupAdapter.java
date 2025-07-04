package net.azureaaron.dandelion.yacl;

import java.util.ArrayList;
import java.util.List;

import dev.isxander.yacl3.api.OptionDescription;

public class YACLOptionGroupAdapter {

	protected static List<dev.isxander.yacl3.api.OptionGroup> toYaclOptionGroups(List<net.azureaaron.dandelion.systems.OptionGroup> groups) {
		List<dev.isxander.yacl3.api.OptionGroup> yaclGroups = new ArrayList<>();

		for (var group : groups) {
			yaclGroups.add(toYaclOptionGroup(group));
		}

		return yaclGroups;
	}

	private static dev.isxander.yacl3.api.OptionGroup toYaclOptionGroup(net.azureaaron.dandelion.systems.OptionGroup group) {
		var yaclGroup = dev.isxander.yacl3.api.OptionGroup.createBuilder()
				.name(group.name())
				.description(OptionDescription.createBuilder()
						.text(group.description())
						.build())
				.collapsed(group.collapsed());

		if (!group.options().isEmpty()) {
			yaclGroup = yaclGroup.options(YACLOptionAdapter.toYaclOptions(group.options()));
		}

		return yaclGroup.build();
	}
}
