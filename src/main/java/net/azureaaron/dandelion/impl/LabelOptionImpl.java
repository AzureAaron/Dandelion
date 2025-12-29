package net.azureaaron.dandelion.impl;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.systems.LabelOption;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.Identifier;

public class LabelOptionImpl implements LabelOption {
	private final Component label;

	protected LabelOptionImpl(Component label) {
		this.label = Objects.requireNonNull(label, "label must not be null");;
	}

	@Override
	public @Nullable Identifier id() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Component name() {
		return Component.empty();
	}

	@Override
	public List<Component> description() {
		return List.of();
	}

	@Override
	public List<Component> tags() {
		return List.of();
	}

	@Override
	public OptionBinding<Component> binding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Controller<Component> controller() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean modifiable() {
		return true;
	}

	@Override
	public List<OptionFlag> flags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<OptionListener<Component>> listeners() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<Component> type() {
		return Component.class;
	}

	@Override
	public Component label() {
		return this.label;
	}

	public static class LabelOptionBuilderImpl implements LabelOption.Builder {
		private List<Component> label = List.of();

		@Override
		public net.azureaaron.dandelion.systems.LabelOption.Builder label(Component... texts) {
			this.label = List.of(texts);
			return this;
		}

		@Override
		public LabelOption build() {
			Component concatted = MutableComponent.create(PlainTextContents.EMPTY);

			for (Component text : this.label) {
				concatted.getSiblings().add(text);
			}

			return new LabelOptionImpl(concatted);
		}
	}
}
