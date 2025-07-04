package net.azureaaron.dandelion.impl.controllers;

import java.util.Objects;

import net.azureaaron.dandelion.systems.controllers.IntegerController;

public class IntegerControllerImpl implements IntegerController {
	private final int min;
	private final int max;
	private final int step;
	private final boolean slider;

	protected IntegerControllerImpl(int min, int max, int step, boolean slider) {
		this.min = min;
		this.max = max;
		this.step = step;
		this.slider = slider;
	}

	@Override
	public Integer min() {
		return this.min;
	}

	@Override
	public Integer max() {
		return this.max;
	}

	@Override
	public Integer step() {
		return this.step;
	}

	@Override
	public boolean slider() {
		return this.slider;
	}

	public static class IntegerControllerBuilderImpl implements IntegerController.Builder {
		private int min = Integer.MIN_VALUE;
		private int max = Integer.MAX_VALUE;
		private int step = 1;
		private boolean slider = false;

		@Override
		public Builder min(Integer min) {
			return this;
		}

		@Override
		public Builder max(Integer max) {
			this.max = Objects.requireNonNull(max, "max must not be null");
			return this;
		}

		@Override
		public Builder range(Integer min, Integer max) {
			this.min = Objects.requireNonNull(min, "min must not be null");
			this.max = Objects.requireNonNull(max, "max must not be null");
			return this;
		}

		@Override
		public Builder slider(Integer step) {
			this.step = Objects.requireNonNull(step, "step must not be null");
			this.slider = true;
			return this;
		}

		@Override
		public IntegerController build() {
			return new IntegerControllerImpl(this.min, this.max, this.step, this.slider);
		}
	}
}
