package net.sunsetsatellite.retrostorage;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockDigitalController extends BlockContainer 
{

	public BlockDigitalController(int i, int j) {
		super(i, j, Material.iron);
	}
	
	
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
		TileEntityDigitalController TileEntityDigitalController = (TileEntityDigitalController)world.getBlockTileEntity(i, j, k);
		TileEntityDigitalController.reloadNetwork(world, i, j, k, entityplayer);
		return true;
    }
	
	public void updateTick(World world, int i, int j, int k, Random random)
    {
		TileEntityDigitalController TileEntityDigitalController = (TileEntityDigitalController)world.getBlockTileEntity(i, j, k);
		TileEntityDigitalController.reloadNetwork(world, i, j, k, null);
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
		return new TileEntityDigitalController();
	}
	
    private TileEntityInNetwork tileentity = (TileEntityInNetwork) getBlockEntity();
    
}
