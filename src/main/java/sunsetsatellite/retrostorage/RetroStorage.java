package sunsetsatellite.retrostorage;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class RetroStorage {
    @Entrypoint.Instance
    public static final RetroStorage INSTANCE = Null.get();

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @EventListener
    void onInit(InitEvent event) {
        LOGGER.info("Initializing mod " + NAMESPACE + " with current instance " + INSTANCE);
    }
}
