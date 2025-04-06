package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;

import java.util.function.Supplier;

public class BlockLogicAdvInterface extends BlockLogicNetworkDevice {

    public BlockLogicAdvInterface(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityAdvInterface TileEntityAdvInterface = (TileEntityAdvInterface) world.getTileEntity(x, y, z);
        label0:
        for (int l = 0; l < TileEntityAdvInterface.getContainerSize(); l++) {
            ItemStack itemstack = TileEntityAdvInterface.getItem(l);
            if (itemstack == null) {
                continue;
            }
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            do {
                if (itemstack.stackSize <= 0) {
                    continue label0;
                }
                int i1 = world.rand.nextInt(21) + 10;
                if (i1 > itemstack.stackSize) {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata(), itemstack.getData()));
                float f3 = 0.05F;
                entityitem.xd = (float) world.rand.nextGaussian() * f3;
                entityitem.yd = (float) world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.zd = (float) world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while (true);
        }
        super.onBlockRemoved(world, x, y, z, data);
    }
}
