package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
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
    
    public void tick()
    {
        setInventorySlotContents(2,virtualDisc);
        if(network != null){
            if(network.drive == null){
                network.drive = this;
            } else {
                if(getStackInSlot(0) != null){
                    if(getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                        ItemStorageDisc item = (ItemStorageDisc) getStackInSlot(0).getItem();
                        maxStacks += item.getMaxStackCapacity();
                        maxItems += item.getMaxItemCapacity();
                        network.inventory.updateSizes(this);
                        ItemStack stack = getStackInSlot(0);
                        Object[] nbt = stack.getData().getCompound("Disc").getValues().toArray();
                        for(Object tag : nbt){
                            if(tag instanceof CompoundTag){
                                ItemStack digitizedItem = DiscManipulator.readUnlimitedStackFromNbt(((CompoundTag)tag));
                                if(digitizedItem == null) continue;
                                network.inventory.add(digitizedItem);
                            }
                        }
                        discsUsed.add(stack.copy());
                        setInventorySlotContents(0,null);
                        network.inventory.inventoryChanged();
                    }
                }
            }
        }
    }

    public void removeLastDisc() {
        if(!discsUsed.isEmpty()){
            ItemStack disc = discsUsed.get(0).copy();
            discsUsed.remove(0);
            maxStacks -= Math.min(maxStacks,((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
            maxItems -= Math.min(maxItems,((ItemStorageDisc) disc.getItem()).getMaxItemCapacity());
            CompoundTag nbt = new CompoundTag();
            Object[] V = virtualDisc.getData().getCompound("Disc").getValues().toArray();
            int stacksToRemove = Math.min(virtualDisc.getData().getCompound("Disc").getValues().size(), ((ItemStorageDisc) disc.getItem()).getMaxStackCapacity());
            for (int i = 0; i < stacksToRemove; i++) {
                nbt.putCompound(String.valueOf(i),(CompoundTag) V[i]);
                if(network != null) {
                    network.inventory.remove(i,Integer.MAX_VALUE,false,true);
                }
            }
            disc.getData().putCompound("Disc",nbt);
            disc.stackSize = 1;
            setInventorySlotContents(1,disc);
            if(maxStacks == 0){
                if(network != null) {
                    network.inventory.clear();
                    network.inventory.resetSizes();
                }
                virtualDisc.getData().putCompound("Disc",new CompoundTag());
            }
            if(network != null) {
                network.inventory.inventoryChanged();
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

    public void readFromNBT(CompoundTag compoundTag)
    {
        super.readFromNBT(compoundTag);
        ListTag listTag = compoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < listTag.tagCount(); i++)
        {
            CompoundTag compoundTag1 = (CompoundTag)listTag.tagAt(i);
            int j = compoundTag1.getByte("Slot") & 0xff;
            if(j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(compoundTag1);
            }
        }
        listTag = compoundTag.getList("DiscsUsed");
        discsUsed = new ArrayList<>();
        for(int i = 0; i < listTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)listTag.tagAt(i);
            discsUsed.add(ItemStack.readItemStackFromNbt(CompoundTag1));
        }
        maxStacks = compoundTag.getInteger("MaxStacks");
        maxItems = compoundTag.getInteger("MaxItems");
        virtualDisc.getData().putCompound("Disc", compoundTag.getCompound("Disc"));
    }

    public void writeToNBT(CompoundTag compoundTag)
    {
        super.writeToNBT(compoundTag);
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
        compoundTag.put("Items", listTag);
        listTag = new ListTag();
        for(int i = 0; i < discsUsed.size(); i++)
        {
            if(discsUsed.get(i) != null)
            {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                discsUsed.get(i).writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }
        compoundTag.put("DiscsUsed", listTag);
        compoundTag.put("MaxStacks",new IntTag(maxStacks));
        compoundTag.put("MaxItems",new IntTag(maxItems));
        compoundTag.putCompound("Disc",virtualDisc.getData().getCompound("Disc"));
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortInventory() {
        
    }

    private ItemStack[] contents;
    public ArrayList<ItemStack> discsUsed = new ArrayList<>();
    public ItemStack virtualDisc = (new ItemStack(RetroStorage.virtualDisc,1));
    private int maxStacks = 0;
    private int maxItems = 0;


    public int getMaxStacks() {
        return maxStacks;
    }

    public int getMaxItems() {
        return maxItems;
    }
}
