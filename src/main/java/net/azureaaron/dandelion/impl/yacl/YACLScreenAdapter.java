package net.azureaaron.dandelion.impl.yacl;

import java.util.List;

import org.jspecify.annotations.Nullable;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.azureaaron.dandelion.api.ConfigCategory;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.impl.ConfigManagerImpl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YACLScreenAdapter {

	public static Screen generateYaclScreen(ConfigManager<?> manager, Component title, List<ConfigCategory> categories, @Nullable Screen parent) {
		var yaclScreenBuilder = YetAnotherConfigLib.createBuilder()
				.title(title)
				.save(((ConfigManagerImpl<?>) manager)::saveAll);

		if (!categories.isEmpty()) {
			yaclScreenBuilder = yaclScreenBuilder.categories(YACLConfigCategoryAdapter.toYaclConfigCategories(manager, categories));
		}

		return yaclScreenBuilder
				.build()
				.generateScreen(parent);
	}
}
