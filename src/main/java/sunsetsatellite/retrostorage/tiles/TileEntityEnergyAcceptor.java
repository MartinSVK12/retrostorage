package sunsetsatellite.retrostorage.tiles;


import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyContainer;

public class TileEntityEnergyAcceptor extends TileEntityEnergyContainer {

    public TileEntityEnergyAcceptor() {
        setCapacity(10000);
        setEnergy(0);
        setMaxProvide(0);
        setMaxReceive(1000);
        for (Direction dir : Direction.values()) {
            setConnection(dir, Connection.INPUT);
        }
    }
}
