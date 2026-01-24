package sunsetsatellite.retrostorage.event;

import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;

public class ReSNetworks {

    @EventListener
    public void registerNetworks(NetworkTypeRegistryEvent event) {
        event.register(NetworkDeviceBlock.RES_NETWORK);
    }

}
