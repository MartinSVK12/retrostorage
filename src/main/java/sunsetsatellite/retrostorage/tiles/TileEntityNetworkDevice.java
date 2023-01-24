package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.TileEntity;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

public abstract class TileEntityNetworkDevice extends TileEntity {
    public DigitalNetwork network = null;

    @Override
    public String toString() {
        return this.getClass().getTypeName()+"{" +
                "x=" + xCoord +
                ", y=" + yCoord +
                ", z=" + zCoord +
                '}';
    }
}
