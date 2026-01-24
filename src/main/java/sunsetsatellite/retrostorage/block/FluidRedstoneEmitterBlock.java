package sunsetsatellite.retrostorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;
import sunsetsatellite.retrostorage.block.entity.FluidRedstoneEmitterBlockEntity;

import java.util.function.Supplier;

public class FluidRedstoneEmitterBlock extends NetworkDeviceBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public FluidRedstoneEmitterBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }

    @Override
    public boolean canEmitRedstonePower() {
        return true;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPoweringSide(BlockView blockView, int x, int y, int z, int side) {
        FluidRedstoneEmitterBlockEntity tile = (FluidRedstoneEmitterBlockEntity) blockView.getBlockEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public boolean isStrongPoweringSide(World world, int x, int y, int z, int side) {
        FluidRedstoneEmitterBlockEntity tile = (FluidRedstoneEmitterBlockEntity) world.getBlockEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public void onPlaced(World level, int x, int y, int z, LivingEntity living) {
        super.onPlaced(level, x, y, z, living);
        level.setBlockState(x, y, z, level.getBlockState(x, y, z).with(ACTIVE, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ACTIVE);
    }
}
