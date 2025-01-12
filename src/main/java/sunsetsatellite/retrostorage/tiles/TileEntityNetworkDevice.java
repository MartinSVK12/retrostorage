package sunsetsatellite.retrostorage.tiles;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.core.util.network.Network;
import sunsetsatellite.catalyst.core.util.network.NetworkComponentTile;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TileEntityNetworkDevice extends TileEntity implements NetworkComponentTile {

    public Network network;

    @Override
    public NetworkType getType() {
        return NetworkType.RES_NETWORK;
    }

    @Override
    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return direction.getTileEntity(worldObj,this) instanceof TileEntityNetworkDevice;
    }

    @Override
    public void networkChanged(Network network) {
        this.network = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.network = null;
    }

    public INetworkController getController() {
        return network != null ? network.findFirst(getPosition(),INetworkController.class) : null;
    }

    public HashMap<Direction, TileEntity> getConnectedTileEntity(ArrayList<Class<?>> allowedTileList) {
        HashMap<Direction, TileEntity> sides = new HashMap<>();
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile != null) {
                if (allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))) {
                    sides.put(dir, tile);
                }
            }
        }
        return sides;
    }

    public <T> T getConnectedTileEntity(Class<T> allowedTile) {

        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
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

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if (worldObj.getBlockTileEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }
}
