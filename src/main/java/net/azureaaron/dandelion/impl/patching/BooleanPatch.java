package net.azureaaron.dandelion.impl.patching;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.dandelion.api.patching.ConfigPatch;
import net.azureaaron.dandelion.api.patching.PatchPredicate;
import net.minecraft.resources.Identifier;

public record BooleanPatch(Optional<Identifier> optionId, String path, boolean value, List<PatchPredicate> predicates) implements ConfigPatch.OfBoolean {
	protected static final MapCodec<BooleanPatch> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Identifier.CODEC.optionalFieldOf("optionId").forGetter(BooleanPatch::optionId),
			Codec.STRING.fieldOf("path").forGetter(BooleanPatch::path),
			Codec.BOOL.fieldOf("value").forGetter(BooleanPatch::value),
			ConfigPatcher.PATCH_PREDICATES_CODEC.optionalFieldOf("predicates", List.of()).forGetter(BooleanPatch::predicates))
			.apply(instance, BooleanPatch::new));

	@Override
	public MapCodec<? extends ConfigPatch> codec() {
		return MAP_CODEC;
	}
}
