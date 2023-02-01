package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.RecipeTask;
import sunsetsatellite.retrostorage.util.Task;
import sunsetsatellite.retrostorage.util.TickTimer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityImporter extends TileEntityNetworkDevice
    implements IInventory
{
    public TileEntityImporter() {
        contents = new ItemStack[9];
        try {
            this.workTimer = new TickTimer(this,this.getClass().getMethod("work"),10,true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory()-1; i++) {
            if(getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
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

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();

    }

    public int getInventorySlotContainItem(int itemID, int itemDamage) {
        for(int i2 = 0; i2 < this.contents.length; ++i2) {
            if(this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getMetadata() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Importer";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        isWhitelist = nbttagcompound.getBoolean("isWhitelist");
        enabled = nbttagcompound.getBoolean("enabled");
        slot = nbttagcompound.getInteger("workSlot");
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

        nbttagcompound.setInteger("workSlot",slot);
        nbttagcompound.setBoolean("isWhitelist",isWhitelist);
        nbttagcompound.setBoolean("enabled",enabled);
        nbttagcompound.setTag("Items", nbttaglist);

    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void updateEntity() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IInventory.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work() {
        if (network != null && network.drive != null && enabled) {
            //RetroStorage.LOGGER.info(connectedTiles.toString());
            for(TileEntity tile : connectedTiles.values()){
                if(tile != null){
                    IInventory inv = (IInventory) tile;
                    if(slot == -1){
                        for (int i = 0; i < inv.getSizeInventory(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            if(stack != null && ((getInventorySlotContainItem(stack.itemID,stack.getMetadata()) != -1 && isWhitelist) || (getInventorySlotContainItem(stack.itemID,stack.getMetadata()) == -1 && !isWhitelist))){
                                if(network.inventory.addItemStackToInventory(stack)){
                                    inv.setInventorySlotContents(i,null);
                                    DiscManipulator.saveDisc(network.drive.virtualDisc,network.inventory);
                                }
                            }
                        }
                    } else {
                        if(slot >= inv.getSizeInventory()){
                            return;
                        }
                        ItemStack stack = inv.getStackInSlot(slot);
                        if(stack != null && ((getInventorySlotContainItem(stack.itemID,stack.getMetadata()) != -1 && isWhitelist) || (getInventorySlotContainItem(stack.itemID,stack.getMetadata()) == -1 && !isWhitelist))){
                            if(network.inventory.addItemStackToInventory(stack)){
                                inv.setInventorySlotContents(slot,null);
                                DiscManipulator.saveDisc(network.drive.virtualDisc,network.inventory);
                            }
                        }
                    }
                }
            }
        }
    }

    private ItemStack[] contents;
    public TickTimer workTimer;
    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public HashMap<String,TileEntity> connectedTiles = new HashMap<>();
}
