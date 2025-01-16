package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ReSTextures {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        ReSItems.blankDisc.setTexture(Identifier.of(NAMESPACE, "item/blank_disc"));
        ReSItems.storageDisc1.setTexture(Identifier.of(NAMESPACE, "item/disc1"));
        ReSItems.storageDisc2.setTexture(Identifier.of(NAMESPACE, "item/disc2"));
        ReSItems.storageDisc3.setTexture(Identifier.of(NAMESPACE, "item/disc3"));
        ReSItems.storageDisc4.setTexture(Identifier.of(NAMESPACE, "item/disc4"));
        ReSItems.storageDisc5.setTexture(Identifier.of(NAMESPACE, "item/disc5"));
        ReSItems.storageDisc6.setTexture(Identifier.of(NAMESPACE, "item/disc6"));
        ReSItems.recipeDisc.setTexture(Identifier.of(NAMESPACE, "item/recipe_disc"));
        ReSItems.advRecipeDisc.setTexture(Identifier.of(NAMESPACE, "item/adv_recipe_disc"));
    }
}
