package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class TileEntityInNetworkWithInv extends TileEntityInNetwork
	implements IInventory
{

	public TileEntityInNetworkWithInv() {
	}
	@Override
	public int getSizeInventory() {
		
		return 0;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		
		return null;
	}
	@Override
	public ItemStack decrStackSize(int i, int j) {
		
		return null;
	}
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		
	}
	@Override
	public String getInvName() {
		
		return null;
	}
	@Override
	public int getInventoryStackLimit() {
		
		return 0;
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		
		return false;
	}

}
