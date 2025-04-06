package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.retrostorage.tiles.TileEntityCoprocessor;

import java.util.function.Supplier;

public class BlockLogicCoprocessor extends BlockLogicNetworkDevice {

    public BlockLogicCoprocessor(Block<?> block) {
        super(block, TileEntityCoprocessor::new, null);
    }
}
