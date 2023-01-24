package sunsetsatellite.retrostorage.util;

import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

import java.util.ArrayDeque;

/**
 * Class for a digital storage network.
 */
public class DigitalNetwork extends Network {
    /**
     * Creates a new digital network with <i>controller</i> as its controller
     *
     * @param controller  Controller of the network
     */
    public InventoryDigital inventory;
    public ArrayDeque<Task> requestQueue = new ArrayDeque<>();
    public TileEntityDiscDrive drive;

    public DigitalNetwork(TileEntityDigitalController controller) {
        super(controller, TileEntityNetworkDevice.class, new int[]{RetroStorage.networkCable.blockID});
        this.inventory = new InventoryDigital("Digital Network",controller);
    }

    @Override
    public void add(BlockInstance device) {
        super.add(device);
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = this;
        }
        if(device.tile instanceof TileEntityDiscDrive){
            DiscManipulator.loadDisc(((TileEntityDiscDrive) device.tile).virtualDisc,inventory);
        }
    }

    @Override
    public void remove(BlockInstance device) {
        super.remove(device);
        if(device.tile == drive){
            drive = null;
        }
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = null;
        }
    }
}
