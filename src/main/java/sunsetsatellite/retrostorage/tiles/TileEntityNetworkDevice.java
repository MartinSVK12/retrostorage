package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.BlockInstance;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class TileEntityNetworkDevice extends TileEntity {
    public DigitalNetwork network = null;

    public HashMap<String,TileEntity> getConnectedTileEntity(ArrayList<Class<? extends TileEntity>> allowedTileList){
        HashMap<String, TileEntity> sides = new HashMap<>();
        sides.put("X+", null);
        sides.put("X-", null);
        sides.put("Y+", null);
        sides.put("Y-", null);
        sides.put("Z+", null);
        sides.put("Z-", null);

        for (Map.Entry<String, Vec3> entry : RetroStorage.directions.entrySet()) {
            String K = entry.getKey();
            Vec3 V = entry.getValue();

            TileEntity tile = worldObj.getBlockTileEntity(xCoord+V.x, yCoord+V.y, zCoord+V.z);
            if(tile != null){
                if(allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))){
                    sides.put(K,tile);
                }
            }
        }

        return sides;
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName()+"{" +
                "x=" + xCoord +
                ", y=" + yCoord +
                ", z=" + zCoord +
                '}';
    }
}
