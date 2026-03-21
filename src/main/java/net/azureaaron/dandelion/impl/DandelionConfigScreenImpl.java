package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.function.TriFunction;
import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.api.ConfigCategory;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ConfigType;
import net.azureaaron.dandelion.api.DandelionConfigScreen;
import net.azureaaron.dandelion.api.PlatformLinks;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigAdapter;
import net.azureaaron.dandelion.impl.yacl.YACLScreenAdapter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DandelionConfigScreenImpl<T> implements DandelionConfigScreen {
	private final ConfigManager<T> manager;
	private final Component title;
	private final List<ConfigCategory> categories;
	private final String search;
	private final @Nullable PlatformLinks platformLinks;

	public DandelionConfigScreenImpl(ConfigManager<T> manager, TriFunction<T, T, Builder, Builder> screenBuilder) {
		this.manager = Objects.requireNonNull(manager, "manager must not be null");
		Objects.requireNonNull(screenBuilder, "screenBuilder must not be null");

		DandelionConfigScreenBuilderImpl builder = new DandelionConfigScreenBuilderImpl();
		screenBuilder.apply(manager.defaults(), manager.unpatchedInstance(), builder);

		this.title = builder.title;
		this.categories = builder.categories;
		this.search = builder.search;
		this.platformLinks = builder.platformLinks;
	}

	@Override
	public Screen generateScreen(@Nullable Screen parent, ConfigType configType) {
		Objects.requireNonNull(configType, "configType must not be null");
		return switch (configType) {
			case ConfigType.YACL -> YACLScreenAdapter.generateYaclScreen(this.manager, this.title, this.categories, parent);
			case ConfigType.MOUL_CONFIG -> new MoulConfigAdapter(this.manager, this.title, this.platformLinks).generateMoulConfigScreen(this.categories, parent, this.search);
			default -> throw new UnsupportedOperationException("The requested backend is unavailable.");
		};
	}

	protected static class DandelionConfigScreenBuilderImpl implements DandelionConfigScreen.Builder {
		private Component title = Component.empty();
		private List<ConfigCategory> categories = new ArrayList<>();
		private String search = "";
		private @Nullable PlatformLinks platformLinks = null;

		@Override
		public Builder title(Component title) {
			this.title = Objects.requireNonNull(title, "title must not be null");
			return this;
		}

		@Override
		public Builder category(ConfigCategory category) {
			this.categories.add(Objects.requireNonNull(category, "category must not be null"));
			return this;
		}

		@Override
		public Builder search(String search) {
			this.search = Objects.requireNonNull(search, "search must not be null");
			return this;
		}

		@Override
		public Builder platformLinks(PlatformLinks links) {
			this.platformLinks = Objects.requireNonNull(links, "links must not be null");
			return this;
		}
	}
}
