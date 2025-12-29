package net.azureaaron.dandelion.impl;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.systems.LabelOption;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LabelOptionImpl implements LabelOption {
	private final Text label;

	protected LabelOptionImpl(Text label) {
		this.label = Objects.requireNonNull(label, "label must not be null");;
	}

	@Override
	public @Nullable Identifier id() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Text name() {
		return Text.empty();
	}

	@Override
	public List<Text> description() {
		return List.of();
	}

	@Override
	public List<Text> tags() {
		return List.of();
	}

	@Override
	public OptionBinding<Text> binding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Controller<Text> controller() {
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
	public List<OptionListener<Text>> listeners() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<Text> type() {
		return Text.class;
	}

	@Override
	public Text label() {
		return this.label;
	}

	public static class LabelOptionBuilderImpl implements LabelOption.Builder {
		private List<Text> label = List.of();

		@Override
		public Builder label(Text... texts) {
			this.label = List.of(texts);
			return this;
		}

		@Override
		public LabelOption build() {
			Text concatted = MutableText.of(PlainTextContent.EMPTY);

			for (Text text : this.label) {
				concatted.getSiblings().add(text);
			}

			return new LabelOptionImpl(concatted);
		}
	}
}
