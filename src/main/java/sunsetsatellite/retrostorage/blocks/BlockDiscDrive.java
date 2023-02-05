package sunsetsatellite.retrostorage.blocks;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.gui.GuiDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

import java.util.ArrayList;

public class BlockDiscDrive extends BlockContainerRotatable {
    public BlockDiscDrive(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDiscDrive();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
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
            entityitem.motionX = (float) world.rand.nextGaussian() * f3;
            entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
        }
        super.onBlockRemoval(world, i, j, k);
    }
}
