package net.azureaaron.dandelion.test;

import java.awt.Color;

import net.azureaaron.dandelion.api.ConfigType;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TestConfig {
	ConfigType configType = ConfigType.MOUL_CONFIG;

	boolean bool = true;

	Item item = Items.RESIN_BLOCK;

	String text = "Dandelion is the best!";

	ColourOptions colourOptions = new ColourOptions();

	Numbers numbers = new Numbers();

	public static class ColourOptions {
		Color normal = new Color(CommonColors.HIGH_CONTRAST_DIAMOND);

		Color alpha = new Color(ARGB.color(0.5f, CommonColors.COSMOS_PINK), true);
	}

	public static class Numbers {
		int integerField = 6;

		int integerSlider = 17;

		float floatField = 8.8f;

		float floatSlider = 10.5f;
	}
}
