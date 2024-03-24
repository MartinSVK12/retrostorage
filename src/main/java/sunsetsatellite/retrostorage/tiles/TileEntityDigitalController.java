package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

public class TileEntityDigitalController extends TileEntityNetworkDevice
{

    public TileEntityDigitalController() {
        network = new DigitalNetwork(this);
        networkReload = new TickTimer(network,network::reload, 60,true);
    }

    @Override
    public void tick() {
        networkReload.tick();
        externalEnergy = (TileEntityEnergyAcceptor) getConnectedTileEntity(TileEntityEnergyAcceptor.class);
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
                if(externalEnergy.energy > 0){
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
                }
            }

        } else {
            active = false;
        }

    }

    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        energy = CompoundTag.getDouble("Energy");
    }

    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        DoubleTag nbt = new DoubleTag(energy);
        CompoundTag.put("Energy", nbt);
    }

    public double energy = 0;
    public boolean active = false;


    public TickTimer networkReload;
    public TileEntityEnergyAcceptor externalEnergy;
}
