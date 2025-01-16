package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;
import sunsetsatellite.retrostorage.item.RecipeDiscItem;
import sunsetsatellite.retrostorage.item.StorageDiscItem;

public class ReSItems {

    public static Item blankDisc;
    public static Item storageDisc1;
    public static Item storageDisc2;
    public static Item storageDisc3;
    public static Item storageDisc4;
    public static Item storageDisc5;
    public static Item storageDisc6;
    public static Item recipeDisc;
    public static Item advRecipeDisc;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        blankDisc = new TemplateItem(Identifier.of(NAMESPACE, "blank_disc")).setTranslationKey(NAMESPACE, "blank_disc");
        storageDisc1 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc1"),64,64 * 64).setTranslationKey(NAMESPACE, "disc1").setMaxCount(1);
        storageDisc2 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc2"),128, 128 * 64).setTranslationKey(NAMESPACE, "disc2").setMaxCount(1);
        storageDisc3 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc3"),256, 256 * 64).setTranslationKey(NAMESPACE, "disc3").setMaxCount(1);
        storageDisc4 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc4"),512, 512 * 64).setTranslationKey(NAMESPACE, "disc4").setMaxCount(1);
        storageDisc5 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc5"),1024, 1024 * 64).setTranslationKey(NAMESPACE, "disc5").setMaxCount(1);
        storageDisc6 = new StorageDiscItem(Identifier.of(NAMESPACE, "disc6"),2048, 2048 * 64).setTranslationKey(NAMESPACE, "disc6").setMaxCount(1);
        recipeDisc = new RecipeDiscItem(Identifier.of(NAMESPACE,"recipe_disc")).setTranslationKey(NAMESPACE, "recipe_disc").setMaxCount(1);
        advRecipeDisc = new AdvRecipeDiscItem(Identifier.of(NAMESPACE,"adv_recipe_disc")).setTranslationKey(NAMESPACE, "adv_recipe_disc").setMaxCount(1);
    }

}
