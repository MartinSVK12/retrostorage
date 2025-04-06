package sunsetsatellite.retrostorage.items;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class ItemStorageDisc extends Item implements ICustomDescription {
    public ItemStorageDisc(String translationKey, String namespaceId, int id, int maxStackCapacity, int maxItemCapacity) {
        super(translationKey, namespaceId, id);
        this.maxStackCapacity = maxStackCapacity;
        this.maxItemCapacity = maxItemCapacity;
        setMaxStackSize(1);
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
    public String getPersistentDescription(ItemStack stack) {
        return TextFormatting.MAGENTA + "" + stack.getData().getCompound("Disc").getValues().size() + " entries out of " + maxStackCapacity;
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return "";
    }


    @Override
    public CompoundTag getDefaultTag() {
        return super.getDefaultTag();
    }
}