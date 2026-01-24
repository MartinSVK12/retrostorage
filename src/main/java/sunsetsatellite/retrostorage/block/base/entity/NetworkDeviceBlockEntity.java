package sunsetsatellite.retrostorage.block.base.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.util.ReSNetwork;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class NetworkDeviceBlockEntity extends BlockEntity {

    public ReSNetwork network;

    public NetworkController getController() {
        if (network != null) {
            NetworkController controller = network.findFirst(NetworkController.class);
            if (controller == null) return null;
            return controller.isActive() ? controller : null;
        }
        return null;
    }

    public HashMap<Direction, BlockEntity> getConnectedTileEntity(ArrayList<Class<?>> allowedTileList) {
        HashMap<Direction, BlockEntity> sides = new HashMap<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tile = dir.getTileEntity(world, this);
            if (tile != null) {
                if (allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))) {
                    sides.put(dir, tile);
                }
            }
        }
        return sides;
    }

    public <T> T getConnectedTileEntity(Class<T> clazz) {
        for (Direction dir : Direction.values()) {
            BlockEntity entity = dir.getTileEntity(world, this);
            if (entity == null) continue;
            if (clazz.isAssignableFrom(entity.getClass())) {
                return clazz.cast(entity);
            }
        }
        return null;
    }

    public boolean canUse(PlayerEntity player) {
        return player.getSquaredDistance(x + 0.5d, y + 0.5d, z + 0.5d) <= 64;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getPosition();
    }

    public Vec3i getPosition() {
        return new Vec3i(x, y, z);
    }

    public abstract String getName();
}
