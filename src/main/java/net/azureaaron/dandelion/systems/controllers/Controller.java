package net.azureaaron.dandelion.systems.controllers;

public sealed interface Controller<T> permits BooleanController, ColourController, EnumController, NumberController, ItemController, StringController {

	interface Builder<T, I extends Controller<T>> {
		I build();
	}
}
