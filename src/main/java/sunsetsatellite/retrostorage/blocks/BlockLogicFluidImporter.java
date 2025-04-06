package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;

import java.util.function.Supplier;

public class BlockLogicFluidImporter extends BlockLogicNetworkDevice {


    public BlockLogicFluidImporter(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        super.onNeighborBlockChange(world, x, y, z, blockId);
        TileEntityFluidImporter tile = (TileEntityFluidImporter) world.getTileEntity(x, y, z);
        if(tile != null) {
            if (world.hasNeighborSignal(x, y, z)) {
                tile.enabled = false;
            } else if (!world.hasNeighborSignal(x, y, z)) {
                tile.enabled = true;
            }
        }
    }
}
