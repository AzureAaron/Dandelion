package net.azureaaron.dandelion.moulconfig;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorAccordion;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedCategory;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.azureaaron.dandelion.systems.ConfigCategory;

/**
 * Represents a category in the config which holds a list of options which themselves can be editable options under the root
 * or group/accordion options that hold editable options.
 */
public class DandelionProcessedCategory implements ProcessedCategory {
	/** The {@code ConfigCategory} that this category is bound to. */
	private final ConfigCategory category;
	/**
	 * The complete list of options this category holds including editable ones under the root, group/accordion ones, and options
	 * under groups/accordions.
	 */
	private final List<DandelionProcessedOption> options;
	/** Mapping of accordion ids to accordion/group option instances this category holds */
	private final Map<Integer, DandelionProcessedOption> accordions;

	protected DandelionProcessedCategory(ConfigCategory category, List<DandelionProcessedOption> options) {
		this.category = category;
		this.options = List.copyOf(options);
		this.accordions = this.options.stream()
				.filter(opt -> opt.editor instanceof GuiOptionEditorAccordion)
				.collect(Collectors.toUnmodifiableMap(opt -> ((GuiOptionEditorAccordion) opt.editor).getAccordionId(), Function.identity()));

		//Bind our processed options to this category
		for (DandelionProcessedOption option : this.options) {
			option.category = this;
		}
	}

	@Override
	public @Nullable String getDebugDeclarationLocation() {
		return this.category.id().toString();
	}

	@Override
	public StructuredText getDisplayName() {
		return MoulConfigPlatform.wrap(this.category.name());
	}

	@Override
	public StructuredText getDescription() {
		return MoulConfigPlatform.wrap(this.category.description());
	}

	@Override
	public String getIdentifier() {
		return this.category.id().toString();
	}

	@Override
	public @Nullable String getParentCategoryId() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Unmodifiable
	public List<ProcessedOption> getOptions() {
		return List.class.cast(this.options);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Unmodifiable
	public Map<Integer, ProcessedOption> getAccordionAnchors() {
		return Map.class.cast(this.accordions);
	}
}
