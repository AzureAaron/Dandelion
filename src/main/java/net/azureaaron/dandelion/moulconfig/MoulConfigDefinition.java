package net.azureaaron.dandelion.moulconfig;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.DescriptionRendereringBehaviour;
import io.github.notenoughupdates.moulconfig.TitleRenderingBehaviour;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.text.Text;

public class MoulConfigDefinition extends Config {
	private final Text title;

	protected MoulConfigDefinition(Text title) {
		this.title = title;
	}

	@Override
	public StructuredText getTitle() {
		return MoulConfigPlatform.wrap(this.title);
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
