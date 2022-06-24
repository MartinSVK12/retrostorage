// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Set;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityDiscDrive extends TileEntityStorage
    implements IInventory
{

    public TileEntityDiscDrive()
    {
        contents = new ItemStack[3];
        //createVirtualDisc();
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
    
    
    
    /*public void createVirtualDisc() {
        NBTTagCompound vDiscNBT = new NBTTagCompound();
        int j = 0;
        int k = 0;
    	for(int i = 0; i < getSizeInventory()-2;i++) {
    		if(getStackInSlot(i) != null) {
    			if (getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
    				ItemStack disc = getStackInSlot(i);
    				Object[] discNBT = disc.getItemData().getValues().toArray();
                    j += ((ItemStorageDisc)disc.getItem()).getMaxStackCapacity();
                    for (int i1 = 0; i1 < discNBT.length; i1++) {
                        Object o = discNBT[i1];
                        vDiscNBT.setCompoundTag(String.valueOf(k), (NBTTagCompound) o);
                        k++;
                    }
    			}
    		}
    	}
        virtualDriveMaxStacks = j;
        virtualDisc.setItemData(vDiscNBT);
    	setInventorySlotContents(9, virtualDisc);
    }*/
    
    /*public void updateDiscs() {
        Object[] vDiscNBT = virtualDisc.getItemData().getValues().toArray();
        for(int i = 0; i < getSizeInventory()-2;i++) {
            if (getStackInSlot(i) != null) {
                if (getStackInSlot(i).getItem() instanceof ItemStorageDisc) {
                    getStackInSlot(i).setItemData(new NBTTagCompound());
                }
            }
        }
        if(vDiscNBT.length > 0) {
            int discId = 0;
            int itemsSaved = 0;
            while(discId < 8){
                ItemStack disc = getStackInSlot(discId);
                NBTTagCompound discNBT = new NBTTagCompound();
                if (disc != null) {
                    if (disc.getItem() instanceof ItemStorageDisc) {
                        int i;
                        int j = 0;
                        for(i = 0; i < Math.min(vDiscNBT.length,((ItemStorageDisc) disc.getItem()).getMaxStackCapacity()); i++){
                            if(vDiscNBT.length-1 < i+itemsSaved){
                                break;
                            }
                            discNBT.setCompoundTag(String.valueOf(i+itemsSaved), (NBTTagCompound) vDiscNBT[i+itemsSaved]);
                            j++;
                        }
                        itemsSaved += j;
                        if(itemsSaved < vDiscNBT.length){
                            disc.setItemData(discNBT);
                            discId++;
                        } else if (itemsSaved == vDiscNBT.length){
                            disc.setItemData(discNBT);
                            break;
                        }
                    } else {
                        discId++;
                    }
                } else {
                    discId++;
                }
            }
        }
    }*/
    
    public void updateEntity()
    {
        if (getStackInSlot(2) != null){
            if (getStackInSlot(2).getItem() == mod_RetroStorage.virtualDisc){
                virtualDisc = getStackInSlot(2);
            }
        }
        setInventorySlotContents(2,virtualDisc);
        if (getStackInSlot(0) != null){
            if (getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                virtualDriveMaxStacks += ((ItemStorageDisc) getStackInSlot(0).getItem()).getMaxStackCapacity();
                Object[] vDiscData = virtualDisc.getItemData().getValues().toArray();
                NBTTagCompound vDiscNBT = virtualDisc.getItemData();
                ItemStack disc = getStackInSlot(0);
                Object[] discNBT = disc.getItemData().getValues().toArray();
                for (int i1 = 0; i1 < discNBT.length; i1++) {
                    Object o = discNBT[i1];
                    vDiscNBT.setCompoundTag(String.valueOf(vDiscData.length+i1+1), (NBTTagCompound) o);
                }
                setInventorySlotContents(0,new ItemStack(mod_RetroStorage.blankDisc,1));
                discsUsed.add(disc);
            }
        }
        if (getStackInSlot(1) != null){
            if (getStackInSlot(1).getItem() == mod_RetroStorage.blankDisc && getStackInSlot(1).stackSize == 1){
                if(discsUsed.size() > 0){
                    ItemStack disc = discsUsed.get(0);
                    discsUsed.remove(0);
                    virtualDriveMaxStacks -= Math.min(virtualDriveMaxStacks,((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
                    setInventorySlotContents(1,disc);
                    NBTTagCompound discData = new NBTTagCompound();
                    Object[] virtualDiscDataV = virtualDisc.getItemData().getValues().toArray();
                    Object[] virtualDiscDataK = virtualDisc.getItemData().getKeys().toArray();
                    int j = Math.min(virtualDisc.getItemData().size(), ((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
                    for(int i = 0; i < j; i++){
                        discData.setCompoundTag(String.valueOf(i+1), (NBTTagCompound) virtualDiscDataV[i]);
                        virtualDisc.getItemData().removeTag((String) virtualDiscDataK[i]);
                    }
                    disc.setItemData(discData);
                }
            }
        }
        /*updateDiscs();
        createVirtualDisc();*/
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
    private ArrayList<ItemStack> discsUsed = new ArrayList<>();
    public ItemStack virtualDisc = (new ItemStack(mod_RetroStorage.virtualDisc));
    public int virtualDriveMaxStacks = 0;
}
