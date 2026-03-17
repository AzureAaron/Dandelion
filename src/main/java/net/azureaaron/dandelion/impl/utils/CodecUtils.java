package net.azureaaron.dandelion.impl.utils;

import java.awt.Color;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;

public class CodecUtils {
	public static final Codec<Color> COLOUR_CODEC = Codec.INT.xmap(argb -> new Color(argb, true), Color::getRGB);
	public static final Codec<SemanticVersion> SEMANTIC_VERSION_CODEC = Codec.STRING.comapFlatMap(version -> {
		try {
			return DataResult.success(SemanticVersion.parse(version));
		} catch (VersionParsingException _) {
			return DataResult.error(() -> "Failed to parse semantic version from string: " + version);
		}
	}, SemanticVersion::toString);
}
