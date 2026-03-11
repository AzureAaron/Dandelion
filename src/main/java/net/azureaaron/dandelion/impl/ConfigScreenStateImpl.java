package net.azureaaron.dandelion.impl;

import net.azureaaron.dandelion.api.ConfigScreenState;
import net.azureaaron.dandelion.impl.moulconfig.MoulConfigScreenState;

public class ConfigScreenStateImpl implements ConfigScreenState {
	public int categoriesHash;

	public MoulConfigScreenState moulConfig = new MoulConfigScreenState();
}
