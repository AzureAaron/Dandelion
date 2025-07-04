package net.azureaaron.dandelion.impl.utils;

import java.awt.Color;

import com.mojang.serialization.Codec;

public class CodecUtils {
	public static final Codec<Color> COLOUR_CODEC = Codec.INT.xmap(argb -> new Color(argb, true), Color::getRGB);
}
