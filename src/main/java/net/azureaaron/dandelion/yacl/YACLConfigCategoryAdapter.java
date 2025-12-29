package net.azureaaron.dandelion.yacl;

import java.util.ArrayList;
import java.util.List;

import net.azureaaron.dandelion.systems.OptionGroup;

public class YACLConfigCategoryAdapter {

	protected static List<dev.isxander.yacl3.api.ConfigCategory> toYaclConfigCategories(List<net.azureaaron.dandelion.systems.ConfigCategory> categories) {
		List<dev.isxander.yacl3.api.ConfigCategory> yaclConfigCategories = new ArrayList<>();

		for (var category : categories) {
			yaclConfigCategories.add(toYaclConfigCategory(category));
		}

		return yaclConfigCategories;
	}

	private static dev.isxander.yacl3.api.ConfigCategory toYaclConfigCategory(net.azureaaron.dandelion.systems.ConfigCategory category) {
		var yaclConfigCategory = dev.isxander.yacl3.api.ConfigCategory.createBuilder()
				.name(category.name());

		OptionGroup group = category.rootGroup();
		if (group != null && !group.options().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.options(YACLOptionAdapter.toYaclOptions(group.options()));
		}

		if (!category.groups().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.groups(YACLOptionGroupAdapter.toYaclOptionGroups(category.groups()));
		}

		return yaclConfigCategory.build();
	}
}
