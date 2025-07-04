package net.azureaaron.dandelion.systems.controllers;

import net.azureaaron.dandelion.impl.controllers.BooleanControllerImpl;

public non-sealed interface BooleanController extends Controller<Boolean> {

	static Builder createBuilder() {
		return new BooleanControllerImpl.BooleanControllerBuilderImpl();
	}

	BooleanStyle style();
	boolean coloured();

	interface Builder extends Controller.Builder<Boolean, BooleanController> {
		Builder booleanStyle(BooleanStyle style);

		Builder coloured(boolean coloured);
	}

	enum BooleanStyle {
		YES_NO,
		ON_OFF,
		TRUE_FALSE;
	}
}
