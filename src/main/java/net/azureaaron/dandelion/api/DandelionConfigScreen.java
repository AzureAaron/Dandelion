package net.azureaaron.dandelion.api;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

	//TODO add support for moulconfig social links
	interface Builder {
		Builder title(Component title);

		Builder category(ConfigCategory category);

		Builder search(String search);

		Builder withState(Supplier<@Nullable ConfigScreenState> supplier, Consumer<@Nullable ConfigScreenState> consumer);

		default Builder categoryIf(boolean condition, ConfigCategory category) {
			return condition ? category(category) : this;
		}

		default Builder withStateIf(boolean condition, Supplier<@Nullable ConfigScreenState> supplier, Consumer<@Nullable ConfigScreenState> consumer) {
			return condition ? withState(supplier, consumer) : this;
		}

		default Builder categories(List<ConfigCategory> categories) {
			for (ConfigCategory category : categories) {
				category(category);
			}

			return this;
		}
	}
}
