package net.sunsetsatellite.itemnbt;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public interface IDataItem {
    String getDescription(ItemStack stack);
    int getDescriptionColor(ItemStack stack);
    int getNameColor(ItemStack stack);
}
