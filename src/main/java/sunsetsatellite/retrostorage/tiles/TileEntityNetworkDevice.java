package sunsetsatellite.retrostorage.tiles;


import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

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

            TileEntity tile = worldObj.getBlockTileEntity(x+V.x, y+V.y, z+V.z);
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

            TileEntity tile = worldObj.getBlockTileEntity(x+V.x, y+V.y, z+V.z);
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
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public String toStringFormatted(){
        return this.getClass().getSimpleName()+" at "+
                "X=" + x +
                ",Y=" + y +
                ",Z=" + z;
    }
}
