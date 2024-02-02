package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.TileEntity;
import net.sunsetsatellite.retrostorage.util.DigitalNetwork;
import net.sunsetsatellite.retrostorage.util.TickTimer;

public class TileEntityDigitalController extends TileEntityNetworkDevice
{

    public TileEntityDigitalController() {
        network = new DigitalNetwork(this);
        networkReload = new TickTimer(network,"reload", 60,true);
    }

    @Override
    public void updateEntity() {
        networkReload.tick();
        //externalEnergy = (TileEntityEnergyAcceptor) getConnectedTileEntity(TileEntityEnergyAcceptor.class);
        if(network != null){
            if(externalEnergy == null){
                if(energy > 0){
                    energy -= network.devicesSize()+1;
                }
                if(energy <= 0){
                    if(energy < 0){
                        energy = 0;
                    }
                    if(!networkReload.isPaused()){
                        network.removeAll();
                        networkReload.pause();
                    }
                    active = false;
                } else {
                    if(networkReload.isPaused()){
                        networkReload.unpause();
                    }
                    active = true;
                }
            } else {
                /*if(externalEnergy.energy > 0){
                    externalEnergy.modifyEnergy((int) (-(network.devicesSize()+1)));
                }
                if(externalEnergy.energy <= 0){
                    if(!networkReload.isPaused()){
                        network.removeAll();
                        networkReload.pause();
                    }
                    active = false;
                } else {
                    if(networkReload.isPaused()){
                        networkReload.unpause();
                    }
                    active = true;
                }*/
            }

        } else {
            active = false;
        }

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        energy = nbttagcompound.getDouble("Energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        NBTTagDouble nbt = new NBTTagDouble("Energy",energy);
        nbttagcompound.setTag("Energy", nbt);
    }

    public double energy = 0;
    public boolean active = false;


    public TickTimer networkReload;
    public TileEntity externalEnergy;
}
