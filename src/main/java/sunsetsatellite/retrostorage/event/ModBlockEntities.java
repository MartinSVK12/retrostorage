package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.block.entity.DigitalChestBlockEntity;

public class ModBlockEntities {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(DigitalChestBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "DigitalChest")));
    }
}
