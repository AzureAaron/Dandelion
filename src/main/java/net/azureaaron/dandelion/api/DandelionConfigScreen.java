package net.azureaaron.dandelion.api;

import java.util.List;

import org.apache.commons.lang3.function.TriFunction;
import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.impl.DandelionConfigScreenImpl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public interface DandelionConfigScreen {

	static <T> DandelionConfigScreen create(ConfigManager<T> manager, TriFunction<T, T, Builder, Builder> screenBuilder) {
		return new DandelionConfigScreenImpl<>(manager, screenBuilder);
	}

	Screen generateScreen(@Nullable Screen parent, ConfigType configType);

	interface Builder {
		Builder title(Component title);

		Builder category(ConfigCategory category);

		Builder search(String search);

		Builder platformLinks(PlatformLinks links);

		default Builder categoryIf(boolean condition, ConfigCategory category) {
			return condition ? category(category) : this;
		}

		default Builder categories(List<ConfigCategory> categories) {
			for (ConfigCategory category : categories) {
				category(category);
			}

			return this;
		}
	}
}
