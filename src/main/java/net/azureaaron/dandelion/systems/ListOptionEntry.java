package net.azureaaron.dandelion.systems;

import java.util.List;

public interface ListOptionEntry<T> extends Option<T> {
    ListOption<T> parent();

    @Override
    default List<OptionFlag> flags() {
        return this.parent().flags();
    }

    @Override
    default boolean modifiable() {
        return this.parent().modifiable();
    }
}
