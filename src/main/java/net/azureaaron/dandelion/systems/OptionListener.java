package net.azureaaron.dandelion.systems;

public interface OptionListener<T> {

	void onUpdate(Option<T> option, UpdateType type);

	enum UpdateType {
		VALUE_CHANGE,
		YACL_UNSUPPORTED;
	}
}
