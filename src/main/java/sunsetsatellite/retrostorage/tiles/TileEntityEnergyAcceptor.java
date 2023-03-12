package sunsetsatellite.retrostorage.tiles;

import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;
import sunsetsatellite.energyapi.template.tiles.TileEntityBatteryBox;
import sunsetsatellite.energyapi.util.Connection;
import sunsetsatellite.energyapi.util.Direction;

public class TileEntityEnergyAcceptor extends TileEntityBatteryBox {

    public TileEntityEnergyAcceptor(){
        setCapacity(10000);
        setEnergy(0);
        setMaxProvide(0);
        setMaxReceive(1000);
        for (Direction dir : Direction.values()) {
            setConnection(dir, Connection.INPUT);
        }
    }

    @Override
    public String getInvName() {
        return "Energy Acceptor";
    }
}
