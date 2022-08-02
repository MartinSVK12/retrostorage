package net.sunsetsatellite.retrostorage;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.mod_RetroStorage;

public class BlockRelay extends Block {
	
	public static int sprites[];

	public BlockRelay(int i) {
		super(i, Material.cloth);
	}
	
	/*public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, (float)j + 1.5F, k + 1);
    }*/

	 public static void loadSprites()
    {

		sprites = new int[4];
        sprites[0] = mod_RetroStorage.relayOnTex; //bottom
        sprites[1] = mod_RetroStorage.relayOnTex; //front
        sprites[2] = mod_RetroStorage.relayOnTex; //side
        sprites[3] = mod_RetroStorage.relayOnTex; //top 1
        
    }
	
	 public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
	    {
	        if(l == 1)
	        {
                return sprites[3];
	        }
	        if(l == 0)
	        {
	            return sprites[0];
	        }
	        int i1 = iblockaccess.getBlockMetadata(i, j, k);
	        if(l != i1)
	        {
	            return sprites[2];
	        } else
	        {
	            return sprites[1];
	        }
	    }
		
    public int getBlockTextureFromSide(int i)
	    {
	        if(i == 1)
	        {
	            return sprites[3];
	        }
	        if(i == 0)
	        {
	            return sprites[0];
	        }
	        if(i == 3)
	        {
	            return sprites[1];
	        } else
	        {
	            return sprites[2];
	        }
	    }
	
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	/*if(world.getBlockId(i, j, k) == mod_RetroStorage.relay.blockID) {
    		world.setBlockWithNotify(i, j, k, mod_RetroStorage.relayOff.blockID);
    		return true;
    	} else if (world.getBlockId(i, j, k) == mod_RetroStorage.relayOff.blockID) {
    		world.setBlockWithNotify(i, j, k, mod_RetroStorage.relay.blockID);
    		return true;
    	}*/
    	return false;
    }
    
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        /*if(world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k))
        {
        	world.setBlockWithNotify(i, j, k, mod_RetroStorage.relayOff.blockID);
        } else if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
        	world.setBlockWithNotify(i, j, k, mod_RetroStorage.relay.blockID);
        }*/
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
    
    public int idDropped(int i, Random random)
    {
    	return 0;//mod_RetroStorage.relay.blockID;
    }
	 
    /*public boolean isOpaqueCube()
    {
        return true;
    }

    public boolean renderAsNormalBlock()
    {
        return true;
    }*/

    /*public int getRenderType()
    {
        return 18;
    }*/

}
