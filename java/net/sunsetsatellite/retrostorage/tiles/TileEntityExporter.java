package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.util.Direction;
import net.sunsetsatellite.retrostorage.util.DiscManipulator;
import net.sunsetsatellite.retrostorage.util.TickTimer;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityExporter extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityExporter() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this,"work",10,true);
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

    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        return null;
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
            if(this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getItemDamage() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    public String getInvName()
    {
        return "Exporter";
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
                contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);//new ItemStack(nbttagcompound1);
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
                nbttaglist.appendTag(nbttagcompound1);
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

    @Override
    public void updateEntity() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IInventory.class);
        connectedTiles = getConnectedTileEntities(tiles);
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
                                    networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
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
                                    if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage()){
                                        int i = networkSlot;
                                        while(i > 1 && (networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage())){
                                            i--;
                                            networkStack = getStackInSlot(i);
                                        }
                                        if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage()){
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
                                    networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
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
                                    if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage()){
                                        int i = networkSlot;
                                        while(i > 1 && (networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage())){
                                            i--;
                                            networkStack = getStackInSlot(i);
                                        }
                                        if(networkStack == null || networkStack.itemID == stack.itemID && networkStack.getItemDamage() == stack.getItemDamage()){
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
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
}
