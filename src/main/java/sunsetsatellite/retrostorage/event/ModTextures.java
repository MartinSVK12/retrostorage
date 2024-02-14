package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ModTextures {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ModItems.blankDisc.setTexture(Identifier.of(NAMESPACE, "item/blank_disc"));
        ModItems.storageDisc1.setTexture(Identifier.of(NAMESPACE, "item/disc1"));
        ModItems.storageDisc2.setTexture(Identifier.of(NAMESPACE, "item/disc2"));
        ModItems.storageDisc3.setTexture(Identifier.of(NAMESPACE, "item/disc3"));
        ModItems.storageDisc4.setTexture(Identifier.of(NAMESPACE, "item/disc4"));
        ModItems.storageDisc5.setTexture(Identifier.of(NAMESPACE, "item/disc5"));
        ModItems.storageDisc6.setTexture(Identifier.of(NAMESPACE, "item/disc6"));
    }
}
