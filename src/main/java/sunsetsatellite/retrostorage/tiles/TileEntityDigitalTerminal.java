

package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.sunsetutils.util.TickTimer;


public class TileEntityDigitalTerminal extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityDigitalTerminal()
    {
        contents = new ItemStack[37];
        saveTimer = new TickTimer(this,"save",40,true);

    }

    public int getSizeInventory()
    {
        return 37;
    }

    public ItemStack getStackInSlot(int i)
    {
        if(i > contents.length){
            return null;
        }
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

    public void save(){
        if(network != null && network.drive != null){
            DiscManipulator.saveDisc(network.drive.virtualDisc, network.inventory);
        }
    }

    public void updateEntity()
    {
        saveTimer.tick();
        if(network != null && network.drive != null){
            contents = network.inventory.inventoryContents;
            setInventorySlotContents(0, network.drive.virtualDisc);
            this.pages = (network.inventory.getLastOccupiedStack()/36)+1;
        } else {
            contents = new ItemStack[37];
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        /*if(i == 0 && itemstack == null){
            DiscManipulator.clearDigitalInv(this);
        }*/
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
        return "Digital Terminal";
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
        if(network != null && network.drive != null){
            j = network.drive.virtualDisc.getData().getCompound("disc").getValues().size();
        }
        return j;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 8000D;
    }

    private ItemStack[] contents;
    private TickTimer saveTimer;
    public int page = 1;
    public int pages = 1;
}
