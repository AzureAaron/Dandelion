package net.azureaaron.dandelion.impl.moulconfig;

import java.util.List;

import org.jspecify.annotations.Nullable;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.DescriptionRendereringBehaviour;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.TitleRenderingBehaviour;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.azureaaron.dandelion.api.PlatformLinks;
import net.minecraft.network.chat.Component;

public class MoulConfigDefinition extends Config {
	private final Component title;
	private final @Nullable PlatformLinks platformLinks;

	protected MoulConfigDefinition(Component title, @Nullable PlatformLinks platformLinks) {
		this.title = title;
		this.platformLinks = platformLinks;
	}

	@Override
	public StructuredText getTitle() {
		return MoulConfigPlatform.wrap(this.title);
	}

	@Override
	public List<Social> getSocials() {
		if (this.platformLinks != null) {
			return this.platformLinks.links().stream()
					.map(DandelionSocialLink::new)
					.map(Social.class::cast)
					.toList();
		}

		return List.of();
	}

	@Override
	public boolean shouldAutoFocusSearchbar() {
		return true;
	}

	@Override
	public TitleRenderingBehaviour getTitleRenderingBehaviour(ProcessedOption option) {
		return TitleRenderingBehaviour.WIDE_CENTERED_UNDERLINED;
	}

	@Override
	public DescriptionRendereringBehaviour getDescriptionBehaviour(ProcessedOption option) {
		return DescriptionRendereringBehaviour.EXPAND_PANEL;
	}
}
