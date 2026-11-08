package net.azureaaron.dandelion.impl.moulconfig;

import java.net.URI;
import java.util.List;

import com.mojang.blaze3d.Blaze3D;

import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.api.PlatformLinks;

// Custom class for implementing social links since the built-in one has a bug where clicking on the button
// does not open the link in the browser.
public class DandelionSocialLink extends Social {
	private final String link;
	private final List<StructuredText> tooltip;
	private final MyResourceLocation icon;

	protected DandelionSocialLink(PlatformLinks.Link link) {
		this.link = link.link();
		this.tooltip = List.of(MoulConfigPlatform.wrap(link.name()));
		this.icon = MoulConfigPlatform.wrap(link.icon());
	}

	@Override
	public void onClick() {
		Blaze3D.openUri(URI.create(this.link));
	}

	@Override
	public List<StructuredText> getTooltip() {
		return this.tooltip;
	}

	@Override
	public MyResourceLocation getIcon() {
		return this.icon;
	}
}
