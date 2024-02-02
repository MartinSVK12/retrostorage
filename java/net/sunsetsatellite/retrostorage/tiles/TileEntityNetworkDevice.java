package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.sunsetsatellite.retrostorage.util.DigitalNetwork;
import net.sunsetsatellite.retrostorage.util.Direction;
import net.sunsetsatellite.retrostorage.util.Network;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityNetworkDevice extends TileEntity {
    public DigitalNetwork network;
    public int facing = 0;

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        facing = nbttagcompound.getInteger("facing");

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("facing",facing);

    }

    public HashMap<Direction, TileEntity> getConnectedTileEntities(ArrayList<Class<?>> allowedTileList){
        HashMap<Direction,TileEntity> map = new HashMap<>();
        for (Direction direction : Direction.values()) {
            TileEntity tileEntity = direction.getTileEntity(worldObj,this);
            if(tileEntity != null) {
                if (allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tileEntity.getClass()))) {
                    map.put(direction,tileEntity);
                } else {
                    map.put(direction,null);
                }
            } else {
                map.put(direction,null);
            }

        }
        return map;
    }

    public TileEntity getConnectedTileEntity(Class<?> allowedTile){
        for (Direction direction : Direction.values()) {
            TileEntity tileEntity = direction.getTileEntity(worldObj, this);
            if (tileEntity != null) {
                if (allowedTile.isAssignableFrom(tileEntity.getClass())) {
                    return tileEntity;
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
