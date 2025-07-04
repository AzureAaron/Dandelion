package net.azureaaron.dandelion.systems.controllers;

import net.azureaaron.dandelion.impl.controllers.ItemControllerImpl;
import net.minecraft.item.Item;

public non-sealed interface ItemController extends Controller<Item> {

	static Builder createBuilder() {
		return new ItemControllerImpl.ItemControllerBuilderImpl();
	}

	interface Builder extends Controller.Builder<Item, ItemController> {}
}
