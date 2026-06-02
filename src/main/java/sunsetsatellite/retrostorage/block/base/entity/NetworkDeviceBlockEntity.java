package sunsetsatellite.retrostorage.block.base.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.packet.SendControllerPacket;
import sunsetsatellite.retrostorage.util.ReSNetwork;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class NetworkDeviceBlockEntity extends BlockEntity {

    public ReSNetwork network;
    public NetworkController controller = null;

    public NetworkController getController() {
        if (world.isRemote) {
            return controller;
        }
        if (network != null) {
            NetworkController controller = network.findFirst(NetworkController.class);
            if (controller == null) return null;
            if(Catalyst.serverEnv() && controller.isActive()){
                for (Object entity : world.players) {
                    if(entity instanceof PlayerEntity player){
                        PacketHelper.sendTo(player, new SendControllerPacket(getPosition(), controller.getPosition()));
                    }
                }
            }
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
