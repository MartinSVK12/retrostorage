package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.util.ArrayList;

public class TileEntityDiscDrive extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityDiscDrive()
    {
        contents = new ItemStack[3];
    }

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
        setInventorySlotContents(2,virtualDisc);
        if(network != null){
            if(network.drive == null){
                network.drive = this;
            }
            else{
                if(getStackInSlot(0) != null){
                    if(getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                        ItemStorageDisc item = (ItemStorageDisc) getStackInSlot(0).getItem();
                        virtualDriveMaxStacks += item.getMaxStackCapacity();
                        ItemStack stack = getStackInSlot(0);
                        Object[] nbt = stack.tag.func_28110_c().toArray();
                        for(Object tag : nbt){
                            if(tag instanceof NBTTagCompound){
                                //RetroStorage.printCompound((NBTTagCompound) tag);
                                ItemStack digitizedItem = new ItemStack(((NBTTagCompound)tag));
                                //RetroStorage.LOGGER.info(String.format("%d %d %d",digitizedItem.itemID,digitizedItem.stackSize,digitizedItem.getMetadata()));
                                network.inventory.addItemStackToInventory(digitizedItem);
                            }
                        }
                        discsUsed.add(stack.copy());
                        setInventorySlotContents(0,null);
                        DiscManipulator.saveDisc(virtualDisc, network.inventory);
                    }
                }
            }
        }
    }

    public void removeLastDisc() {
        if(network != null){
            if(discsUsed.size() > 0){
                ItemStack disc = discsUsed.get(0).copy();
                discsUsed.remove(0);
                virtualDriveMaxStacks -= Math.min(virtualDriveMaxStacks,((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
                NBTTagCompound nbt = new NBTTagCompound();
                Object[] V = virtualDisc.tag.func_28110_c().toArray();
                int stacksToRemove = Math.min(virtualDisc.tag.func_28110_c().size(), ((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
                for (int i = 0; i < stacksToRemove; i++) {
                    nbt.setCompoundTag(String.valueOf(i+1),(NBTTagCompound) V[i]);
                    network.inventory.setInventorySlotContents(i+1,null);
                }
                disc.tag = nbt;
                disc.stackSize = 1;
                setInventorySlotContents(1,disc);
                DiscManipulator.saveDisc(virtualDisc, network.inventory);
            }
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
    
    public void onInventoryChanged() {
    	super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Disc Drive";
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
        nbttaglist = nbttagcompound.getTagList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            discsUsed.add(new ItemStack(nbttagcompound1));
        }
        virtualDriveMaxStacks = nbttagcompound.getInteger("MaxStacks");
        virtualDisc.tag = nbttagcompound.getCompoundTag("VirtualDisc");
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
        nbttaglist = new NBTTagList();
        for(int i = 0; i < discsUsed.size(); i++)
        {
            if(discsUsed.get(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                discsUsed.get(i).writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("DiscsUsed", nbttaglist);
        nbttagcompound.setTag("MaxStacks",new NBTTagInt(virtualDriveMaxStacks));
        nbttagcompound.setTag("VirtualDisc",virtualDisc.tag);
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

    private ItemStack contents[];
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    public ItemStack virtualDisc = (new ItemStack(RetroStorage.virtualDisc,1));
    public int virtualDriveMaxStacks = 0;
}
