package net.azureaaron.dandelion.impl.yacl;

import java.util.ArrayList;
import java.util.List;

import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.OptionGroup;

public class YACLConfigCategoryAdapter {

	protected static List<dev.isxander.yacl3.api.ConfigCategory> toYaclConfigCategories(ConfigManager<?> manager, List<net.azureaaron.dandelion.api.ConfigCategory> categories) {
		List<dev.isxander.yacl3.api.ConfigCategory> yaclConfigCategories = new ArrayList<>();

		for (var category : categories) {
			yaclConfigCategories.add(toYaclConfigCategory(manager, category));
		}

		return yaclConfigCategories;
	}

	private static dev.isxander.yacl3.api.ConfigCategory toYaclConfigCategory(ConfigManager<?> manager, net.azureaaron.dandelion.api.ConfigCategory category) {
		var yaclConfigCategory = dev.isxander.yacl3.api.ConfigCategory.createBuilder()
				.name(category.name());

		OptionGroup group = category.rootGroup();
		if (group != null && !group.options().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.options(YACLOptionAdapter.toYaclOptions(manager, group.options()));
		}

		if (!category.groups().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.groups(YACLOptionGroupAdapter.toYaclOptionGroups(manager, category.groups()));
		}

		return yaclConfigCategory.build();
	}
}
