package net.azureaaron.dandelion.moulconfig;

import java.lang.reflect.Type;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.github.notenoughupdates.moulconfig.Config;
import net.azureaaron.dandelion.systems.Option;
import net.azureaaron.dandelion.systems.OptionListener;
import net.minecraft.text.StringVisitable;

/**
 * Represents an actual option which can be edited in the config, this option will either lie within a group or under the
 * root determined by it's {@code accordionId} which respectively links this either to a group/accordion or to the root of the
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
	public String getName() {
		return this.option.name().getString();
	}

	@Override
	public String getDescription() {
		return StringVisitable.concat(this.option.description()).getString();
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
		this.option.listeners().forEach(listener -> listener.onUpdate(option, OptionListener.UpdateType.VALUE_CHANGE));
	}

	@Override
	@Nullable
	public String getDebugDeclarationLocation() {
		return this.option.id() != null ? this.option.id().toString() : this.getName();
	}
}
