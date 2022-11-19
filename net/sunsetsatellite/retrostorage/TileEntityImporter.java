package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class TileEntityImporter extends TileEntityInNetwork {

	public TileEntityImporter() {
	}
	
	public void updateEntity()
    {
		workTicks++;
		if(workTicks>=workMaxTicks){
			workTicks = 0;
			if(enabled){
				if(controller != null && controller.network_drive != null && controller.network_drive.virtualDriveMaxStacks != 0) {
					TileEntity tile = findAnyTileEntityAroundBlock(TileEntityInNetworkWithInv.class);
					if (tile instanceof IInventory){
						if(slot == -1){
							for(int i = 0; i < ((IInventory) tile).getSizeInventory(); i++) {
								ItemStack item = ((IInventory) tile).getStackInSlot(i);
								if (item != null) {
									if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
										if (controller.network_inv.addItemStackToInventory(item)){
											((IInventory) tile).setInventorySlotContents(i, null);
											DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
										}
									}
								}
							}
						} else {
							ItemStack item = ((IInventory) tile).getStackInSlot(slot);
							if (item != null) {
								if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
									if (controller.network_inv.addItemStackToInventory(item)){
										((IInventory) tile).setInventorySlotContents(slot, null);
										DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
									}
								}
							}
						}
					} else if (tile instanceof TileEntityStorageContainer){
						TileEntityStorageContainer container = (TileEntityStorageContainer) tile;
						if(container.storedID != 0){
							if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
								for(int i = 0; i<64;i++){
									if(container.storedAmount > 0){
										ItemStack item = new ItemStack(container.storedID,1,container.storedMetadata,container.storedData);
										if (controller.network_inv.addItemStackToInventory(item)){
											container.storedAmount--;
										}
									}
								}
								DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
							}
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

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		enabled = nbttagcompound.getBoolean("enabled");
		slot = nbttagcompound.getInteger("slot");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("enabled",enabled);
		nbttagcompound.setInteger("slot",slot);
	}

	boolean enabled = true;
	int slot = 0;
	private int workTicks = 0;
	private int workMaxTicks = 5;
}
