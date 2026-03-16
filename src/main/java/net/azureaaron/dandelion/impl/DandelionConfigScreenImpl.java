package net.azureaaron.dandelion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.azureaaron.dandelion.api.ConfigScreenState;
import org.apache.commons.lang3.function.TriFunction;
import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.api.ConfigCategory;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ConfigType;
import net.azureaaron.dandelion.api.DandelionConfigScreen;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigAdapter;
import net.azureaaron.dandelion.impl.yacl.YACLScreenAdapter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DandelionConfigScreenImpl<T> implements DandelionConfigScreen {
	private final ConfigManager<T> manager;
	private final Component title;
	private final List<ConfigCategory> categories;
	private final String search;

	private @Nullable Supplier<@Nullable ConfigScreenState> stateSupplier = null;
	private @Nullable Consumer<ConfigScreenState> stateConsumer = null;

	public DandelionConfigScreenImpl(ConfigManager<T> manager, TriFunction<T, T, Builder, Builder> screenBuilder) {
		this.manager = Objects.requireNonNull(manager, "manager must not be null");
		Objects.requireNonNull(screenBuilder, "screenBuilder must not be null");

		DandelionConfigScreenBuilderImpl builder = new DandelionConfigScreenBuilderImpl();
		screenBuilder.apply(manager.defaults(), manager.unpatchedInstance(), builder);

		this.title = builder.title;
		this.categories = builder.categories;
		this.search = builder.search;
		this.stateSupplier = builder.stateSupplier;
		this.stateConsumer = builder.stateConsumer;
	}

	@Override
	public Screen generateScreen(@Nullable Screen parent, ConfigType configType) {
		Objects.requireNonNull(configType, "configType must not be null");
		return switch (configType) {
			case ConfigType.YACL -> YACLScreenAdapter.generateYaclScreen(this.manager, this.title, this.categories, parent);
			case ConfigType.MOUL_CONFIG -> new MoulConfigAdapter(this.manager, this.title).generateMoulConfigScreen(this.categories, parent, this.search, this.stateSupplier, this.stateConsumer);
		};
	}

	protected static class DandelionConfigScreenBuilderImpl implements DandelionConfigScreen.Builder {
		private Component title = Component.empty();
		private List<ConfigCategory> categories = new ArrayList<>();
		private String search = "";

		private @Nullable Supplier<@Nullable ConfigScreenState> stateSupplier = null;
		private @Nullable Consumer<ConfigScreenState> stateConsumer = null;

		@Override
		public Builder title(Component title) {
			this.title = title;
			return this;
		}

		@Override
		public Builder category(ConfigCategory category) {
			this.categories.add(category);
			return this;
		}

		@Override
		public Builder search(String search) {
			this.search = search;
			return this;
		}

		@Override
		public Builder withState(Supplier<ConfigScreenState> stateSupplier, Consumer<ConfigScreenState> stateConsumer) {
			this.stateSupplier = stateSupplier;
			this.stateConsumer = stateConsumer;
			return this;
		}
	}
}
