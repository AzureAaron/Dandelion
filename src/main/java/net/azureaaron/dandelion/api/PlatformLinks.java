package net.azureaaron.dandelion.api;

import java.util.List;

import net.azureaaron.dandelion.Dandelion;
import net.azureaaron.dandelion.impl.PlatformLinksImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface PlatformLinks {
	/// Built-in GitHub icon.
	///
	/// The texture was made by Bedrock Armor.
	Identifier GITHUB_ICON = Dandelion.id("textures/gui/github.png");
	/// Built-in Modrinth icon.
	///
	/// The texture was made by Bedrock Armor.
	Identifier MODRINTH_ICON = Dandelion.id("textures/gui/modrinth.png");

	/// Creates a new builder from which platform links can be appended.
	static Builder createBuilder() {
		return new PlatformLinksImpl.PlatformLinksBuilderImpl();
	}

	/// {@return the links}
	List<Link> links();

	interface Builder {
		/// Adds a new link.
		///
		/// @param name the name for this link, typically of the platform it leads to
		/// @param icon the icon to be displayed on the button for this link
		/// @param link the actual link
		///
		/// @apiNote The provided {@code link} is checked for validity.
		Builder link(Component name, Identifier icon, String link);

		/// {@return the constructed {@code PlatformLinks} instance}
		PlatformLinks build();
	}

	interface Link {
		/// {@return the name of this link}
		Component name();

		/// {@return the icon of this link}
		Identifier icon();

		/// {@return the actual link}
		String link();
	}
}
