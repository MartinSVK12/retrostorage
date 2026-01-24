package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Namespace;
import sunsetsatellite.retrostorage.item.*;

import java.util.HashMap;

public class ReSItems {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static HashMap<Item, String> itemTextures = new HashMap<>();

    public static Item storageDisc1;
    public static Item storageDisc2;
    public static Item storageDisc3;
    public static Item storageDisc4;
    public static Item storageDisc5;
    public static Item storageDisc6;

    public static Item fluidStorageDisc1;
    public static Item fluidStorageDisc2;
    public static Item fluidStorageDisc3;
    public static Item fluidStorageDisc4;
    public static Item fluidStorageDisc5;
    public static Item fluidStorageDisc6;

    public static Item recipeDisc;
    public static Item advRecipeDisc;

    public static Item blankDisc;
    public static Item machineCasing;
    public static Item advMachineCasing;
    public static Item energyCore;
    public static Item chipShell;
    public static Item chipShellFilled;
    public static Item chipDigitizer;
    public static Item chipCrafting;
    public static Item chipDematerializer;
    public static Item chipRematerializer;
    public static Item chipDieDigitizer;
    public static Item chipDieCrafting;
    public static Item chipDieRematerializer;
    public static Item chipDieDematerializer;
    public static Item silicon;
    public static Item siliconWafer;
    public static Item ceramicPlate;
    public static Item ceramicPlateUnfired;
    public static Item chipDieWireless;
    public static Item chipWireless;
    public static Item wirelessAntenna;
    public static Item redstoneCore;
    public static Item blankCard;

    public static Item mobileTerminal;
    public static Item mobileFluidTerminal;
    public static Item mobileRequestTerminal;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        storageDisc1 = customItem(new StorageDiscItem("storage_disc_1", 64, 64 * 64), "disc1", "disc1");
        storageDisc2 = customItem(new StorageDiscItem("storage_disc_2", 128, 128 * 64), "disc2", "disc2");
        storageDisc3 = customItem(new StorageDiscItem("storage_disc_3", 256, 256 * 64), "disc3", "disc3");
        storageDisc4 = customItem(new StorageDiscItem("storage_disc_4", 512, 512 * 64), "disc4", "disc4");
        storageDisc5 = customItem(new StorageDiscItem("storage_disc_5", 1024, 1024 * 64), "disc5", "disc5");
        storageDisc6 = customItem(new StorageDiscItem("storage_disc_6", 2048, 2048 * 64), "disc6", "disc6");

        fluidStorageDisc1 = customItem(new FluidStorageDiscItem("fluid_storage_disc_1", 2, 8000), "fluid_disc1", "fluid_disc_1");
        fluidStorageDisc2 = customItem(new FluidStorageDiscItem("fluid_storage_disc_2", 4, 16000), "fluid_disc2", "fluid_disc_2");
        fluidStorageDisc3 = customItem(new FluidStorageDiscItem("fluid_storage_disc_3", 6, 32000), "fluid_disc3", "fluid_disc_3");
        fluidStorageDisc4 = customItem(new FluidStorageDiscItem("fluid_storage_disc_4", 8, 64000), "fluid_disc4", "fluid_disc_4");
        fluidStorageDisc5 = customItem(new FluidStorageDiscItem("fluid_storage_disc_5", 10, 128000), "fluid_disc5", "fluid_disc_5");
        fluidStorageDisc6 = customItem(new FluidStorageDiscItem("fluid_storage_disc_6", 12, 256000), "fluid_disc6", "fluid_disc_6");

        recipeDisc = customItem(new RecipeDiscItem("recipe_disc"), "recipe_disc", "recipe_disc").setMaxCount(1);
        advRecipeDisc = customItem(new AdvRecipeDiscItem("adv_recipe_disc"), "adv_recipe_disc", "adv_recipe_disc").setMaxCount(1);

        mobileTerminal = customItem(new MobileTerminalItem("mobile_terminal"), "mobile_terminal", "mobile_terminal").setMaxCount(1);
        mobileFluidTerminal = customItem(new MobileTerminalItem("mobile_fluid_terminal"), "mobile_fluid_terminal", "mobile_fluid_terminal").setMaxCount(1);
        mobileRequestTerminal = customItem(new MobileTerminalItem("mobile_request_terminal"), "mobile_request_terminal", "mobile_request_terminal").setMaxCount(1);

        machineCasing = simpleItem("machine_casing", "machineCasing", "machine_casing");
        advMachineCasing = simpleItem("adv_machine_casing", "advMachineCasing", "adv_machine_casing");
        energyCore = simpleItem("energy_core", "energyCore", "energy_core");
        chipShell = simpleItem("chip_shell", "chipShell", "chip_shell");
        chipShellFilled = simpleItem("chip_shell_filled", "chipShellFilled", "chip_shell_filled");
        chipDigitizer = simpleItem("chip_digitizer", "chipDigitizer", "digitizer_chip");
        chipCrafting = simpleItem("chip_crafting", "chipCrafting", "crafting_chip");
        chipDematerializer = simpleItem("chip_dematerializer", "chipDematerializer", "dematerializer_chip");
        chipRematerializer = simpleItem("chip_rematerializer", "chipRematerializer", "rematerializer_chip");
        chipDieDigitizer = simpleItem("chip_die_digitizer", "chipDieDigitizer", "digitizer_die");
        chipDieCrafting = simpleItem("chip_die_crafting", "chipDieCrafting", "crafting_die");
        chipDieRematerializer = simpleItem("chip_die_rematerializer", "chipDieRematerializer", "rematerializer_die");
        chipDieDematerializer = simpleItem("chip_die_dematerializer", "chipDieDematerializer", "dematerializer_die");
        silicon = simpleItem("silicon", "silicon", "silicon");
        siliconWafer = simpleItem("silicon_wafer", "siliconWafer", "silicon_wafer");
        ceramicPlate = simpleItem("ceramic_plate", "ceramicPlate", "ceramic_plate");
        ceramicPlateUnfired = simpleItem("ceramic_plate_unfired", "ceramicPlateUnfired", "ceramic_plate_unfired");
        chipDieWireless = simpleItem("chip_die_wireless", "chipDieWireless", "wireless_die");
        chipWireless = simpleItem("chip_wireless", "chipWireless", "wireless_chip");
        wirelessAntenna = simpleItem("wireless_antenna", "wirelessAntenna", "wireless_antenna");
        redstoneCore = simpleItem("redstone_core", "redstoneCore", "redstone_core");
        blankCard = simpleItem("blank_card", "blankCard", "blank_card");
        blankDisc = simpleItem("blank_disc", "blankDisc", "blank_disc");
    }

    public Item simpleItem(String name, String lang, String texture) {
        Item item = new TemplateItem(NAMESPACE.id(name)).setTranslationKey(NAMESPACE, lang);
        itemTextures.put(item, "item/" + texture);
        return item;
    }

    public Item customItem(Item item, String lang, String texture) {
        item.setTranslationKey(NAMESPACE, lang);
        itemTextures.put(item, "item/" + texture);
        return item;
    }
}
