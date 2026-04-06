package sunsetsatellite.retrostorage.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.stationapi.api.state.property.Properties.FACING;
import static net.modificationstation.stationapi.api.state.property.Properties.FACING;

public abstract class RotatableBlockWithEntity extends TemplateBlockWithEntity {
    public RotatableBlockWithEntity(Identifier identifier, Material material) {
        super(identifier, material);
    }

    private static final Direction[] DIRECTIONS = new Direction[]{Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH};

    @Override
    public void onPlaced(World level, int x, int y, int z, LivingEntity living) {
        super.onPlaced(level, x, y, z, living);
        int facing = getFacingForPlacement(level, x, y, z, (PlayerEntity) living);
        level.setBlockState(x, y, z, getDefaultState().with(FACING, DIRECTIONS[facing]));
    }

    public static int getFacingForPlacement(World world, int x, int y, int z, PlayerEntity player) {
        if (MathHelper.abs((float)player.x - (float)x) < 2.0F && MathHelper.abs((float)player.z - (float)z) < 2.0F) {
            double var5 = player.y + 1.82 - (double)player.standingEyeHeight;
            if (var5 - (double)y > (double)2.0F) {
                return 1;
            }

            if ((double)y - var5 > (double)0.0F) {
                return 0;
            }
        }

        int var7 = MathHelper.floor((double)(player.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (var7 == 0) {
            return 2;
        } else if (var7 == 1) {
            return 5;
        } else if (var7 == 2) {
            return 3;
        } else {
            return var7 == 3 ? 4 : 0;
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public abstract BlockEntity createBlockEntity();
}
