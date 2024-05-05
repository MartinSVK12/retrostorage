package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.util.DigitalNetwork;

public class TileEntityDigitalController extends TileEntityNetworkDevice
{

    public TileEntityDigitalController() {
        network = new DigitalNetwork(this);
    }

    @Override
    public void tick() {

        externalEnergy = (TileEntityEnergyAcceptor) getConnectedTileEntity(TileEntityEnergyAcceptor.class);
        if(network != null){
            if(!init){
                network.reload();
                init = true;
            }
            if(externalEnergy == null){
                if(energy > 0){
                    int cableSize = network.searchAll(TileEntityNetworkCable.class).size();
                    energy -= (network.devicesSize()-cableSize)+1;
                    network.tick();
                }
                if(energy <= 0){
                    if(energy < 0){
                        energy = 0;
                    }
                    if(network.inventory.sizeStacks() != 0){
                        network.inventory.clear();
                    }
                    network.removeAll();
                    active = false;
                } else {
                    active = true;
                }
            } else {
                if(externalEnergy.energy > 0){
                    int cableSize = network.searchAll(TileEntityNetworkCable.class).size();
                    externalEnergy.modifyEnergy((int) (-((network.devicesSize()-cableSize)+1)));
                    network.tick();
                }
                if(externalEnergy.energy <= 0){
                    network.removeAll();
                    active = false;
                    if(network.inventory.sizeStacks() != 0){
                        network.inventory.clear();
                    }
                } else {
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
    public boolean init = false;

    public TileEntityEnergyAcceptor externalEnergy;
}
