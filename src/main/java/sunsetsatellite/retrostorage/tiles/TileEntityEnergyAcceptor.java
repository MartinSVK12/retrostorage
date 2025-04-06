package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.energy.simple.impl.TileEntityEnergyDevice;

public class TileEntityEnergyAcceptor extends TileEntityEnergyDevice implements Container {

    public ItemStack[] contents;

    public TileEntityEnergyAcceptor() {
        capacity = 10000;
        maxProvide = 0;
        maxReceive = 1000;
        energy = 0;
        contents = new ItemStack[2];
    }

    @Override
    public void tick() {
        if (worldObj != null && worldObj.isClientSide) return;
        super.tick();
    }

    @Override
    public int getContainerSize()
    {
        return contents.length;
    }

    @Override
    public ItemStack getItem(int i)
    {
        return contents[i];
    }

    @Override
    public ItemStack removeItem(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                setChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            setChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getMaxStackSize())
        {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.energyAcceptor";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readFromNBT(CompoundTag tag)
    {
        super.readFromNBT(tag);
        ListTag ListTag = tag.getList("Items");
        contents = new ItemStack[getContainerSize()];
        for(int i = 0; i < ListTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)ListTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if(j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag)
    {
        super.writeToNBT(tag);
        ListTag ListTag = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {

                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(CompoundTag1);
                ListTag.addTag(CompoundTag1);
            }
        }
        tag.put("Items", ListTag);
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player entityplayer)
    {
        if(worldObj.getTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

    }
}
