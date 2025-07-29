package net.azureaaron.dandelion.moulconfig.editor;

import org.jetbrains.annotations.NotNull;

import io.github.notenoughupdates.moulconfig.common.IItemStack;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.component.ItemStackComponent;
import io.github.notenoughupdates.moulconfig.gui.component.RowComponent;
import io.github.notenoughupdates.moulconfig.gui.component.TextFieldComponent;
import io.github.notenoughupdates.moulconfig.gui.editors.ComponentEditor;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigPlatform;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class DandelionItemEditor extends ComponentEditor {
	private final GuiComponent component;
	/**
	 * This saves the last input, this is because the text field component expects that the value that gets input
	 * will be written to the backing option/field immediately, and then following that it will pull the field value into the
	 * text box right after. Since we only save something if it is valid, we need to store the last text and then feed that
	 * back to the text field to ensure the input does not get stuck at the default value (minecraft:air)/become unusable.
	 */
	private String lastText = "";

	public DandelionItemEditor(ProcessedOption option) {
		super(option);

		GetSetter<IItemStack> itemStackGetter = new GetSetter<>() {
			@Override
			public IItemStack get() {
				return MoulConfigPlatform.wrap(new ItemStack((ItemConvertible) option.get()));
			}

			@Override
			public void set(IItemStack newValue) {
				throw new UnsupportedOperationException();
			}
		};
		GetSetter<String> itemStackSetter = new GetSetter<>() {
			@Override
			public String get() {
				return DandelionItemEditor.this.lastText;
			}

			@Override
			public void set(String newValue) {
				DandelionItemEditor.this.lastText = newValue;
				Identifier id = Identifier.tryParse(newValue);

				//Only update the value when a valid item is has been typed in
				if (id != null) {
					option.set(Registries.ITEM.get(id));
				}
			}
		};

		this.lastText = Registries.ITEM.getId((Item) option.get()).toString();
		this.component = this.wrapComponent(
				new RowComponent(
						new ItemStackComponent(itemStackGetter),
						new TextFieldComponent(
								itemStackSetter,
								49,
								() -> true,
								"Item ID",
								IMinecraft.INSTANCE.getDefaultFontRenderer()
								)
						)
				);
	}

	@Override
	@NotNull
	public GuiComponent getDelegate() {
		return this.component;
	}
}
