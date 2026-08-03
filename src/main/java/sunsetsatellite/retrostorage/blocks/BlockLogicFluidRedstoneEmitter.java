package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
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
	public boolean renderAsNormalBlockOnCondition(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return false;
	}

	@Override
	public boolean isEmittingSignal(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityFluidRedstoneEmitter tile = (TileEntityFluidRedstoneEmitter) world.getTileEntity(tilePos);
		return tile != null && tile.isActive;
	}

	@Override
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityFluidRedstoneEmitter tile = (TileEntityFluidRedstoneEmitter) world.getTileEntity(tilePos);
		return tile != null && tile.isActive;
	}
}
