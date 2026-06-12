package net.azureaaron.dandelion.api;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface OptionFlag extends Consumer<Minecraft> {
	/// When invoked, this flag will reload the game assets.
	OptionFlag ASSET_RELOAD = Minecraft::delayTextureReload;

	/// When invoked, this flag will reload the game's chunks.
	OptionFlag RELOAD_CHUNKS = client -> client.levelExtractor.allChanged();
}
