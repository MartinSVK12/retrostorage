package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.network.Network;
import net.teamterminus.machineessentials.network.NetworkComponent;
import net.teamterminus.machineessentials.network.NetworkManager;
import net.teamterminus.machineessentials.network.NetworkType;
import net.teamterminus.machineessentials.util.BlockEntityInit;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.NetworkController;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class NetworkDeviceBlockEntity extends BlockEntity implements NetworkComponent, BlockEntityInit {

    public Network network;

    @Override
    public void init() {
        networkChanged(NetworkManager.getNet(world, x, y, z));
    }

    @Override
    public void tick() {
        //networkChanged(NetworkManager.getNet(world, x, y, z));
    }

    @Override
    public NetworkType getType() {
        return RetroStorage.RES_NETWORK;
    }

    @Override
    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return MachineEssentials.getBlockEntity(direction, world, this) instanceof NetworkDeviceBlockEntity;
    }

    @Override
    public void networkChanged(Network network) {
        this.network = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.network = null;
    }

    public NetworkController getController() {
        if(network != null) {
            NetworkController controller = network.findFirst(getPosition(), NetworkController.class, RetroStorage.RES_NETWORK);
            if(controller != null) {
                return controller.isActive() ? controller : null;
            }
        }
        return null;
    }

    public HashMap<Direction, BlockEntity> getConnectedBlockEntity(ArrayList<Class<?>> allowedTileList) {
        HashMap<Direction, BlockEntity> sides = new HashMap<>();
        for (Direction dir : Direction.values()) {
            BlockEntity tile = MachineEssentials.getBlockEntity(dir, world, this);
            if (tile != null) {
                if (allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))) {
                    sides.put(dir, tile);
                }
            }
        }
        return sides;
    }

    public <T> T getConnectedBlockEntity(Class<T> allowedTile) {

        for (Direction dir : Direction.values()) {
            BlockEntity tile = MachineEssentials.getBlockEntity(dir, world, this);
            if (tile != null) {
                if (allowedTile.isAssignableFrom(tile.getClass())) {
                    return allowedTile.cast(tile);
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getPosition();
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }
}
