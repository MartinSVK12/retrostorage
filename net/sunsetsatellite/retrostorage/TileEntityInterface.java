package net.sunsetsatellite.retrostorage;

import ic2.TileEntityElecMachine;
import ic2.TileEntityElectricBlock;
import ic2.TileEntityElectricMachine;
import net.minecraft.src.*;

import java.util.Objects;

public class TileEntityInterface extends TileEntityInNetworkWithInv {

	public TileEntityInterface() {
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
        return "Item Interface";
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
        if(!Objects.equals(nbttagcompound.getCompoundTag("processing"), new NBTTagCompound())){
            processing = new ItemStack(nbttagcompound.getCompoundTag("processing"));
            processingAmount = nbttagcompound.getInteger("processingAmount");
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
        if(processing != null) {
            NBTTagCompound compound = new NBTTagCompound();
            processing.writeToNBT(compound);
            nbttagcompound.setCompoundTag("processing", compound);
            nbttagcompound.setInteger("processingAmount", processingAmount);
        }
        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getInventorySlotContainItem(int itemID, int itemDamage) {
        for(int i2 = 0; i2 < this.contents.length; ++i2) {
            if(this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getItemDamage() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    public void cancelProcessing(){
        processing = null;
        processingAmount = 0;
    }

	public void updateEntity()
    {
        TileEntity tile = findTileEntityAroundBlock(TileEntityFurnace.class);
        TileEntity tile2 = findTileEntityAroundBlock(TileEntityElectricMachine.class);
        attachedTileEntity = (tile != null ? tile : tile2);
        requestSearchTicks++;
        if(requestSearchTicks >= requestSearchMaxTicks){
            requestSearchTicks = 0;
            if(controller != null && controller.isActive() &&controller.network_disc != null && controller.network_disc.getItem() instanceof ItemStorageDisc && attachedTileEntity != null) {
                if(processing != null && processingAmount != 0){
                    if(attachedTileEntity instanceof TileEntityFurnace){
                        ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(2);
                        if(tileItem != null){// && tileItem.stackSize >= processingAmount){
                            if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
                                if (controller.network_inv.addItemStackToInventory(tileItem.copy())) {
                                    ((IInventory) attachedTileEntity).setInventorySlotContents(2, null);
                                    DiscManipulator.saveDisc(controller);
                                    if (((IInventory) attachedTileEntity).getStackInSlot(0) == null) {
                                        processing = null;
                                        processingAmount = 0;
                                        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing finished!");
                                    }
                                }
                            }
                        }
                    } else if(mod_RetroStorage.IC2Available() && attachedTileEntity instanceof TileEntityElectricMachine){
                        ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(2);
                        if(tileItem != null){// && tileItem.stackSize >= processingAmount){
                            if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
                                if (controller.network_inv.addItemStackToInventory(tileItem.copy())) {
                                    ((IInventory) attachedTileEntity).setInventorySlotContents(2, null);
                                    DiscManipulator.saveDisc(controller);
                                    if (((IInventory) attachedTileEntity).getStackInSlot(0) == null) {
                                        processing = null;
                                        processingAmount = 0;
                                        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing finished!");
                                    }
                                }
                            }
                        }
                    }
                }
                if (!controller.assemblyQueue.isEmpty()) {
                    ItemStack item = controller.assemblyQueue.peekFirst();
                    if(processing == null && getInventorySlotContainItem(item.itemID,item.getItemDamage()) != -1){
                        int networkItemCount = controller.network_inv.getItemCount(item.itemID,item.getItemDamage());
                        if(networkItemCount >= 1){
                            if(attachedTileEntity instanceof TileEntityFurnace){ // || (ModLoader.isModLoaded("mod_IC2")||ModLoader.isModLoaded(" net.minecraft.src.mod_IC2")) && attachedTileEntity instanceof TileEntityElecMachine){
                                ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(0);
                                if(tileItem == null){
                                    controller.assemblyQueue.remove(item);
                                    processing = item;
                                    int networkSlot = controller.network_inv.getInventorySlotContainItem(item.itemID,item.getItemDamage());
                                    if(networkSlot != -1){
                                        processingAmount += 1;
                                        controller.network_inv.decrStackSize(networkSlot,1);
                                        DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                        ((IInventory) attachedTileEntity).setInventorySlotContents(0,item.copy());
                                    }
                                }
                            } else if(mod_RetroStorage.IC2Available() && attachedTileEntity instanceof TileEntityElectricMachine){
                                ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(0);
                                if(tileItem == null){
                                    controller.assemblyQueue.remove(item);
                                    processing = item;
                                    int networkSlot = controller.network_inv.getInventorySlotContainItem(item.itemID,item.getItemDamage());
                                    if(networkSlot != -1){
                                        processingAmount += 1;
                                        controller.network_inv.decrStackSize(networkSlot,1);
                                        DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                        ((IInventory) attachedTileEntity).setInventorySlotContents(0,item.copy());
                                    }
                                }
                            }
                        } else {
                            controller.assemblyQueue.remove(item);
                            ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing failed!");
                        }
                    } else if(processing != null && processing.getItem() == item.getItem() && processing.getItemDamage() == item.getItemDamage() && getInventorySlotContainItem(item.itemID,item.getItemDamage()) != -1){
                        int networkItemCount = controller.network_inv.getItemCount(item.itemID,item.getItemDamage());
                        if(networkItemCount >= 1) {
                            if (attachedTileEntity instanceof TileEntityFurnace){//  || (ModLoader.isModLoaded("mod_IC2")||ModLoader.isModLoaded(" net.minecraft.src.mod_IC2")) && attachedTileEntity instanceof TileEntityElecMachine){
                                ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(0);
                                if (tileItem.isItemEqual(item) && tileItem.stackSize < 64) {
                                    controller.assemblyQueue.remove(item);
                                    int networkSlot = controller.network_inv.getInventorySlotContainItem(item.itemID, item.getItemDamage());
                                    if (networkSlot != -1) {
                                        processingAmount += 1;
                                        controller.network_inv.decrStackSize(networkSlot, 1);
                                        DiscManipulator.saveDisc(controller.network_disc, controller.network_inv);
                                        tileItem.stackSize += 1;
                                        //((TileEntityFurnace) attachedTileEntity).setInventorySlotContents(0, item.copy());
                                    }
                                }
                            } else if(mod_RetroStorage.IC2Available() && attachedTileEntity instanceof TileEntityElectricMachine){
                                ItemStack tileItem = ((IInventory) attachedTileEntity).getStackInSlot(0);
                                if (tileItem.isItemEqual(item) && tileItem.stackSize < 64) {
                                    controller.assemblyQueue.remove(item);
                                    int networkSlot = controller.network_inv.getInventorySlotContainItem(item.itemID, item.getItemDamage());
                                    if (networkSlot != -1) {
                                        processingAmount += 1;
                                        controller.network_inv.decrStackSize(networkSlot, 1);
                                        DiscManipulator.saveDisc(controller.network_disc, controller.network_inv);
                                        tileItem.stackSize += 1;
                                        //((TileEntityFurnace) attachedTileEntity).setInventorySlotContents(0, item.copy());
                                    }
                                }
                            }
                        } else if(getInventorySlotContainItem(item.itemID,item.getItemDamage()) == -1){
                            return;
                        } else {
                            controller.assemblyQueue.remove(item);
                            ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing failed!");
                        }
                    } else {
                        return;
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
    private ItemStack processing = null;
    private int processingAmount = 0;
    private int requestSearchTicks = 0;
    private int requestSearchMaxTicks = 20;
    private TileEntity attachedTileEntity = null;
}
