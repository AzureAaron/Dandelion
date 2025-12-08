package net.azureaaron.dandelion.moulconfig;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.processor.ProcessedCategory;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import org.jspecify.annotations.Nullable;

/**
 * An abstract processed option which will either represent an editable option or a group/accordion option.
 */
public abstract class DandelionProcessedOption implements ProcessedOption {
	private final int accordionId;
	private final Config config;
	protected ProcessedCategory category;
	protected @Nullable GuiOptionEditor editor;

	protected DandelionProcessedOption(int accordionId, Config config) {
		this.accordionId = accordionId;
		this.config = config;
	}

	@Override
	public int getAccordionId() {
		return this.accordionId;
	}

	protected abstract GuiOptionEditor createEditor();

	@Override
	public GuiOptionEditor getEditor() {
		if (this.editor == null) {
			this.editor = createEditor();
		}

		return this.editor;
	}

	@Override
	public ProcessedCategory getCategory() {
		return this.category;
	}

	@Override
	public String getPath() {
		return "NYI";
	}

	@Override
	public Config getConfig() {
		return this.config;
	}

	@Override
	public void explicitNotifyChange() {}
}
