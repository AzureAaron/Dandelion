package net.azureaaron.dandelion.impl.controllers;

import net.azureaaron.dandelion.api.controllers.StringController;

public class StringControllerImpl implements StringController {
	protected StringControllerImpl() {}

	public static class StringControllerBuilderImpl implements StringController.Builder {
		@Override
		public StringController build() {
			return new StringControllerImpl();
		}
	}
}
