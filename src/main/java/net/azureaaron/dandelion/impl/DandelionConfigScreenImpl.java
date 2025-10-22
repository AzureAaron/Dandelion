package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.function.TriFunction;

import net.azureaaron.dandelion.moulconfig.MoulConfigAdapter;
import net.azureaaron.dandelion.platform.ConfigType;
import net.azureaaron.dandelion.systems.ConfigCategory;
import net.azureaaron.dandelion.systems.ConfigManager;
import net.azureaaron.dandelion.systems.DandelionConfigScreen;
import net.azureaaron.dandelion.yacl.YACLScreenAdapter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class DandelionConfigScreenImpl<T> implements DandelionConfigScreen {
	private final ConfigManager<T> manager;
	private final Text title;
	private final List<ConfigCategory> categories;

	public DandelionConfigScreenImpl(ConfigManager<T> manager, TriFunction<T, T, Builder, Builder> screenBuilder) {
		this.manager = Objects.requireNonNull(manager, "manager must not be null");
		Objects.requireNonNull(screenBuilder, "screenBuilder must not be null");

		DandelionConfigScreenBuilderImpl builder = new DandelionConfigScreenBuilderImpl();
		screenBuilder.apply(manager.defaults(), manager.instance(), builder);

		this.title = builder.title;
		this.categories = builder.categories;
	}

	@Override
	public Screen generateScreen(Screen parent, ConfigType configType) {
		return generateScreen(parent, configType, "");
	}

	@Override
	public Screen generateScreen(Screen parent, ConfigType configType, String search) {
		Objects.requireNonNull(configType, "configType must not be null");
		return switch (configType) {
			case ConfigType.YACL -> YACLScreenAdapter.generateYaclScreen(this.manager, this.title, this.categories, parent);
			case ConfigType.MOUL_CONFIG -> new MoulConfigAdapter(this.manager, this.title).generateMoulConfigScreen(this.categories, parent, search);
		};
	}

	protected static class DandelionConfigScreenBuilderImpl implements DandelionConfigScreen.Builder {
		private Text title = Text.empty();
		private List<ConfigCategory> categories = new ArrayList<>();

		@Override
		public Builder title(Text title) {
			this.title = title;
			return this;
		}

		@Override
		public Builder category(ConfigCategory category) {
			this.categories.add(category);
			return this;
		}
	}
}
