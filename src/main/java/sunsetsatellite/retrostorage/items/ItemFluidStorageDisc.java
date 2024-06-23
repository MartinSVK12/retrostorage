package sunsetsatellite.retrostorage.items;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class ItemFluidStorageDisc extends Item implements ICustomDescription {

    public ItemFluidStorageDisc(String name, int id, int maxStackCapacity, int maxItemCapacity) {
        super(name, id);
        this.maxStackCapacity = maxStackCapacity;
        this.maxItemCapacity = maxItemCapacity;
    }

    public int getMaxStackCapacity() {
        return maxStackCapacity;
    }

    public Item setMaxStackCapacity(int i) {
        maxStackCapacity = i;
        return this;
    }

    public int getMaxItemCapacity() {
        return maxItemCapacity;
    }

    public void setMaxItemCapacity(int maxItemCapacity) {
        this.maxItemCapacity = maxItemCapacity;
    }

    private int maxStackCapacity;
    private int maxItemCapacity;

    @Override
    public String getDescription(ItemStack itemStack) {
        return TextFormatting.MAGENTA + "" + itemStack.getData().getCompound("Disc").getValues().size() + " entries out of " + maxStackCapacity;
    }


}