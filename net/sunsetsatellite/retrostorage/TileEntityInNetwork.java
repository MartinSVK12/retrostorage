package net.sunsetsatellite.retrostorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class TileEntityInNetwork extends TileEntityDigitalContainer
{

	public TileEntityInNetwork() {
	}
	
	protected TileEntity findTileEntityAroundBlock() {
		if (worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord) != null) {
			return worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord) != null) {
			return worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord) != null) {
			return worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord) != null) {
			return worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1) != null) {
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1) != null) {
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
		}
		return null;
	}
	
	protected void connectDrive() {
		if(network.size() > 0) {
			for (Entry<ArrayList<Integer>, HashMap<String, Object>> element : network.entrySet()) {
				ArrayList<Integer> pos = element.getKey();
				TileEntity tile = (TileEntity) worldObj.getBlockTileEntity(pos.get(0), pos.get(1), pos.get(2));
				if (tile != null) {
					if (tile instanceof TileEntityDiscDrive) {
						TileEntityDiscDrive drive = (TileEntityDiscDrive) tile;
						network_drive = drive;
						if (drive.getStackInSlot(drive.getSizeInventory() - 1) != null) {
							if (drive.getStackInSlot(drive.getSizeInventory() - 1).getItem() instanceof ItemStorageDisc) {
								network_disc = drive.getStackInSlot(drive.getSizeInventory() - 1);
								break;
							} else {
								network_disc = null;
								network_drive = null;
							}
						} else {
							network_disc = null;
							network_drive = null;
						}
					}/* else if (tile instanceof TileEntityStorageBlock) {
						TileEntityStorageBlock drive = (TileEntityStorageBlock) tile;
						network_drive = drive;
						if (drive.getStackInSlot(drive.getSizeInventory() - 1) != null) {
							if (drive.getStackInSlot(drive.getSizeInventory() - 1).getItem() instanceof ItemStorageDisc) {
								network_drive.createVirtualDisc();
								network_disc = drive.getStackInSlot(drive.getSizeInventory() - 1);
								break;
							} else {
								network_disc = null;
								network_drive = null;
							}
						} else {
							network_disc = null;
							network_drive = null;
						}*/
					else {
						network_disc = null;
						network_drive = null;
					}
				} else {
					network_disc = null;
					network_drive = null;
				}
			}
		} else {
			network_disc = null;
			network_drive = null;
		}
	}
	
	protected HashMap<ArrayList<Integer>, HashMap<String, Object>> network = new HashMap<ArrayList<Integer>, HashMap<String, Object>>();
	
	protected TileEntityDiscDrive network_drive = null;
	protected ItemStack network_disc = null;
	protected TileEntityDigitalController controller = null;
}
