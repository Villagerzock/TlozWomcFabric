package net.villagerzock.tlozwomcfabric.client;

import net.minecraft.item.ItemStack;

public interface QuickPickerResult {
     void run(ItemStack item, QuickPickerCloseReason reason);
}
