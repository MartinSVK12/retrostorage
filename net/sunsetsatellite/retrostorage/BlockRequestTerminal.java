package net.sunsetsatellite.retrostorage;

import java.util.Random;

import net.minecraft.src.*;

public class BlockRequestTerminal extends BlockContainer{

	public static int sprites[];
    private final boolean isActive;
	
	public BlockRequestTerminal(int i, boolean flag)
    {
        super(i, Material.rock);
        isActive = flag;
        blockIndexInTexture = mod_RetroStorage.digitalChestFront;
    }

	
    public static void loadSprites()
    {
        sprites = new int[5];
        sprites[0] = mod_RetroStorage.digitalChestSide; //bottom
        sprites[1] = mod_RetroStorage.requestTerminalFront; //front
        sprites[2] = mod_RetroStorage.assemblerSide; //side
        sprites[3] = mod_RetroStorage.assemblerSide; //top 1
        sprites[4] = mod_RetroStorage.assemblerSide; //top 2
    }

    public int idDropped(int i, Random random)
    {
        return 0;//mod_RetroStorage.requestTerminal.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
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

            if (entityplayer.getCurrentEquippedItem() != null){
                if (entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.mobileRequestTerminal) {
                    return false;
                }
            }
            TileEntityRequestTerminal TileEntityRequestTerminal = (TileEntityRequestTerminal)world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityRequestTerminal);
            if (TileEntityRequestTerminal != null) {
            	ModLoader.OpenGUI(entityplayer, new GuiRequestTerminal(entityplayer.inventory, TileEntityRequestTerminal));
            }
            return true;
        }
    }
    
    public void dropContent(World world, int i, int j, int k)
    {
        TileEntityRequestTerminal TileEntityRequestTerminal = (TileEntityRequestTerminal)world.getBlockTileEntity(i, j, k);
   label0:
        for(int l = 0; l < TileEntityRequestTerminal.getSizeInventory(); l++)
        {
            ItemStack itemstack = TileEntityRequestTerminal.getStackInSlot(l);
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
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }

    }
    
    protected TileEntity getBlockEntity() {
		return new TileEntityRequestTerminal();
	}
}
