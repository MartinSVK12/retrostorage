package sunsetsatellite.retrostorage.compat.whatsthis;

import net.danygames2014.whatsthis.event.BlockProbeInfoProviderRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ProbeInfoProviderListener {

    @EventListener
    public void registerBlockProbeInfoProviders(BlockProbeInfoProviderRegistryEvent event) {
        event.registerProvider(new NetworkDeviceProbeInfoProvider());
    }

}
