package net.sunsetsatellite.retrostorage;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockImporter extends BlockContainer {

	public BlockImporter(int i, Material material) {
		super(i, material);
		
	}

	public BlockImporter(int i, int j, Material material) {
		super(i, j, material);
		
	}
	
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
		TileEntityImporter tileentityimporter = (TileEntityImporter)world.getBlockTileEntity(i, j, k);
		return true;
    }
	
	protected TileEntity getBlockEntity() {
		return new TileEntityImporter();
	}
	
    private TileEntityInNetwork tileentity = (TileEntityInNetwork) getBlockEntity();

}
