package net.azureaaron.dandelion.moulconfig;

import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.SearchTag;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorAccordion;
import net.azureaaron.dandelion.systems.OptionGroup;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

/**
 * This class represents a {@code ProcessedOption} that maps to an {@code OptionGroup} or accordion (in MoulConfig terms) which
 * under which editable options exist.
 */
public class DandelionProcessedGroupOption extends DandelionProcessedOption {
	private final OptionGroup group;
	private final int categoryAccordionId;

	protected DandelionProcessedGroupOption(OptionGroup group, int rootAccordionId, int categoryAccordionId, Config config) {
		super(rootAccordionId, config);
		this.group = group;
		this.categoryAccordionId = categoryAccordionId;
	}

	@Override
	public String getName() {
		return this.group.name().getString();
	}

	@Override
	public String getDescription() {
		return StringVisitable.concat(this.group.description()).getString();
	}

	@Override
	public SearchTag[] getSearchTags() {
		SearchTag[] tags = this.group.tags().stream()
				.map(Text::getString)
				.map(DandelionSearchTag::new)
				.toArray(DandelionSearchTag[]::new);
		return tags;
	}

	@Override
	public Object get() {
		return (Void) null;
	}

	@Override
	public Type getType() {
		return Void.class;
	}

	@Override
	public boolean set(Object value) {
		return false;
	}

	@Override
	@Nullable
	public String getDebugDeclarationLocation() {
		return this.group.id() != null ? this.group.id().toString() : this.getName();
	}

	@Override
	protected GuiOptionEditor createEditor() {
		return new GuiOptionEditorAccordion(this, this.categoryAccordionId);
	}
}
