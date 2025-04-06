package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockLogicDiscDrive extends BlockLogicNetworkDevice {


    public BlockLogicDiscDrive(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityDiscDrive tile = (TileEntityDiscDrive) world.getTileEntity(x, y, z);
        ArrayList<ItemStack> discsUsed = (ArrayList<ItemStack>) tile.discsUsed.clone();
        for (ItemStack ignored : discsUsed) {
            tile.removeLastDisc();
            ItemStack itemstack = tile.getItem(1).copy();
            tile.setItem(1, null);
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, itemstack);
            float f3 = 0.05F;
            entityitem.xd = (float) world.rand.nextGaussian() * f3;
            entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) world.rand.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
        }
        super.onBlockRemoved(world, x, y, z, data);
    }
}
