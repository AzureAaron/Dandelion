package net.azureaaron.dandelion.impl.controllers;

import java.util.Objects;

import net.azureaaron.dandelion.systems.controllers.BooleanController;

public class BooleanControllerImpl implements BooleanController {
	private final BooleanStyle style;
	private final boolean coloured;

	protected BooleanControllerImpl(BooleanStyle style, boolean coloured) {
		this.style = Objects.requireNonNull(style, "style must not be null");
		this.coloured = coloured;
	}

	@Override
	public BooleanStyle style() {
		return this.style;
	}

	@Override
	public boolean coloured() {
		return this.coloured;
	}

	public static class BooleanControllerBuilderImpl implements BooleanController.Builder {
		private BooleanStyle style = BooleanStyle.ON_OFF;
		private boolean coloured = false;

		@Override
		public Builder booleanStyle(BooleanStyle style) {
			this.style = style;
			return this;
		}

		@Override
		public Builder coloured(boolean coloured) {
			this.coloured = coloured;
			return this;
		}

		@Override
		public BooleanController build() {
			return new BooleanControllerImpl(this.style, this.coloured);
		}
	}
}
