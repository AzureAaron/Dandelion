package net.azureaaron.dandelion.api.controllers;

import net.azureaaron.dandelion.impl.controllers.ItemControllerImpl;
import net.minecraft.world.item.Item;

public non-sealed interface ItemController extends Controller<Item> {

	static Builder createBuilder() {
		return new ItemControllerImpl.ItemControllerBuilderImpl();
	}

	interface Builder extends Controller.Builder<Item, ItemController> {}
}
