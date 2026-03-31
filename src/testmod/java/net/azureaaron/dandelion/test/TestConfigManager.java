package net.azureaaron.dandelion.test;

import java.awt.Color;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

import net.azureaaron.dandelion.api.ButtonOption;
import net.azureaaron.dandelion.api.ConfigCategory;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.ConfigType;
import net.azureaaron.dandelion.api.DandelionConfigScreen;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.OptionGroup;
import net.azureaaron.dandelion.api.PlatformLinks;
import net.azureaaron.dandelion.api.controllers.BooleanController;
import net.azureaaron.dandelion.api.controllers.ColourController;
import net.azureaaron.dandelion.api.controllers.EnumController;
import net.azureaaron.dandelion.api.controllers.FloatController;
import net.azureaaron.dandelion.api.controllers.IntegerController;
import net.azureaaron.dandelion.api.controllers.ItemController;
import net.azureaaron.dandelion.api.controllers.StringController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;

public class TestConfigManager {
	public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("dandelion-testmod.json");
	private static final ConfigManager<TestConfig> CONFIG_MANAGER = ConfigManager.create(TestConfig.class, PATH, UnaryOperator.identity());

	protected static void init() {
		CONFIG_MANAGER.load();
	}

	protected static Screen createGui() {
		return DandelionConfigScreen.create(CONFIG_MANAGER, (defaults, config, builder) -> builder
				.title(Component.literal("Dandelion Test Mod 1.0.0"))
				.category(createPrimaryCategory(defaults, config))
				.category(createSecondaryCategory(defaults, config))
				.platformLinks(PlatformLinks.createBuilder()
						.link(Component.literal("GitHub"), PlatformLinks.GITHUB_ICON, "https://github.com/AzureAaron/Dandelion")
						.link(Component.literal("Modrinth"), PlatformLinks.MODRINTH_ICON, "https://modrinth.com/mod/aaron-mod")
						.build())
				).generateScreen(null, CONFIG_MANAGER.instance().configType);
	}

	private static ConfigCategory createPrimaryCategory(TestConfig defaults, TestConfig config) {
		return ConfigCategory.createBuilder()
				.name(Component.literal("Primary"))
				.id(DandelionTestMod.id("primary"))

				// Ungrouped options
				.option(Option.<ConfigType>createBuilder()
						.name(Component.literal("Config Type"))
						.binding(defaults.configType,
								() -> config.configType,
								newValue -> config.configType = newValue)
						.controller(EnumController.<ConfigType>createBuilder()
								.dropdown(true)
								.build())
						.build())
				.option(Option.<Boolean>createBuilder()
						.name(Component.literal("Boolean"))
						.binding(defaults.bool,
								() -> config.bool,
								newValue -> config.bool = newValue)
						.controller(BooleanController.createBuilder()
								.booleanStyle(BooleanController.BooleanStyle.YES_NO)
								.coloured(true)
								.build())
						.build())

				// Colour
				.group(OptionGroup.createBuilder()
						.name(Component.literal("Colour").withColor(CommonColors.GREEN))
						.collapsed(true)
						.option(Option.<Color>createBuilder()
								.name(Component.literal("Normal").withColor(CommonColors.HIGH_CONTRAST_DIAMOND))
								.binding(defaults.colourOptions.normal,
										() -> config.colourOptions.normal,
										newValue -> config.colourOptions.normal = newValue)
								.controller(ColourController.createBuilder()
										.build())
								.build())
						.option(Option.<Color>createBuilder()
								.name(Component.literal("Alpha").withColor(CommonColors.COSMOS_PINK))
								.binding(defaults.colourOptions.alpha,
										() -> config.colourOptions.alpha,
										newValue -> config.colourOptions.alpha = newValue)
								.controller(ColourController.createBuilder()
										.hasAlpha(true)
										.build())
								.build())
						.build())

				// Numbers
				.group(OptionGroup.createBuilder()
						.name(Component.literal("Numbers"))
						.collapsed(true)
						.option(Option.<Integer>createBuilder()
								.name(Component.literal("Integer Field"))
								.binding(defaults.numbers.integerField,
										() -> config.numbers.integerField,
										newValue -> config.numbers.integerField = newValue)
								.controller(IntegerController.createBuilder()
										.build())
								.build())
						.option(Option.<Integer>createBuilder()
								.name(Component.literal("Integer Slider"))
								.binding(defaults.numbers.integerSlider,
										() -> config.numbers.integerSlider,
										newValue -> config.numbers.integerSlider = newValue)
								.controller(IntegerController.createBuilder()
										.range(10, 20)
										.slider(1)
										.build())
								.build())
						.option(Option.<Float>createBuilder()
								.name(Component.literal("Float Field"))
								.binding(defaults.numbers.floatField,
										() -> config.numbers.floatField,
										newValue -> config.numbers.floatField = newValue)
								.controller(FloatController.createBuilder()
										.build())
								.build())
						.option(Option.<Float>createBuilder()
								.name(Component.literal("Float Slider"))
								.binding(defaults.numbers.floatSlider,
										() -> config.numbers.floatSlider,
										newValue -> config.numbers.floatSlider = newValue)
								.controller(FloatController.createBuilder()
										.range(10f, 12f)
										.slider(0.5f)
										.build())
								.build())
						.build())

				.build();
	}

	private static ConfigCategory createSecondaryCategory(TestConfig defaults, TestConfig config) {
		return ConfigCategory.createBuilder()
				.name(Component.literal("Secondary"))
				.id(DandelionTestMod.id("secondary"))

				.option(ButtonOption.createBuilder()
						.name(Component.literal("Play Sound").withColor(CommonColors.SOFT_YELLOW))
						.prompt(Component.literal("Click"))
						.action(_ -> Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMETHYST_BLOCK_STEP, 1f, 1f)))
						.build())
				.option(Option.<Item>createBuilder()
						.name(Component.literal("Item").withColor(0xFF5517))
						.binding(defaults.item,
								() -> config.item,
								newValue -> config.item = newValue)
						.controller(ItemController.createBuilder()
								.build())
						.build())
				.option(Option.<String>createBuilder()
						.name(Component.literal("String").withColor(CommonColors.SOFT_RED))
						.binding(defaults.text,
								() -> config.text,
								newValue -> config.text = newValue)
						.controller(StringController.createBuilder()
								.build())
						.build())

				.build();
	}
}
