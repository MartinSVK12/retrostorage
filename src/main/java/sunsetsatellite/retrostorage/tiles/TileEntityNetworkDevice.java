package sunsetsatellite.retrostorage.tiles;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class TileEntityNetworkDevice extends TileEntity {
    public DigitalNetwork network = null;

    public HashMap<Direction,TileEntity> getConnectedTileEntity(ArrayList<Class<?>> allowedTileList){
        HashMap<Direction, TileEntity> sides = new HashMap<>();
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile != null) {
                if(allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))){
                    sides.put(dir,tile);
                }
            }
        }
        return sides;
    }

    public TileEntity getConnectedTileEntity(Class<?> allowedTile){

        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
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

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }
}
