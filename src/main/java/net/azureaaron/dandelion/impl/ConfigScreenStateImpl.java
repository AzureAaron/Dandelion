package net.azureaaron.dandelion.impl;

import net.azureaaron.dandelion.api.ConfigScreenState;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigScreenState;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ConfigScreenStateImpl implements ConfigScreenState {
	public int categoriesHash;

	public MoulConfigScreenState moulConfig = new MoulConfigScreenState();

	public static ConfigScreenStateImpl getFromSupplier(@Nullable Supplier<@Nullable ConfigScreenState> supplier) {
		if (supplier == null) return new ConfigScreenStateImpl();
		ConfigScreenStateImpl configScreenState = (ConfigScreenStateImpl) supplier.get();
		if (configScreenState == null) configScreenState = new ConfigScreenStateImpl();
		return configScreenState;
	}
}
