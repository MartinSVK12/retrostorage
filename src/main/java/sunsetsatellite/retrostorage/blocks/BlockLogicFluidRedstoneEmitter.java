package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidRedstoneEmitter;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;

import java.util.function.Supplier;

public class BlockLogicFluidRedstoneEmitter extends BlockLogicNetworkDevice {

    public BlockLogicFluidRedstoneEmitter(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlockOnCondition(WorldSource world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean getDirectSignal(World worldSource, int x, int y, int z, Side side) {
        TileEntityFluidRedstoneEmitter tile = (TileEntityFluidRedstoneEmitter) worldSource.getTileEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public boolean getSignal(WorldSource world, int x, int y, int z, Side side) {
        TileEntityFluidRedstoneEmitter tile = (TileEntityFluidRedstoneEmitter) world.getTileEntity(x, y, z);
        return tile != null && tile.isActive;
    }
}
