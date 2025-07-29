package net.azureaaron.dandelion.moulconfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.processor.ProcessedCategory;
import net.azureaaron.dandelion.systems.ConfigCategory;
import net.azureaaron.dandelion.systems.ConfigManager;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionGroup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MoulConfigAdapter {
	/**
	 * The id of the accordion which is at the root of each category.
	 */
	private static final int ROOT_ACCORDION = -1;
	private final ConfigManager<?> manager;
	private final Text title;
	private final MoulConfigDefinition configDefinition;
	//LinkedHashMap to preserve insertion order
	private final Map<Option<?>, BiFunction<Integer, MoulConfigDefinition, DandelionProcessedEditableOption<?>>> editableOptionFactories = new LinkedHashMap<>();

	public MoulConfigAdapter(ConfigManager<?> manager, Text title) {
		this.manager = manager;
		this.title = title;
		this.configDefinition = new MoulConfigDefinition(title);
	}

	public Screen generateMoulConfigScreen(List<ConfigCategory> categories, Screen parent) {
		this.generateEditableOptions(categories);

		List<DandelionProcessedCategory> processedCategories = this.generateProcessedCategories(categories);
		MoulConfigEditor<MoulConfigDefinition> editor = new MoulConfigEditor<>(ProcessedCategory.collect(processedCategories), this.configDefinition);
		DandelionConfigScreenComponent moulConfigScreenComponent = new DandelionConfigScreenComponent(this.title, new GuiContext(new GuiElementComponent(editor)), parent, this.manager::save);

		return moulConfigScreenComponent;
	}

	private void generateEditableOptions(List<ConfigCategory> categories) {
		Stream<Option<?>> allRootOptions = categories.stream()
				.map(ConfigCategory::rootGroup)
				.filter(Objects::nonNull)
				.flatMap(group -> group.options().stream());
		Stream<Option<?>> allGroupedOptions = categories.stream()
				.flatMap(category -> category.groups().stream())
				.flatMap(group -> group.options().stream());
		List<Option<?>> allOptions = Stream.concat(allRootOptions, allGroupedOptions).toList();

		for (Option<?> option : allOptions) {
			this.editableOptionFactories.put(option, MoulConfigEditableOptionAdapter.createEditableOptionFactory(option));
		}
	}

	private List<DandelionProcessedCategory> generateProcessedCategories(List<ConfigCategory> categories) {
		return categories.stream()
				.map(category -> {
					List<DandelionProcessedOption> processedOptions = new ArrayList<>();
					int nextAccordionId = 1717;

					//Add root group as options to the category under the root accordion
					if (category.rootGroup() != null) {
						this.addProcessedOptionsToGroup(processedOptions, category.rootGroup().options(), ROOT_ACCORDION);
					}

					//For each option group, create an accordion option and then add each of its options to said accordion
					for (OptionGroup group : category.groups()) {
						int groupAccordionId = nextAccordionId++;

						//Create the accordion/group option under the root accordion
						processedOptions.add(new DandelionProcessedGroupOption(group, ROOT_ACCORDION, groupAccordionId, this.configDefinition));
						//Add the options in this group under the generated accordion (grouping)
						this.addProcessedOptionsToGroup(processedOptions, group.options(), groupAccordionId);
					}

					//Return the newly created category holding all of our options
					return new DandelionProcessedCategory(category, processedOptions);
				}).toList();
	}

	private void addProcessedOptionsToGroup(List<DandelionProcessedOption> processedOptions, List<Option<?>> options, int groupAccordionId) {
		for (Option<?> option : options) {
			//Each option should be in the map unless its not supported (at which point an exception would be thrown when
			//attempting to create the factory)
			DandelionProcessedEditableOption<?> processedOption = this.editableOptionFactories.get(option).apply(groupAccordionId, this.configDefinition);
			processedOptions.add(processedOption);
		}
	}
}
