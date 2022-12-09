

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;
import net.sunsetsatellite.itemnbt.IDataItem;

import java.util.ArrayList;

public class ItemRecipeDisc extends Item
    implements IDataItem
{

    public ItemRecipeDisc(int i)
    {
        super(i);
    }
    
    
    public Item setMaxStackCapacity(int i) {
    	return this;
    }

    @Override
    public String getDescription(ItemStack stack) {
        CraftingManager crafter = CraftingManager.getInstance();
        ArrayList<?> recipe = DiscManipulator.convertRecipeToArray((stack.getItemData()));
        ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
        return output != null ? "Makes: " + StringTranslate.getInstance().translateNamedKey(output.getItemName()) : "Makes: null";
    }

    @Override
    public int getDescriptionColor(ItemStack stack) {
        return 0xFFFF00FF;
    }

    @Override
    public int getNameColor(ItemStack stack) {
        return -1;
    }
}
