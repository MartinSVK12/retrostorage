package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

public class BlockDigitalChest extends BlockContainer{

	public static int sprites[];
    private final boolean isActive;
	
	public BlockDigitalChest(int i, boolean flag)
    {
        super(i, Material.rock);
        isActive = flag;
        blockIndexInTexture = mod_RetroStorage.digitalChestFront;
    }

	
    public static void loadSprites()
    {
        sprites = new int[5];
        sprites[0] = mod_RetroStorage.digitalChestSide; //bottom
        sprites[1] = mod_RetroStorage.digitalChestFront; //front
        sprites[2] = mod_RetroStorage.digitalChestSide; //side
        sprites[3] = mod_RetroStorage.digitalChestTop2; //top 1
        sprites[4] = mod_RetroStorage.digitalChestTop2; //top 2
    }

    public int idDropped(int i, Random random)
    {
        return mod_RetroStorage.digitalChest.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        byte byte0 = 3;
        if(l == 0) {
            byte0 = 2;
        }

        if(l == 1) {
            byte0 = 5;
        }

        if(l == 2) {
            byte0 = 3;
        }

        if(l == 3) {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }
	
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(l == 1)
        {
            if(isActive)
            {
                return sprites[3];
            } else
            {
                return sprites[4];
            }
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
            return sprites[4];
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
            TileEntityDigitalChest TileEntityDigitalChest = (TileEntityDigitalChest)world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (TileEntityDigitalChest != null) {
            	ModLoader.OpenGUI(entityplayer, new GuiDigitalChest(entityplayer.inventory, TileEntityDigitalChest));
            }
            return true;
        }
    }
    
    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityDigitalChest TileEntityDigitalChest = (TileEntityDigitalChest)world.getBlockTileEntity(i, j, k);
        ItemStack itemstack = TileEntityDigitalChest.getStackInSlot(0);
        if(itemstack == null)
        {
            return;
        }
        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
        while (itemstack.stackSize > 0) {
            int i1 = world.rand.nextInt(21) + 10;
            if (i1 > itemstack.stackSize) {
                i1 = itemstack.stackSize;
            }
            itemstack.stackSize -= i1;
            EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage(), itemstack.getItemData()));
            float f3 = 0.05F;
            entityitem.motionX = (float) world.rand.nextGaussian() * f3;
            entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
        }
    }
    
    protected TileEntity getBlockEntity() {
		return new TileEntityDigitalChest();
	}
}
