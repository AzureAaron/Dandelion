package net.azureaaron.dandelion.yacl;

import java.util.ArrayList;
import java.util.List;

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

		if (!category.rootGroup().options().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.options(YACLOptionAdapter.toYaclOptions(category.rootGroup().options()));
		}

		if (!category.groups().isEmpty()) {
			yaclConfigCategory = yaclConfigCategory.groups(YACLOptionGroupAdapter.toYaclOptionGroups(category.groups()));
		}

		return yaclConfigCategory.build();
	}
}
