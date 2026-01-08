package net.azureaaron.dandelion.api;

public interface OptionListener<T> {

	void onUpdate(Option<T> option, UpdateType type);

	enum UpdateType {
		VALUE_CHANGE,
		YACL_UNSUPPORTED;
	}
}
