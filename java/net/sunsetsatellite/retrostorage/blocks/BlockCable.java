package net.sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;

import java.util.Random;

public class BlockCable extends Block {
	
	public static int sprites[];

	public BlockCable(int i) {
		super(i, Material.cloth);
	}
	
	/*public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, (float)j + 1.5F, k + 1);
    }*/

	public static void loadSprites()
    {
        sprites = new int[4];
        sprites[0] = mod_RetroStorage.networkCableTex; //bottom
        sprites[1] = mod_RetroStorage.networkCableTex; //front
        sprites[2] = mod_RetroStorage.networkCableTex; //side
        sprites[3] = mod_RetroStorage.networkCableTex; //top 1
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

	public int idDropped(int i, Random random)
	{
		return this.blockID;
	}

	@Override
	public String getTextureFile() {
		return "/assets/retrostorage/blocks.png";
	}

	public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }


    
    public int getRenderType()
    {
        return mod_RetroStorage.networkCableRenderID;
    }

}
