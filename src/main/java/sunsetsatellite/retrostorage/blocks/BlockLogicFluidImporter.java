package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidExporter;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;

import java.util.function.Supplier;

public class BlockLogicFluidImporter extends BlockLogicNetworkDevice {


    public BlockLogicFluidImporter(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		super.onNeighborChanged(world, tilePos, block);
		TileEntityFluidImporter tile = (TileEntityFluidImporter) world.getTileEntity(tilePos);
		if(tile != null) {
			if (world.hasNeighborSignal(tilePos)) {
				tile.enabled = false;
			} else if (!world.hasNeighborSignal(tilePos)) {
				tile.enabled = true;
			}
		}
	}
}
