package sunsetsatellite.retrostorage.tiles;



import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class TileEntityNetworkDevice extends TileEntity {
    public DigitalNetwork network = null;

    public HashMap<String,TileEntity> getConnectedTileEntity(ArrayList<Class<?>> allowedTileList){
        HashMap<String, TileEntity> sides = new HashMap<>();
        sides.put("X+", null);
        sides.put("X-", null);
        sides.put("Y+", null);
        sides.put("Y-", null);
        sides.put("Z+", null);
        sides.put("Z-", null);

        for (Map.Entry<String, Vec3i> entry : RetroStorage.directions.entrySet()) {
            String K = entry.getKey();
            Vec3i V = entry.getValue();

            TileEntity tile = worldObj.getBlockTileEntity(xCoord+V.x, yCoord+V.y, zCoord+V.z);
            if(tile != null){
                if(allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))){
                    sides.put(K,tile);
                }
            }
        }

        return sides;
    }

    public TileEntity getConnectedTileEntity(Class<?> allowedTile){
        HashMap<String, TileEntity> sides = new HashMap<>();

        for (Map.Entry<String, Vec3i> entry : RetroStorage.directions.entrySet()) {
            String K = entry.getKey();
            Vec3i V = entry.getValue();

            TileEntity tile = worldObj.getBlockTileEntity(xCoord+V.x, yCoord+V.y, zCoord+V.z);
            if(tile != null){
                if(allowedTile.isAssignableFrom(tile.getClass())){
                    return tile;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName()+"{" +
                "x=" + xCoord +
                ", y=" + yCoord +
                ", z=" + zCoord +
                '}';
    }

    public String toStringFormatted(){
        return this.getClass().getSimpleName()+" at "+
                "X=" + xCoord +
                ",Y=" + yCoord +
                ",Z=" + zCoord;
    }
}
