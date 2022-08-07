package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

public class BlockExporter extends BlockContainer {

	public BlockExporter(int i, Material material) {
		super(i, material);
		
	}

	public BlockExporter(int i, int j, Material material) {
		super(i, j, material);
		
	}
	
	 public void onBlockRemoval(World world, int i, int j, int k)
	    {
	        TileEntityExporter TileEntityExporter = (TileEntityExporter)world.getBlockTileEntity(i, j, k);
	        label0:
	        for(int l = 0; l < TileEntityExporter.getSizeInventory()-1; l++)
	        {
	            ItemStack itemstack = TileEntityExporter.getStackInSlot(l);
	            if(itemstack == null)
	            {
	                continue;
	            }
	            float f = world.rand.nextFloat() * 0.8F + 0.1F;
	            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
	            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
	            do
	            {
	                if(itemstack.stackSize <= 0)
	                {
	                    continue label0;
	                }
	                int i1 = world.rand.nextInt(21) + 10;
	                if(i1 > itemstack.stackSize)
	                {
	                    i1 = itemstack.stackSize;
	                }
	                itemstack.stackSize -= i1;
	                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage(), itemstack.getItemData()));
	                float f3 = 0.05F;
	                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
	                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
	                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
	                world.entityJoinedWorld(entityitem);
	            } while(true);
	        }
	        super.onBlockRemoval(world, i, j, k);
	    }

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
		 if(world.multiplayerWorld)
		    {
		        return true;
		    } else
		    {
		    	TileEntityExporter TileEntityExporter = (TileEntityExporter)world.getBlockTileEntity(i, j, k);
		        //System.out.println(TileEntityExporter);
		    	if (TileEntityExporter != null) {
			    	ModLoader.OpenGUI(entityplayer, new GuiExporter
			    			(entityplayer.inventory, TileEntityExporter));
			    }
			    return true;
		    }
    }
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		TileEntityExporter tileentityexporter = (TileEntityExporter)world.getBlockTileEntity(i, j, k);
		if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
		{
			tileentityexporter.enabled = false;
		} else if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
			tileentityexporter.enabled = true;
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
		return new TileEntityExporter();
	}
	
    private TileEntityInNetworkWithInv tileentity = (TileEntityInNetworkWithInv) getBlockEntity();
}
