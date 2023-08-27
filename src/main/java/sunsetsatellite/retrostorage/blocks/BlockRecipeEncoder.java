package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiRecipeEncoder;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

public class BlockRecipeEncoder extends BlockTileEntityRotatable {

    public BlockRecipeEncoder(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityRecipeEncoder();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityRecipeEncoder tile = (TileEntityRecipeEncoder) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                ((IOpenGUI)entityplayer).displayGUI(new GuiRecipeEncoder(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityRecipeEncoder tileEntityRecipeEncoder = (TileEntityRecipeEncoder)world.getBlockTileEntity(i, j, k);
        label0:
        for(int l = 0; l < tileEntityRecipeEncoder.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileEntityRecipeEncoder.getStackInSlot(l);
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
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata(), itemstack.tag));
                float f3 = 0.05F;
                entityitem.xd = (float)world.rand.nextGaussian() * f3;
                entityitem.yd = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.zd = (float)world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
