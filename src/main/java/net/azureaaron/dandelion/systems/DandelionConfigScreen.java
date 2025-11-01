package net.azureaaron.dandelion.systems;

import java.util.List;

import org.apache.commons.lang3.function.TriFunction;

import net.azureaaron.dandelion.impl.DandelionConfigScreenImpl;
import net.azureaaron.dandelion.platform.ConfigType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public interface DandelionConfigScreen {

	static <T> DandelionConfigScreen create(ConfigManager<T> manager, TriFunction<T, T, Builder, Builder> screenBuilder) {
		return new DandelionConfigScreenImpl<>(manager, screenBuilder);
	}

	Screen generateScreen(Screen parent, ConfigType configType);

	//TODO add support for moulconfig social links
	interface Builder {
		Builder title(Text title);

		Builder category(ConfigCategory category);

		Builder search(String search);

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
