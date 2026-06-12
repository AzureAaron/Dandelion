package net.azureaaron.dandelion.api.patching;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.azureaaron.dandelion.Dandelion;
import net.azureaaron.dandelion.impl.patching.ConfigPatcher;
import net.minecraft.resources.Identifier;


/// A patch that can be applied to a value in a config classes.
public sealed interface ConfigPatch {
	/// The {@link Codec} used to deserialize config patches.
	///
	/// <strong>The usage of this {@code Codec} off-thread is not recommended.</strong>
	Codec<List<ConfigPatch>> PATCH_LIST_CODEC = ConfigPatcher.PATCH_ID_MAPPER.codec(Identifier.CODEC).dispatch(ConfigPatch::codec, Function.identity()).listOf();

	/// {@return the {@link MapCodec} used to deserialize the patch}
	MapCodec<? extends ConfigPatch> codec();

	/// {@return the id of the option being patched}
	Optional<Identifier> optionId();

	/// {@return the path to the field being patched from the root of the config}
	String path();

	/// {@return the predicates required for the patch to be applied}
	List<PatchPredicate> predicates();

	/// Config Patch for overriding boolean values.
	non-sealed interface OfBoolean extends ConfigPatch {
		Identifier ID = Dandelion.id("patch/boolean");

		/// {@return the overridden boolean value}
		boolean value();
	}

	/// Config Patch for overriding Enum-based values.
	non-sealed interface OfEnum extends ConfigPatch {
		Identifier ID = Dandelion.id("patch/enum");

		/// {@return the overridden Enum value}
		String value();
	}
}
