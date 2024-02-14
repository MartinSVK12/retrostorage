package sunsetsatellite.retrostorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public abstract class RotatableBlockWithEntity extends TemplateBlockWithEntity {
    public RotatableBlockWithEntity(Identifier identifier, Material material) {
        super(identifier, material);
    }

    private static final Direction[] DIRECTIONS = new Direction[] { Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH };

    @Override
    public void onPlaced(World level, int x, int y, int z, LivingEntity living) {
        level.setBlockState(x, y, z, getDefaultState().with(HORIZONTAL_FACING, DIRECTIONS[MathHelper.floor((double)(living.yaw * 4.0F / 360.0F) + 0.5D) & 3]));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HORIZONTAL_FACING);
    }
}
