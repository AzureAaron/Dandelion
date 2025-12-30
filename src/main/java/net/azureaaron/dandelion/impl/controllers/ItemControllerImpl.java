package net.azureaaron.dandelion.impl.controllers;

import net.azureaaron.dandelion.api.controllers.ItemController;

public class ItemControllerImpl implements ItemController {
	protected ItemControllerImpl() {}

	public static class ItemControllerBuilderImpl implements ItemController.Builder {
		@Override
		public ItemController build() {
			return new ItemControllerImpl();
		}
	}
}
