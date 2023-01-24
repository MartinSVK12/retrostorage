

package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.TickTimer;

public class TileEntityDigitalTerminal extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityDigitalTerminal()
    {
        contents = new ItemStack[37];
        try {
            saveTimer = new TickTimer(this,this.getClass().getMethod("save"),40,true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSizeInventory()
    {
        return 37;
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

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getAmountOfUsedSlots(){
        int j = 0;
        if(network != null && network.drive != null){
            j = network.drive.virtualDisc.tag.func_28110_c().size();
        }
        return j;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 8000D;
    }

    private ItemStack[] contents;
    private TickTimer saveTimer;
    public int page = 1;
    public int pages = 1;
}
