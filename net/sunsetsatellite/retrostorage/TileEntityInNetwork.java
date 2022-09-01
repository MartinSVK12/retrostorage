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
	
	protected TileEntity findTileEntityAroundBlock(Class<?> tile) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord).getClass())) {
			return worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord).getClass())) {
			return worldObj.getBlockTileEntity(xCoord+1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord).getClass())) {
			return worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord).getClass())) {
			return worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1).getClass())) {
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1) != null && tile.isAssignableFrom(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1).getClass())) {
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
		}
		return null;
	}

	protected TileEntity findAnyTileEntityAroundBlock() {
		if (worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord) != null) {
			return worldObj.getBlockTileEntity(xCoord, yCoord+1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord) != null){
			return worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord) != null){
			return worldObj.getBlockTileEntity(xCoord-1, yCoord, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord) != null){
			return worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1) != null){
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord+1);
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1) != null){
			return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord-1);
		}
		return null;
	}


	protected TileEntityDigitalController controller = null;
}
