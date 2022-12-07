package net.sunsetsatellite.retrostorage;

import net.minecraft.src.ItemStack;

public interface IDataItem {
    String getDescription(ItemStack stack);
}
