package net.azureaaron.dandelion.systems;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

public interface OptionFlag extends Consumer<Minecraft> {
	/**
	 * When invoked, this flag will reload the game assets.
	 */
	OptionFlag ASSET_RELOAD = Minecraft::delayTextureReload;
}
