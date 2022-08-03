package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class TileEntityImporter extends TileEntityInNetwork {

	public TileEntityImporter() {
	}
	
	public void updateEntity()
    {
		if(controller != null && controller.network_drive != null && controller.network_drive.virtualDriveMaxStacks != 0) {
			TileEntity tile = findTileEntityAroundBlock();
			if (tile instanceof TileEntityChest){
				//System.out.println("chest connected");
				for(int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
					ItemStack item = ((TileEntityChest) tile).getStackInSlot(i);
					if (item != null) {
						if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
							if (controller.network_inv.addItemStackToInventory(item)){
								((TileEntityChest) tile).setInventorySlotContents(i, null);
								DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
							}
		    			}
					}
				}
			} else if (tile instanceof TileEntityFurnace){
				ItemStack item = ((TileEntityFurnace) tile).getStackInSlot(2);
				if (item != null) {
					if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
						if (controller.network_inv.addItemStackToInventory(item)){
							((TileEntityFurnace) tile).setInventorySlotContents(2, null);
							DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
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
	
	public void readFromNBT() {	
	}
	
	public void writeToNBT() {
	}
}
