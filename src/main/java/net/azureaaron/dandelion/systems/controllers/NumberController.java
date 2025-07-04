package net.azureaaron.dandelion.systems.controllers;

public sealed interface NumberController<T extends Number> extends Controller<T> permits IntegerController, FloatController {
	T min();
	T max();
	T step();
	boolean slider();

	interface Builder<T extends Number> extends Controller.Builder<T, NumberController<T>> {
		Builder<T> min(T min);

		Builder<T> max(T max);

		Builder<T> range(T min, T max);

		Builder<T> slider(T step);
	}
}
