package net.azureaaron.dandelion.impl.controllers;

import net.azureaaron.dandelion.systems.controllers.ColourController;

public class ColourControllerImpl implements ColourController {
	private final boolean hasAlpha;

	protected ColourControllerImpl(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	@Override
	public boolean hasAlpha() {
		return this.hasAlpha;
	}

	public static class ColourControllerBuilderImpl implements ColourController.Builder {
		private boolean hasAlpha = false;

		@Override
		public Builder hasAlpha(boolean hasAlpha) {
			this.hasAlpha = hasAlpha;
			return this;
		}

		@Override
		public ColourController build() {
			return new ColourControllerImpl(this.hasAlpha);
		}
	}
}
