package net.azureaaron.dandelion.impl.moulconfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.DescriptionRendereringBehaviour;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.TitleRenderingBehaviour;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.azureaaron.dandelion.api.ConfigManager;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.OptionFlag;
import net.azureaaron.dandelion.api.PlatformLinks;
import net.azureaaron.dandelion.impl.ConfigManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class MoulConfigDefinition extends Config {
	private final ConfigManager<?> manager;
	private final Component title;
	private final @Nullable PlatformLinks platformLinks;
	private final Set<OptionFlag> queuedOptionFlags = new HashSet<>();

	protected MoulConfigDefinition(ConfigManager<?> manager, Component title, @Nullable PlatformLinks platformLinks) {
		this.manager = manager;
		this.title = title;
		this.platformLinks = platformLinks;

		this.saveRunnables.add(manager::save);
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
	public void saveNow() {
		super.saveNow();

		for (OptionFlag flag : this.queuedOptionFlags) {
			flag.accept(Minecraft.getInstance());
		}
	}

	/// Queues the {@code flags} to run after the config screen is saved.
	///
	/// @implNote Each unique option flag will only execute once.
	public void queueOptionFlags(List<OptionFlag> flags) {
		this.queuedOptionFlags.addAll(flags);
	}

	/// {@return whether the {@code option} is patched}
	public boolean isOptionPatched(Option<?> option) {
		return ((ConfigManagerImpl<?>) this.manager).isOptionPatched(option.id());
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
