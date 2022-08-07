package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

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


	public void updateTick(World world, int i, int j, int k, Random random)
	{
		TileEntityImporter tileentityimporter = (TileEntityImporter)world.getBlockTileEntity(i, j, k);
        if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
        {
			tileentityimporter.enabled = false;
        } else if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
			tileentityimporter.enabled = true;
        }
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if(l > 0 && Block.blocksList[l].canProvidePower())
		{
			boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			boolean flag2 = !world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			if(flag || flag2)
			{
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
	}
	
	protected TileEntity getBlockEntity() {
		return new TileEntityImporter();
	}
	
    private TileEntityInNetwork tileentity = (TileEntityInNetwork) getBlockEntity();

}
