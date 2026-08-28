package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBdc;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;

import java.util.function.Supplier;

public class BlockLogicNetworkCable extends BlockLogicNetworkDevice implements IConduitBlock {

    public BlockLogicNetworkCable(Block<?> block, Supplier<TileEntity> tileEntitySupplier) {
        super(block, tileEntitySupplier, null);
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
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.RES_NETWORK;
    }

    public void setBlockBoundsBasedOnState(WorldSource world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        float bx = 0.3f, by = 0.3f, bz = 0.3f;
        float tx = 0.7f, ty = 0.7f, tz = 0.7f;
        // Loop de-loop
        for (Direction dir : Direction.values()) {
            Vec3i v = dir.getVec();
            TileEntity te = world.getTileEntity(x + v.x, y + v.y, z + v.z);
            Block<?> b = world.getBlock(x + v.x, y + v.y, z + v.z);
			if(b == Blocks.AIR) continue;
            //noinspection ConditionCoveredByFurtherCondition
            if (
                    te == null || !(te instanceof TileEntityFluidPipe) ||
                            getConduitCapability() != ((IConduitBlock) b.getLogic()).getConduitCapability()
            ) continue;
            if (v.x > 0) tx = 1.0f;
            else if (v.x < 0) bx = 0.0f;
            if (v.z > 0) tz = 1.0f;
            else if (v.z < 0) bz = 0.0f;
            if (v.y > 0) ty = 1.0f;
            else if (v.y < 0) by = 0.0f;
        }
        setBlockBounds(bx, by, bz, tx, ty, tz);
    }

	@Override
	public @Nullable AABBdc getCollisionAABB(@NotNull WorldSource world, @NotNull TilePosc tilePos) {
		setBlockBoundsBasedOnState(world, tilePos.x(), tilePos.y(), tilePos.z());
		return super.getCollisionAABB(world, tilePos);
	}

}
