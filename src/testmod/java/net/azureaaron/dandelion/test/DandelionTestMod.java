package net.azureaaron.dandelion.test;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;

import net.azureaaron.dandelion.Dandelion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class DandelionTestMod implements ClientModInitializer {
	protected static final String MINECRAFT_VERSION = SharedConstants.getCurrentVersion().id();
	private static final KeyMapping.Category KEY_MAPPING_CATEGORY = KeyMapping.Category.register(Dandelion.id("general"));
	protected static final KeyMapping TEST_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.dandelion.test", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_MAPPING_CATEGORY));

	@Override
	public void onInitializeClient() {
		IO.println("Loaded Dandelion Test Mod!");
		TestConfigManager.init();

		// Add a command to open the configuration
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
			dispatcher.register(ClientCommands.literal("dandelion").executes(context -> {
				// Open a test configuration GUI
				Minecraft minecraft = context.getSource().getClient();
				minecraft.schedule(() -> minecraft.gui.setScreen(TestConfigManager.createGui()));

				return Command.SINGLE_SUCCESS;
			}));
		});

		// Add a button to the title screen to open the configuration
		ScreenEvents.AFTER_INIT.register((minecraft, screen, _, _) -> {
			if (screen instanceof TitleScreen) {
				List<AbstractWidget> widgets = Screens.getWidgets(screen);
				AbstractWidget realmsButton = Screens.getWidgets(screen).get(2);
				SpriteIconButton configButton = SpriteIconButton.builder(Component.literal("Config"), _ -> minecraft.gui.setScreen(TestConfigManager.createGui()), true)
						.sprite(id("icon/config"), 16, 16)
						.size(20, 20)
						.build();

				configButton.setPosition(realmsButton.getRight() + 4, realmsButton.getY());
				widgets.add(configButton);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.level != null && TEST_KEY_MAPPING.consumeClick()) {
				client.player.sendSystemMessage(Component.literal("[Dandelion] Pressed test key mapping!"));
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath("dandelion-testmod", path);
	}
}
