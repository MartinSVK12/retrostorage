package sunsetsatellite.retrostorage.items;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.retrostorage.RetroStorage;

public class ItemRecipeDisc extends Item implements ICustomDescription {

    public ItemRecipeDisc(String name, int id) {
        super(name, id);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "";
    }

    @Override
    public String getPersistentDescription(ItemStack stack) {
        ItemStack result = RetroStorage.findRecipeResultFromNBT(stack.getData().getCompound("recipe"));
        if (result != null) {
            return TextFormatting.MAGENTA + "Output: " + result.stackSize + "x " + result.getDisplayName() + TextFormatting.WHITE;
        }
        return TextFormatting.GRAY + "Empty" + TextFormatting.WHITE;
    }

}
