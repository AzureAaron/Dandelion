package net.azureaaron.dandelion.api.controllers;

import java.awt.Color;

import net.azureaaron.dandelion.impl.controllers.ColourControllerImpl;

public non-sealed interface ColourController extends Controller<Color> {

	static Builder createBuilder() {
		return new ColourControllerImpl.ColourControllerBuilderImpl();
	}

	boolean hasAlpha();

	interface Builder extends Controller.Builder<Color, ColourController> {
		Builder hasAlpha(boolean hasAlpha);
	}
}
