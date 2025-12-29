package net.azureaaron.dandelion.impl;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jspecify.annotations.Nullable;

import net.azureaaron.dandelion.systems.ButtonOption;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionFlag;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ButtonOptionImpl implements ButtonOption {
	private final @Nullable Identifier id;
	private final Component name;
	private final List<Component> description;
	private final Component prompt;
	private final Consumer<Screen> action;
	private final List<Component> tags;

	protected ButtonOptionImpl(@Nullable Identifier id, Component name, List<Component> description, List<Component> tags, Component prompt, Consumer<Screen> action) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.prompt = Objects.requireNonNull(prompt, "prompt must not be null");
		this.action = Objects.requireNonNull(action, "action must not be null");
		this.tags = Objects.requireNonNull(tags, "tags must not be null");
	}

	@Override
	public @Nullable Identifier id() {
		return this.id;
	}

	@Override
	public Component name() {
		return this.name;
	}

	@Override
	public List<Component> description() {
		return this.description;
	}

	@Override
	public List<Component> tags() {
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
	public Component prompt() {
		return this.prompt;
	}

	@Override
	public Consumer<Screen> action() {
		return this.action;
	}

	public static class ButtonOptionBuilderImpl implements ButtonOption.Builder {
		private @Nullable Identifier id = null;
		private Component name = Component.empty();
		private List<Component> description = List.of();
		private List<Component> tags = List.of();
		private Component prompt = Component.nullToEmpty("Execute");
		private Consumer<Screen> action = Consumers.nop();

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder name(Component name) {
			this.name = name;
			return this;
		}

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder description(Component... texts) {
			this.description = List.of(texts);
			return this;
		}

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder tags(Component... tags) {
			this.tags = List.of(tags);
			return this;
		}

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder prompt(Component prompt) {
			this.prompt = prompt;
			return this;
		}

		@Override
		public net.azureaaron.dandelion.systems.ButtonOption.Builder action(Consumer<Screen> action) {
			this.action = action;
			return this;
		}

		@Override
		public ButtonOption build() {
			return new ButtonOptionImpl(this.id, this.name, this.description, this.tags, this.prompt, this.action);
		}
	}
}
