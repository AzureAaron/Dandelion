package net.azureaaron.dandelion.moulconfig;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.DescriptionRendereringBehaviour;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.text.Text;

public class MoulConfigDefinition extends Config {
	private final String title;

	protected MoulConfigDefinition(Text title) {
		this.title = title.getString();
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public boolean shouldAutoFocusSearchbar() {
		return true;
	}

	@Override
	public DescriptionRendereringBehaviour getDescriptionBehaviour(ProcessedOption option) {
		return DescriptionRendereringBehaviour.EXPAND_PANEL;
	}
}
