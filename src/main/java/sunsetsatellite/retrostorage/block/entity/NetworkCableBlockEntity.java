package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.network.*;
import net.teamterminus.machineessentials.util.BlockEntityInit;
import sunsetsatellite.retrostorage.RetroStorage;

public class NetworkCableBlockEntity extends BlockEntity implements NetworkComponent, NetworkWire, BlockEntityInit {

    public Network network;


    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return MachineEssentials.getBlockEntity(direction,world,this) instanceof NetworkCableBlockEntity || MachineEssentials.getBlockEntity(direction,world,this) instanceof NetworkDeviceBlockEntity;
    }

    @Override
    public void networkChanged(Network network) {
        this.network = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.network = null;
    }

    @Override
    public NetworkType getType() {
        return RetroStorage.RES_NETWORK;
    }

    @Override
    public void init() {
        networkChanged(NetworkManager.getNet(world, x, y, z));
    }

    @Override
    public void tick() {
        super.tick();
        //networkChanged(NetworkManager.getNet(world, x, y, z));
    }
}
