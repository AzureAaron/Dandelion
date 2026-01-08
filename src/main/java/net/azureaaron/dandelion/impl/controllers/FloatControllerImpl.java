package net.azureaaron.dandelion.impl.controllers;

import java.util.Objects;

import net.azureaaron.dandelion.api.controllers.FloatController;

public class FloatControllerImpl implements FloatController {
	private final float min;
	private final float max;
	private final float step;
	private final boolean slider;

	protected FloatControllerImpl(float min, float max, float step, boolean slider) {
		this.min = min;
		this.max = max;
		this.step = step;
		this.slider = slider;
	}

	@Override
	public Float min() {
		return this.min;
	}

	@Override
	public Float max() {
		return this.max;
	}

	@Override
	public Float step() {
		return this.step;
	}

	@Override
	public boolean slider() {
		return this.slider;
	}

	public static class FloatControllerBuilderImpl implements FloatController.Builder {
		private float min = Float.MIN_VALUE;
		private float max = Float.MAX_VALUE;
		private float step = 1f;
		private boolean slider = false;

		@Override
		public FloatController.Builder min(Float min) {
			this.min = Objects.requireNonNull(min, "min must not be null");
			return this;
		}

		@Override
		public FloatController.Builder max(Float max) {
			this.max = Objects.requireNonNull(max, "max must not be null");
			return this;
		}

		@Override
		public FloatController.Builder range(Float min, Float max) {
			this.min = Objects.requireNonNull(min, "min must not be null");
			this.max = Objects.requireNonNull(max, "max must not be null");
			return this;
		}

		@Override
		public FloatController.Builder slider(Float step) {
			this.step = Objects.requireNonNull(step, "step must not be null");
			this.slider = true;
			return this;
		}

		@Override
		public FloatController build() {
			return new FloatControllerImpl(this.min, this.max, this.step, this.slider);
		}
	}
}
