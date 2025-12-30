package net.azureaaron.dandelion.api.controllers;

import net.azureaaron.dandelion.impl.controllers.StringControllerImpl;

public non-sealed interface StringController extends Controller<String> {

	static Builder createBuilder() {
		return new StringControllerImpl.StringControllerBuilderImpl();
	}

	interface Builder extends Controller.Builder<String, StringController> {}
}
