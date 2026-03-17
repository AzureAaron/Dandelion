package net.azureaaron.dandelion.impl.patching;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.dandelion.api.patching.ComparisonOperator;
import net.azureaaron.dandelion.api.patching.PatchPredicate;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.game.minecraft.McVersionLookup;
import net.minecraft.SharedConstants;

public record MinecraftVersionPredicate(String version, ComparisonOperator operator) implements PatchPredicate.OfMinecraftVersion {
	protected static final MapCodec<MinecraftVersionPredicate> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("version").forGetter(MinecraftVersionPredicate::version),
			ComparisonOperator.CODEC.fieldOf("operator").forGetter(MinecraftVersionPredicate::operator))
			.apply(instance, MinecraftVersionPredicate::new));

	@Override
	public MapCodec<? extends PatchPredicate> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean test() {
		String currentVersionId = SharedConstants.getCurrentVersion().id();
		String normalizedCurrent = McVersionLookup.normalizeVersion(currentVersionId, McVersionLookup.getRelease(currentVersionId));
		String normalizedTarget = McVersionLookup.normalizeVersion(this.version(), McVersionLookup.getRelease(this.version()));

		try {
			SemanticVersion currentVersion = SemanticVersion.parse(normalizedCurrent);
			SemanticVersion targetVersion = SemanticVersion.parse(normalizedTarget);

			return this.operator().compare(currentVersion, targetVersion);
		} catch (VersionParsingException _) {}

		return false;
	}
}
