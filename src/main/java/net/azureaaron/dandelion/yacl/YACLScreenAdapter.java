package net.azureaaron.dandelion.yacl;

import java.util.List;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.azureaaron.dandelion.systems.ConfigCategory;
import net.azureaaron.dandelion.systems.ConfigManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YACLScreenAdapter {

	public static Screen generateYaclScreen(ConfigManager<?> manager, Component title, List<ConfigCategory> categories, Screen parent) {
		var yaclScreenBuilder = YetAnotherConfigLib.createBuilder()
				.title(title)
				.save(manager::save);

		if (!categories.isEmpty()) {
			yaclScreenBuilder = yaclScreenBuilder.categories(YACLConfigCategoryAdapter.toYaclConfigCategories(categories));
		}

		return yaclScreenBuilder
				.build()
				.generateScreen(parent);
	}
}
