package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.item.StorageDiscItem;

public class ModItems {

    public static Item blankDisc;
    public static Item storageDisc1;
    public static Item storageDisc2;
    public static Item storageDisc3;
    public static Item storageDisc4;
    public static Item storageDisc5;
    public static Item storageDisc6;

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        blankDisc = new TemplateItem(Identifier.of(NAMESPACE, "blankDisc")).setTranslationKey(NAMESPACE, "blankDisc");
        storageDisc1 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc1"),64).setTranslationKey(NAMESPACE, "disc1").setMaxCount(1);
        storageDisc2 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc2"),64*2).setTranslationKey(NAMESPACE, "disc2").setMaxCount(1);
        storageDisc3 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc3"),64*3).setTranslationKey(NAMESPACE, "disc3").setMaxCount(1);
        storageDisc4 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc4"),64*4).setTranslationKey(NAMESPACE, "disc4").setMaxCount(1);
        storageDisc5 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc5"),64*5).setTranslationKey(NAMESPACE, "disc5").setMaxCount(1);
        storageDisc6 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc6"),64*6).setTranslationKey(NAMESPACE, "disc6").setMaxCount(1);
    }

}
