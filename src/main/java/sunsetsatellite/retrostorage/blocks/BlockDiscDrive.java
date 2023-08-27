package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

import java.util.ArrayList;

public class BlockDiscDrive extends BlockTileEntityRotatable {

    public BlockDiscDrive(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDiscDrive();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityDiscDrive tile = (TileEntityDiscDrive) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                ((IOpenGUI)entityplayer).displayGUI(new GuiDiscDrive(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityDiscDrive tile = (TileEntityDiscDrive) world.getBlockTileEntity(i, j, k);
        if(tile.network != null){
            if(tile.network.drive == tile){
                tile.network.drive = null;
            }
        }
        ArrayList<ItemStack> discsUsed = (ArrayList<ItemStack>) tile.discsUsed.clone();
        for (ItemStack ignored : discsUsed) {
            tile.removeLastDisc();
            ItemStack itemstack = tile.getStackInSlot(1).copy();
            tile.setInventorySlotContents(1, null);
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, itemstack);
            float f3 = 0.05F;
            entityitem.xd = (float) world.rand.nextGaussian() * f3;
            entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
