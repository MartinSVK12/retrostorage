package sunsetsatellite.retrostorage.tiles;

import sunsetsatellite.energyapi.template.tiles.TileEntityBatteryBox;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;

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
