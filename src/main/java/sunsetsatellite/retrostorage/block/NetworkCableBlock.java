package sunsetsatellite.retrostorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldRegion;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import net.teamterminus.machineessentials.network.NetworkWire;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.NetworkCableBlockEntity;
import sunsetsatellite.retrostorage.block.entity.NetworkDeviceBlockEntity;

    public class NetworkCableBlock extends NetworkDeviceBlock {
    public NetworkCableBlock(Identifier identifier, Material material) {
        super(identifier, material);
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
    public void updateBoundingBox(BlockView world, int x, int y, int z) {
        float bx = 0.3f, by = 0.3f, bz = 0.3f;
        float tx = 0.7f, ty = 0.7f, tz = 0.7f;
        // Loop de-loop
        for (Direction dir : Direction.values()) {
            Vec3i v = dir.getVector();
            BlockEntity te = world.getBlockEntity(x + v.getX(), y + v.getY(), z + v.getZ());
            //noinspection ConditionCoveredByFurtherCondition
            if (
                    te == null || (!(te instanceof NetworkCableBlockEntity) && !(te instanceof NetworkDeviceBlockEntity) )
            ) continue;
            if (v.getX() > 0) tx = 1.0f;
            else if (v.getX() < 0) bx = 0.0f;
            if (v.getZ() > 0) tz = 1.0f;
            else if (v.getZ() < 0) bz = 0.0f;
            if (v.getY() > 0) ty = 1.0f;
            else if (v.getY() < 0) by = 0.0f;
        }
        setBoundingBox(bx, by, bz, tx, ty, tz);
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        updateBoundingBox(world, x, y, z);
        return super.getBoundingBox(world, x, y, z);
    }

    @Override
    public void onPlaced(World level, int x, int y, int z, LivingEntity living) {

    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {

    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new NetworkCableBlockEntity();
    }
}
