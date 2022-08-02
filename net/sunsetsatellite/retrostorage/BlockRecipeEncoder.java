package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

public class BlockRecipeEncoder extends BlockContainer{

	public static int sprites[];
	
	public BlockRecipeEncoder(int i)
    {
        super(i, Material.rock);
        blockIndexInTexture = 0;
    }

	
    public static void loadSprites()
    {
        sprites = new int[4];
        sprites[0] = mod_RetroStorage.digitalChestSide; //bottom
        sprites[1] = mod_RetroStorage.recipeEncoderFront; //front
        sprites[2] = mod_RetroStorage.digitalChestSide; //side
        sprites[3] = mod_RetroStorage.recipeEncoderTop; //top 1
    }

    public int idDropped(int i, Random random)
    {
        return mod_RetroStorage.recipeEncoder.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
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
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
			TileEntityRecipeEncoder TileEntityRecipeEncoder = (TileEntityRecipeEncoder)world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityRecipeEncoder);
            if (TileEntityRecipeEncoder != null) {
            	ModLoader.OpenGUI(entityplayer, new GuiRecipeEncoder
            			(entityplayer.inventory, TileEntityRecipeEncoder));
            }
            return true;
        }
    }
    
    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityRecipeEncoder TileEntityRecipeEncoder = (TileEntityRecipeEncoder)world.getBlockTileEntity(i, j, k);
        label0:
        for(int l = 0; l < TileEntityRecipeEncoder.getSizeInventory(); l++)
        {
            ItemStack itemstack = TileEntityRecipeEncoder.getStackInSlot(l);
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
    
    protected TileEntity getBlockEntity() {
		return new TileEntityRecipeEncoder();
	}
}
