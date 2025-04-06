package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;

import java.util.function.Supplier;

public class BlockLogicDigitalFluidTerminal extends BlockLogicNetworkDevice {

    public BlockLogicDigitalFluidTerminal(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

}
