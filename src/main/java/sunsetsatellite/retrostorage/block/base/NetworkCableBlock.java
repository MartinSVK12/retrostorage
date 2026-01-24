package sunsetsatellite.retrostorage.block.base;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkNodeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class NetworkCableBlock extends TemplateBlock implements NetworkNodeComponent {

    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public static final HashMap<Direction, BooleanProperty> PROPERTY_LOOKUP = new HashMap<>();

    static {
        PROPERTY_LOOKUP.put(Direction.UP, UP);
        PROPERTY_LOOKUP.put(Direction.DOWN, DOWN);
        PROPERTY_LOOKUP.put(Direction.NORTH, NORTH);
        PROPERTY_LOOKUP.put(Direction.SOUTH, SOUTH);
        PROPERTY_LOOKUP.put(Direction.EAST, EAST);
        PROPERTY_LOOKUP.put(Direction.WEST, WEST);
    }

    public NetworkCableBlock(String identifier) {
        super(RetroStorage.NAMESPACE.id(identifier), Material.WOOL);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState()
                .with(UP, false)
                .with(DOWN, false)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false);
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        updateConnections(world, x, y, z);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        updateConnections(world, x, y, z);
    }

    public void updateConnections(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        for (Direction side : Direction.values()) {
            state = state.with(PROPERTY_LOOKUP.get(side), this.canConnectTo(world, x, y, z, null, side));
        }

        world.setBlockState(x, y, z, state);
    }

    @Override
    public boolean canConnectTo(World world, int x, int y, int z, @Nullable Network network, Direction direction) {
        Block block = sunsetsatellite.catalyst.core.util.Direction.get(direction).getBlock(world, new Vec3i(x, y, z));
        return block instanceof NetworkDeviceBlock || block instanceof NetworkCableBlock;
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        float minX = 0.34375F;
        float minY = 0.34375F;
        float minZ = 0.34375F;

        float maxX = 0.65625F;
        float maxY = 0.65625F;
        float maxZ = 0.65625F;

        if (state.get(UP)) {
            maxY = 1.0F;
        }

        if (state.get(DOWN)) {
            minY = 0.0F;
        }

        if (state.get(WEST)) {
            maxZ = 1.0F;
        }

        if (state.get(EAST)) {
            minZ = 0.0F;
        }

        if (state.get(NORTH)) {
            minX = 0.0F;
        }

        if (state.get(SOUTH)) {
            maxX = 1.0F;
        }

        return Box.createCached(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }

    @Override
    public void addIntersectingBoundingBox(World world, int x, int y, int z, Box box, ArrayList boxes) {
        BlockState state = world.getBlockState(x, y, z);

        if (state.get(UP)) {
            this.setBoundingBox(0.34375F, 0.34375F, 0.34375F, 0.65625F, 1.0F, 0.65625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(DOWN)) {
            this.setBoundingBox(0.34375F, 0.0F, 0.34375F, 0.65625F, 0.65625F, 0.65625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(WEST)) {
            this.setBoundingBox(0.34375F, 0.34375F, 0.34375F, 0.65625F, 0.65625F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(EAST)) {
            this.setBoundingBox(0.34375F, 0.34375F, 0.0F, 0.65625F, 0.65625F, 0.65625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(SOUTH)) {
            this.setBoundingBox(0.34375F, 0.34375F, 0.34375F, 1.0F, 0.65625F, 0.65625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(NORTH)) {
            this.setBoundingBox(0.0F, 0.34375F, 0.34375F, 0.65625F, 0.65625F, 0.65625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    public HitResult raycast(World world, int x, int y, int z, Vec3d startPos, Vec3d endPos) {
        Box box = getBoundingBox(world, x, y, z).expand(0.05D, 0.05D, 0.05D);

        this.updateBoundingBox(world, x, y, z);

        HitResult hitResult = box.raycast(startPos, endPos);

        if (hitResult == null) {
            return null;
        }

        if (hitResult.blockX == 0 && hitResult.blockY == 0 && hitResult.blockZ == 0) {
            return new HitResult(x, y, z, hitResult.side, hitResult.pos);
        }

        return hitResult;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkDeviceBlock.RES_NETWORK;
    }
}
