package net.azureaaron.dandelion.test;

import java.util.List;

import com.mojang.brigadier.Command;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class DandelionTestMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		IO.println("Loaded Dandelion Test Mod!");
		TestConfigManager.init();

		// Add a command to open the configuration
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
			dispatcher.register(ClientCommands.literal("dandelion").executes(context -> {
				// Open a test configuration GUI
				Minecraft minecraft = context.getSource().getClient();
				minecraft.schedule(() -> minecraft.setScreen(TestConfigManager.createGui()));

				return Command.SINGLE_SUCCESS;
			}));
		});

		// Add a button to the title screen to open the configuration
		ScreenEvents.AFTER_INIT.register((minecraft, screen, _, _) -> {
			if (screen instanceof TitleScreen) {
				List<AbstractWidget> widgets = Screens.getWidgets(screen);
				AbstractWidget realmsButton = Screens.getWidgets(screen).get(2);
				SpriteIconButton configButton = SpriteIconButton.builder(Component.literal("Config"), _ -> minecraft.setScreen(TestConfigManager.createGui()), true)
						.sprite(id("icon/config"), 16, 16)
						.size(20, 20)
						.build();

				configButton.setPosition(realmsButton.getRight() + 4, realmsButton.getY());
				widgets.add(configButton);
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath("dandelion-testmod", path);
	}
}
