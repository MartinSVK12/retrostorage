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

public class TileEntityExporter extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityExporter() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this,this::work,10,true);
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(ItemStack stack : contents) {
            if(stack != null) {
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
        return "Exporter";
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
            for(TileEntity tile : connectedTiles.values()){
                if(tile != null){
                    IInventory inv = (IInventory) tile;
                    if(slot == -1){
                        int availableSlot = -1;
                        for (int i = 0; i < inv.getSizeInventory(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            if(stack == null) {
                                availableSlot = i;
                                break;
                            }
                        }

                        int networkSlot = -1;
                        if(isWhitelist){
                            for (ItemStack stack : contents) {
                                if(stack != null){
                                    networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getMetadata());
                                    if(networkSlot != -1){
                                        break;
                                    }
                                }
                            }
                        } else if(isEmpty()) {
                            networkSlot = network.inventory.getLastOccupiedStack();
                        } else {
                            networkSlot = network.inventory.getLastOccupiedStack();
                            ItemStack networkStack = network.inventory.getStackInSlot(networkSlot);
                            for (ItemStack stack : contents) {
                                if (stack != null) {
                                    if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata()){
                                        int i = networkSlot;
                                        while(i > 1 && (networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata())){
                                            i--;
                                            networkStack = getStackInSlot(i);
                                        }
                                        if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata()){
                                            networkSlot = -1;
                                        } else {
                                            networkSlot = i;
                                        }
                                    }
                                }
                            }
                        }
                        if(networkSlot != -1){
                            ItemStack stack = network.inventory.getStackInSlot(networkSlot);
                            if(stack != null && availableSlot != -1){
                                inv.setInventorySlotContents(availableSlot,stack);
                                network.inventory.setInventorySlotContents(networkSlot,null);
                                DiscManipulator.saveDisc(network.drive.virtualDisc,network.inventory);
                            }
                        }
                    } else {
                        if(slot >= inv.getSizeInventory()){
                            return;
                        }
                        int availableSlot = -1;
                        if(inv.getStackInSlot(slot) == null){
                            availableSlot = slot;
                        }

                        int networkSlot = -1;
                        if(isWhitelist){
                            for (ItemStack stack : contents) {
                                if(stack != null){
                                    networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getMetadata());
                                    if(networkSlot != -1){
                                        break;
                                    }
                                }
                            }
                        } else if(isEmpty()) {
                            networkSlot = network.inventory.getLastOccupiedStack();
                        } else {
                            networkSlot = network.inventory.getLastOccupiedStack();
                            ItemStack networkStack = network.inventory.getStackInSlot(networkSlot);
                            for (ItemStack stack : contents) {
                                if (stack != null) {
                                    if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata()){
                                        int i = networkSlot;
                                        while(i > 1 && (networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata())){
                                            i--;
                                            networkStack = getStackInSlot(i);
                                        }
                                        if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getMetadata() == stack.getMetadata()){
                                            networkSlot = -1;
                                        } else {
                                            networkSlot = i;
                                        }
                                    }
                                }
                            }
                        }
                        if(networkSlot != -1){
                            ItemStack stack = network.inventory.getStackInSlot(networkSlot);
                            if(stack != null && availableSlot != -1){
                                inv.setInventorySlotContents(availableSlot,stack);
                                network.inventory.setInventorySlotContents(networkSlot,null);
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
