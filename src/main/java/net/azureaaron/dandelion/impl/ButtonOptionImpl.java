package net.azureaaron.dandelion.impl;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

import net.azureaaron.dandelion.systems.ButtonOption;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ButtonOptionImpl implements ButtonOption {
	@Nullable
	private final Identifier id;
	private final Text name;
	private final List<Text> description;
	private final Text prompt;
	private final Consumer<Screen> action;
    private final List<Text> tags;

    protected ButtonOptionImpl(@Nullable Identifier id, Text name, List<Text> description, List<Text> tags, Text prompt, Consumer<Screen> action) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.prompt = Objects.requireNonNull(prompt, "prompt must not be null");
		this.action = Objects.requireNonNull(action, "action must not be null");
        this.tags = Objects.requireNonNull(tags, "tags must not be null");
    }

	@Override
	@Nullable
	public Identifier id() {
		return this.id;
	}

	@Override
	public Text name() {
		return this.name;
	}

	@Override
	public List<Text> description() {
		return this.description;
	}

	@Override
	public List<Text> tags() {
		return this.tags;
	}

	@Override
	public OptionBinding<Consumer<Screen>> binding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Controller<Consumer<Screen>> controller() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean modifiable() {
		//NYI
		return true;
	}

	@Override
	public List<OptionFlag> flags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<OptionListener<Consumer<Screen>>> listeners() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Consumer<Screen>> type() {
		return (Class<Consumer<Screen>>) this.action.getClass();
	}

	@Override
	public Text prompt() {
		return this.prompt;
	}

	@Override
	public Consumer<Screen> action() {
		return this.action;
	}

	public static class ButtonOptionBuilderImpl implements ButtonOption.Builder {
		private Identifier id = null;
		private Text name = Text.empty();
		private List<Text> description = List.of();
        private List<Text> tags = List.of();
		private Text prompt = Text.of("Execute");
		private Consumer<Screen> action = Consumers.nop();

		@Override
		public Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public Builder name(Text name) {
			this.name = name;
			return this;
		}

		@Override
		public Builder description(Text... texts) {
			this.description = List.of(texts);
			return this;
		}

        @Override
        public Builder tags(Text... tags) {
            this.tags = List.of(tags);
            return this;
        }

        @Override
		public Builder prompt(Text prompt) {
			this.prompt = prompt;
			return this;
		}

		@Override
		public Builder action(Consumer<Screen> action) {
			this.action = action;
			return this;
		}

		@Override
		public ButtonOption build() {
			return new ButtonOptionImpl(this.id, this.name, this.description, this.tags, this.prompt, this.action);
		}
	}
}
