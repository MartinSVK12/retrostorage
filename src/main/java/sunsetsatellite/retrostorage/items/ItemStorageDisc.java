package sunsetsatellite.retrostorage.items;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class ItemStorageDisc extends Item implements ICustomDescription
{

    public ItemStorageDisc(String name, int id, int maxStackCapacity) {
        super(name, id);
        this.maxStackCapacity = maxStackCapacity;
    }

    public int getMaxStackCapacity() {
        return maxStackCapacity;
    }

    public Item setMaxStackCapacity(int i) {
        maxStackCapacity = i;
        return this;
    }


    public int maxStackCapacity;

    @Override
    public String getDescription(ItemStack itemStack) {
        return TextFormatting.MAGENTA+""+itemStack.getData().getCompound("disc").getValues().size()+" entries out of "+maxStackCapacity;
    }
}