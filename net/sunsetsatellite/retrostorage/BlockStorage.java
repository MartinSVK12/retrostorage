package net.sunsetsatellite.retrostorage;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockStorage extends BlockContainer {

	public BlockStorage(int i, Item j, Material material) {
		super(i, material);
		representingDisc = j;
		
	}

	public BlockStorage(int i, int j, Item k, Material material) {
		super(i, j, material);
		representingDisc = k;
    }
	
    public Item getRepresentingDisc() {
    	return representingDisc;
    }
    
    
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
		TileEntityStorageBlock tile = (TileEntityStorageBlock)world.getBlockTileEntity(i, j, k);
		ItemStorageDisc disc = (ItemStorageDisc) tile.virtualDisc.getItem();
		entityplayer.addChatMessage(tile.virtualDisc.getItemData().toString()+" out of "+disc.getMaxStackCapacity());
		return true;
    }
    
    

	@Override
	protected TileEntity getBlockEntity() {
		return new TileEntityStorageBlock();
	}
	
	public Item representingDisc;
	
}
