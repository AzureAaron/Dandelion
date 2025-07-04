package net.azureaaron.dandelion.systems;

import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;

public interface OptionFlag extends Consumer<MinecraftClient> {
	/**
	 * When invoked, this flag will reload the game assets.
	 */
	OptionFlag ASSET_RELOAD = MinecraftClient::reloadResourcesConcurrently;
}
