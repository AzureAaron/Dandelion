package net.azureaaron.dandelion.platform;

import net.minecraft.client.resources.language.I18n;

public enum ConfigType {
	YACL,
	MOUL_CONFIG;

	@Override
	public String toString() {
		return I18n.get("text.dandelion.configType." + this.name());
	}
}
