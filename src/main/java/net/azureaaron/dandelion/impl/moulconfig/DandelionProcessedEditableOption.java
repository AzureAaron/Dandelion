package net.azureaaron.dandelion.impl.moulconfig;

import java.lang.reflect.Type;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.SearchTag;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import net.azureaaron.dandelion.api.Option;
import net.azureaaron.dandelion.api.OptionListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * Represents an actual option which can be edited in the config, this option will either lie within a group or under the
 * root determined by its {@code accordionId} which respectively links this either to a group/accordion or to the root of the
 * category.
 */
public abstract class DandelionProcessedEditableOption<T> extends DandelionProcessedOption {
	private static final Logger LOGGER = LogUtils.getLogger();
	protected final Option<T> option;

	protected DandelionProcessedEditableOption(Option<T> option, int accordionId, Config config) {
		super(accordionId, config);
		this.option = option;
	}

	@Override
	public StructuredText getName() {
		return MoulConfigPlatform.wrap(this.option.name());
	}

	@Override
	public StructuredText getDescription() {
		Component concat = Component.empty();
		List<Component> siblings = concat.getSiblings();

		// Add a new line between description components to match YACL.
		Component last = this.option.description().isEmpty() ? null : this.option.description().getLast();
		for (Component component : this.option.description()) {
			siblings.add(component);
			if (component != last) siblings.add(CommonComponents.NEW_LINE);
		}

		return MoulConfigPlatform.wrap(concat);
	}

	@Override
	public SearchTag[] getSearchTags() {
		SearchTag[] tags = this.option.tags().stream()
				.map(Component::getString)
				.map(DandelionSearchTag::new)
				.toArray(DandelionSearchTag[]::new);
		return tags;
	}

	@Override
	public Type getType() {
		return this.option.type();
	}

	@Override
	public Object get() {
		return this.option.binding().get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean set(Object value) {
		try {
			this.option.binding().set((T) value);
			this.explicitNotifyChange();

			return true;
		} catch (Exception e) {
			LOGGER.error("[Dandelion] Failed to set option! Id: {}, Name: {}", this.option.id(), this.getName(), e);
		}

		return false;
	}

	@Override
	public void explicitNotifyChange() {
		this.option.listeners().forEach(listener -> listener.onUpdate(this.option, OptionListener.UpdateType.VALUE_CHANGE));
		((MoulConfigDefinition) this.getConfig()).queueOptionFlags(this.option.flags());
	}

	@Override
	public @Nullable String getDebugDeclarationLocation() {
		return this.option.id() != null ? this.option.id().toString() : this.getName().getText();
	}
}
