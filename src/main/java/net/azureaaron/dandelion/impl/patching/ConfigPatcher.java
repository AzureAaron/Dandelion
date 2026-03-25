package net.azureaaron.dandelion.impl.patching;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.azureaaron.dandelion.api.patching.ConfigPatch;
import net.azureaaron.dandelion.api.patching.PatchPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs.LateBoundIdMapper;

public class ConfigPatcher {
	public static final LateBoundIdMapper<Identifier, MapCodec<? extends ConfigPatch>> PATCH_ID_MAPPER = new LateBoundIdMapper<>();
	public static final LateBoundIdMapper<Identifier, MapCodec<? extends PatchPredicate>> PREDICATE_ID_MAPPER = new LateBoundIdMapper<>();
	protected static final Codec<List<PatchPredicate>> PATCH_PREDICATES_CODEC = PREDICATE_ID_MAPPER.codec(Identifier.CODEC).dispatch("id", PatchPredicate::codec, Function.identity())
			.listOf();

	static {
		// Register built-in patch types
		PATCH_ID_MAPPER.put(BooleanPatch.ID, BooleanPatch.MAP_CODEC);
		PATCH_ID_MAPPER.put(EnumPatch.ID, EnumPatch.MAP_CODEC);
		// Register built-in predicate types
		PREDICATE_ID_MAPPER.put(DatePredicate.ID, DatePredicate.MAP_CODEC);
		PREDICATE_ID_MAPPER.put(MinecraftVersionPredicate.ID, MinecraftVersionPredicate.MAP_CODEC);
		PREDICATE_ID_MAPPER.put(ModVersionPredicate.ID, ModVersionPredicate.MAP_CODEC);
	}

	public static void applyPatches(Object config, List<ConfigPatch> patches) {
		for (ConfigPatch patch : patches) {
			boolean applicable = patch.predicates().stream()
					.map(PatchPredicate::test)
					.reduce(true, (a, b) -> a & b);

			if (applicable) {
				applyPatch(config, patch);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void applyPatch(Object root, ConfigPatch patch) {
		String[] split = patch.path().split("\\.");
		String targetFieldName = split[split.length - 1];
		String[] objects2Traverse = new String[split.length - 1];

		// Copy the traversal path
		System.arraycopy(split, 0, objects2Traverse, 0, split.length - 1);

		Object currentNode = root;

		// Iterate through the path until we get to the object that holds the desired field
		for (String objectName : objects2Traverse) {
			try {
				Field field = currentNode.getClass().getDeclaredField(objectName);
				currentNode = field.get(currentNode);
			} catch (Exception _) {
				// The field likely does not exist so we will return early
				return;
			}
		}

		try {
			Field targetField = currentNode.getClass().getDeclaredField(targetFieldName);

			switch (patch) {
				case ConfigPatch.OfBoolean booleanPatch when targetField.getType() == boolean.class -> targetField.setBoolean(currentNode, booleanPatch.value());
				case ConfigPatch.OfEnum enumPatch when targetField.getType().isEnum() -> {
					Object enumConstant = Enum.valueOf((Class<Enum>) targetField.getType(), enumPatch.value());
					targetField.set(currentNode, enumConstant);
				}
				default -> {}
			}
		} catch (Exception _) {
			// The field did not exist, or its type did not match
			return;
		}
	}
}
