package net.azureaaron.dandelion.api.controllers;

import net.azureaaron.dandelion.impl.controllers.FloatControllerImpl;

public non-sealed interface FloatController extends NumberController<Float> {

	static Builder createBuilder() {
		return new FloatControllerImpl.FloatControllerBuilderImpl();
	}

	interface Builder extends NumberController.Builder<Float> {}
}
