package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityDigitalChest extends TileEntity
    implements IInventory
{
    public TileEntityDigitalChest()
    {
        contents = new ItemStack[37];
    }

    public int page = 1;
    public int pages = 1;

    public int getSizeInventory()
    {
        return contents.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void updateEntity()
    {
        if(getStackInSlot(0) != null){
            this.pages = 36;
            this.page = getStackInSlot(0).getData().getCompound("disc").getValues().size();
        } else {
            this.pages = 0;
            this.page = 0;
        }
        /*if(getStackInSlot(0) != null){
            this.pages = ((int) Math.floor((double) getStackInSlot(0).getData().getCompound("disc").func_28110_c().size()/(getSizeInventory()-1))) + 1;
        } else {
            this.page = 1;
            this.pages = 1;
        }*/
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Digital Chest";
    }

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < listTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }

    }

    public void writeToNBT(CompoundTag CompoundTag)
    {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.put("Items", listTag);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getAmountOfUsedSlots(){
        int j = 0;
        for (ItemStack content : contents) {
            if (content != null) {
                j++;
            }
        }
        return j;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack[] contents;
}
