package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class TileEntityExporter extends TileEntityInNetworkWithInv {

	public TileEntityExporter() {
		 contents = new ItemStack[10];
	}
	
	public int getSizeInventory()
    {
        return contents.length;
    }
	
	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory()-1; i++) {
			if(getStackInSlot(i) != null) {
				return false;
			} else
			{
				continue;
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
    
    public void onInventoryChanged() {
    	super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Item Exporter";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        enabled = nbttagcompound.getBoolean("enabled");
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
        nbttagcompound.setBoolean("enabled",enabled);
        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

	public void updateEntity()
    {
        workTicks++;
        if(enabled && workTicks >= workMaxTicks){
            workTicks = 0;
            if(controller != null && controller.isActive() && controller.network_disc != null) {
                TileEntity tile = findAnyTileEntityAroundBlock(TileEntityInNetworkWithInv.class);
                if (tile instanceof IInventory){
                    IInventory chest = (IInventory) tile;
                    ItemStack item = null;
                    int s_slot = -1;
                    if(slot == -1) {
                        for (int i = 0; i < chest.getSizeInventory(); i++) {
                            item = chest.getStackInSlot(i);
                            if (item != null) {
                                continue;
                            } else {
                                s_slot = i;
                                break;
                            }
                        }
                        if(s_slot == -1){
                            return;
                        }
                    } else {
                        s_slot = slot;
                        item = chest.getStackInSlot(s_slot);
                    }
                    if(isEmpty()) {
                        if (item == null) {
                            if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
                                ItemStack network_item = controller.network_inv.getStackInSlot(controller.network_inv.getFirstOccupiedStack());
                                if(network_item != null && network_item.getItem() != mod_RetroStorage.virtualDisc){
                                    controller.network_inv.setInventorySlotContents(controller.network_inv.getFirstOccupiedStack(),null);
                                    DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                    chest.setInventorySlotContents(s_slot, network_item);
                                }
                            }
                        }
                    } else {
                        if (item == null) {
                            if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
                                int i = 0;
                                int filter = -1;
                                do {
                                    if(getStackInSlot(i) != null) {
                                        filter = controller.network_inv.getInventorySlotContainItem(getStackInSlot(0).itemID);
                                    }
                                    i++;
                                    if(i == 9) {
                                        break;
                                    }
                                } while (filter == -1);
                                if(filter != -1){
                                    ItemStack network_item = controller.network_inv.getStackInSlot(filter);
                                    if(network_item != null){
                                        controller.network_inv.setInventorySlotContents(filter,null);
                                        DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                        chest.setInventorySlotContents(s_slot, network_item);
                                    }
                                }
                            }
                        }
                    }
                } else if(tile instanceof TileEntityStorageContainer){
                    TileEntityStorageContainer container = (TileEntityStorageContainer) tile;
                    if(container.storedID != 0 && isEmpty()){
                        int filter = controller.network_inv.getInventorySlotContainItem(container.storedID,container.storedMetadata);
                        if(filter != -1){
                            ItemStack network_item = controller.network_inv.getStackInSlot(filter);
                            if(network_item != null && container.storedAmount + network_item.stackSize <= container.maxAmount){
                                controller.network_inv.setInventorySlotContents(filter,null);
                                DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                container.storedAmount += network_item.stackSize;
                            } else if(network_item != null && !(container.storedAmount == container.maxAmount) && container.storedAmount + network_item.stackSize > container.maxAmount){
                                controller.network_inv.decrStackSize(filter,container.maxAmount-container.storedAmount);
                                DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                container.storedAmount += container.maxAmount - container.storedAmount;
                            }
                        }
                    } else if(container.storedID == 0 && isEmpty()) {
                        int filter = 0;
                        ItemStack network_item = controller.network_inv.getStackInSlot(filter);
                        if(network_item != null){
                            controller.network_inv.setInventorySlotContents(filter,null);
                            DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                            container.storedAmount += network_item.stackSize;
                        }
                    }
                }
            }
        }
    }
	
	public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
	
	private ItemStack[] contents;
    boolean enabled = true;
    private int workTicks = 0;
    private int workMaxTicks = 20;
    int slot = 0;
}
