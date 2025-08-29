package net.azureaaron.dandelion.impl;

import net.azureaaron.dandelion.systems.ListOption;
import net.azureaaron.dandelion.systems.ListOptionEntry;
import net.azureaaron.dandelion.systems.OptionBinding;
import net.azureaaron.dandelion.systems.OptionListener;
import net.azureaaron.dandelion.systems.controllers.Controller;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListOptionEntryImpl<T> implements ListOptionEntry<T> {
    private final ListOptionImpl<T> group;
    private T value;
    private final OptionBinding<T> binding;
    private final Controller<T> controller;

    public ListOptionEntryImpl(ListOptionImpl<T> group, T initialValue, Controller<T> controller) {
        this.group = group;
        this.value = initialValue;
        this.binding = new OptionBindingImpl<>(
                initialValue,
                () -> this.value,
                newValue -> {
                    this.value = newValue;
                    this.group.listeners().forEach(listener -> listener.onUpdate(this.group, OptionListener.UpdateType.VALUE_CHANGE));
                }
        );
        this.controller = controller;
        // Types are checked in the ListOptionImpl constructor. We don't check types here because this would run before other fields in ListOptionImpl are initialized.
    }

    @Override
    public ListOption<T> parent() {
        return this.group;
    }

    @Override
    public @Nullable Identifier id() {
        return this.group.id().withSuffixedPath("/" + this.group.options().indexOf(this));
    }

    @Override
    public Text name() {
        return this.group.name();
    }

    @Override
    public List<Text> description() {
        return this.group.description();
    }

    @Override
    public List<Text> tags() {
        return this.group.tags();
    }

    @Override
    public OptionBinding<T> binding() {
        return this.binding;
    }

    @Override
    public Controller<T> controller() {
        return this.controller;
    }

    @Override
    public List<OptionListener<T>> listeners() {
        return List.of();
    }

    @Override
    public Class<T> type() {
        return this.group.entryType();
    }
}
