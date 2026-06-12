package net.azureaaron.dandelion.impl.patching;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.dandelion.api.patching.ConfigPatch;
import net.azureaaron.dandelion.api.patching.PatchPredicate;
import net.minecraft.resources.Identifier;

public record EnumPatch(Optional<Identifier> optionId, String path, String value, List<PatchPredicate> predicates) implements ConfigPatch.OfEnum {
	protected static final MapCodec<EnumPatch> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Identifier.CODEC.optionalFieldOf("optionId").forGetter(EnumPatch::optionId),
			Codec.STRING.fieldOf("path").forGetter(EnumPatch::path),
			Codec.STRING.fieldOf("value").forGetter(EnumPatch::value),
			ConfigPatcher.PATCH_PREDICATES_CODEC.optionalFieldOf("predicates", List.of()).forGetter(EnumPatch::predicates))
			.apply(instance, EnumPatch::new));

	@Override
	public MapCodec<? extends ConfigPatch> codec() {
		return MAP_CODEC;
	}
}
