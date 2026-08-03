package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class BlockLogicWirelessLink extends BlockLogic {

    public BlockLogicWirelessLink(Block<?> block) {
        super(block, Materials.STONE);
        block.withEntity(TileEntityWirelessLink::new);
    }
}
