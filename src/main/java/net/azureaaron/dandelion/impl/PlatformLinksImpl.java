package net.azureaaron.dandelion.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.azureaaron.dandelion.api.PlatformLinks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PlatformLinksImpl implements PlatformLinks {
	private final List<PlatformLinks.Link> links;

	protected PlatformLinksImpl(List<PlatformLinks.Link> links) {
		this.links = links;
	}

	@Override
	public List<PlatformLinks.Link> links() {
		return this.links;
	}

	public static class PlatformLinksBuilderImpl implements PlatformLinks.Builder {
		private final List<PlatformLinks.Link> links = new ArrayList<>();

		@Override
		public Builder link(Component name, Identifier icon, String link) {
			this.links.add(new PlatformLinksLinkImpl(name, icon, link));
			return this;
		}

		@Override
		public PlatformLinks build() {
			return new PlatformLinksImpl(this.links);
		}
	}

	private record PlatformLinksLinkImpl(Component name, Identifier icon, String link) implements PlatformLinks.Link {
		public PlatformLinksLinkImpl {
			Objects.requireNonNull(name, "name must not be null");
			Objects.requireNonNull(icon, "icon must not be null");
			Objects.requireNonNull(link, "link must not be null");

			// Ensures that the link is valid and can be parsed by Java
			URI.create(link);
		}
	}
}
