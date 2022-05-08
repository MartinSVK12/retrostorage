package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class TileEntityImporter extends TileEntityInNetwork {

	public TileEntityImporter() {
	}
	
	public void updateEntity()
    {
		connectDrive();
		
		if(network.size() > 0 && network_disc != null && network_drive != null) {
			TileEntity tile = findTileEntityAroundBlock();
			if (tile instanceof TileEntityChest){
				//System.out.println("chest connected");
				for(int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
					ItemStack item = ((TileEntityChest) tile).getStackInSlot(i);
					if (item != null) {
						if (network_disc.getItem() instanceof ItemStorageDisc) {
		    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
		    					if(DiscManipulator.getFirstAvailablePartition(network_disc, network_drive) != -1) {
		    						((TileEntityChest) tile).setInventorySlotContents(i, null);
		    						DiscManipulator.addStackToPartitionedDisc(item,network_disc,DiscManipulator.getFirstAvailablePartition(network_disc, network_drive));
		    						network_drive.updateDiscs();
		    					}
		    				}
		    			}
					}
				}
			} else if (tile instanceof TileEntityFurnace){
				ItemStack item = ((TileEntityFurnace) tile).getStackInSlot(2);
				if (item != null) {
					if (network_disc.getItem() instanceof ItemStorageDisc) {
						if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
							if(DiscManipulator.getFirstAvailablePartition(network_disc, network_drive) != -1) {
								((TileEntityFurnace) tile).setInventorySlotContents(2, null);
								DiscManipulator.addStackToPartitionedDisc(item,network_disc,DiscManipulator.getFirstAvailablePartition(network_disc, network_drive));
								network_drive.updateDiscs();
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
	
	public void readFromNBT() {	
	}
	
	public void writeToNBT() {
	}
}
