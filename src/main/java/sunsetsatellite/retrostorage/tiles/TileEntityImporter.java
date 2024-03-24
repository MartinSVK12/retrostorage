package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityImporter extends TileEntityNetworkDevice
    implements IInventory
{
    public TileEntityImporter() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this,this::work,10,true);
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

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        isWhitelist = CompoundTag.getBoolean("isWhitelist");
        enabled = CompoundTag.getBoolean("enabled");
        slot = CompoundTag.getInteger("workSlot");
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

        CompoundTag.putInt("workSlot",slot);
        CompoundTag.putBoolean("isWhitelist",isWhitelist);
        CompoundTag.putBoolean("enabled",enabled);
        CompoundTag.put("Items", listTag);

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

    @Override
    public void tick() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IInventory.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work() {
        if (network != null && network.drive != null && enabled) {
            //RetroStorage.LOGGER.debug(connectedTiles.toString());
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
