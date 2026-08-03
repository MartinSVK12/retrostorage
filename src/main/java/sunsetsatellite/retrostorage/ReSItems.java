package sunsetsatellite.retrostorage;

import net.minecraft.core.item.Item;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.retrostorage.items.*;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.HashMap;
import java.util.function.Supplier;

import static sunsetsatellite.retrostorage.ReSConfig.item;
import static sunsetsatellite.retrostorage.RetroStorage.*;

public class ReSItems extends DataInitializer {

    public static HashMap<Item, String> itemTextures = new HashMap<>();

    public static Item blankDisc;
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
    public static Item goldenDisc;
    public static Item azureDisc;
    public static Item advRecipeDisc;

    public static Item slotIdFinder;
    public static Item mobileTerminal;
    public static Item mobileFluidTerminal;
    public static Item mobileRequestTerminal;

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
    public static Item linkingCard;
    public static Item blankCard;

    public void afterItemInit() {
        init();
    }

    public Item simpleItem(String configId, String name, String lang, String texture) {
        Item item = new Item(lang, key("item/"+name), item(configId));
        itemTextures.put(item, texture);
        //LOGGER.info("Registering item '" + item.namespaceID.toString() + "' with texture 'signalindustries:item/" + texture + "'.");
        return new ItemBuilder(MOD_ID)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(item);
    }

    public Item customItem(Supplier<Item> itemSupplier, String texture) {
        Item item = itemSupplier.get();
        itemTextures.put(item, texture);
        //LOGGER.info("Registering item '" + item.namespaceID.toString() + "' with texture 'signalindustries:item/" + texture + "'.");
        return new ItemBuilder(MOD_ID)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(item);
    }

    @Override
    public void init() {
        if (initialized) return;
        RetroStorage.LOGGER.info("Initializing items...");

        machineCasing = simpleItem("machineCasing", "machine_casing", "machineCasing", "machine_casing");
        advMachineCasing = simpleItem("advMachineCasing", "adv_machine_casing", "advMachineCasing", "adv_machine_casing");
        energyCore = simpleItem("energyCore", "energy_core", "energyCore", "energy_core");
        chipShell = simpleItem("chipShell", "chip_shell", "chipShell", "chip_shell");
        chipShellFilled = simpleItem("chipShellFilled", "chip_shell_filled", "chipShellFilled", "chip_shell_filled");
        chipDigitizer = simpleItem("chipDigitizer", "chip_digitizer", "chipDigitizer", "digitizer_chip");
        chipCrafting = simpleItem("chipCrafting", "chip_crafting", "chipCrafting", "crafting_chip");
        chipDematerializer = simpleItem("chipDematerializer", "chip_dematerializer", "chipDematerializer", "dematerializer_chip");
        chipRematerializer = simpleItem("chipRematerializer", "chip_rematerializer", "chipRematerializer", "rematerializer_chip");
        chipDieDigitizer = simpleItem("chipDieDigitizer", "chip_die_digitizer", "chipDieDigitizer", "digitizer_die");
        chipDieCrafting = simpleItem("chipDieCrafting", "chip_die_crafting", "chipDieCrafting", "crafting_die");
        chipDieRematerializer = simpleItem("chipDieRematerializer", "chip_die_rematerializer", "chipDieRematerializer", "rematerializer_die");
        chipDieDematerializer = simpleItem("chipDieDematerializer", "chip_die_dematerializer", "chipDieDematerializer", "dematerializer_die");
        silicon = simpleItem("silicon", "silicon", "silicon", "silicon");
        siliconWafer = simpleItem("siliconWafer", "silicon_wafer", "siliconWafer", "silicon_wafer");
        ceramicPlate = simpleItem("ceramicPlate", "ceramic_plate", "ceramicPlate", "ceramic_plate");
        ceramicPlateUnfired = simpleItem("ceramicPlateUnfired", "ceramic_plate_unfired", "ceramicPlateUnfired", "ceramic_plate_unfired");
        chipDieWireless = simpleItem("chipDieWireless", "chip_die_wireless", "chipDieWireless", "wireless_die");
        chipWireless = simpleItem("chipWireless", "chip_wireless", "chipWireless", "wireless_chip");
        wirelessAntenna = simpleItem("wirelessAntenna", "wireless_antenna", "wirelessAntenna", "wireless_antenna");
        redstoneCore = simpleItem("redstoneCore", "redstone_core", "redstoneCore", "redstone_core");
        linkingCard = simpleItem("linkingCard", "linking_card", "linkingCard", "linking_card");
        blankCard = simpleItem("blankCard", "blank_card", "blankCard", "blank_card");
        blankDisc = simpleItem("blankDisc", "blank_disc", "blankDisc", "blank_disc");

        storageDisc1 = customItem(()-> new ItemStorageDisc("storageDisc1",key("item/storage_disc_1"),item("storageDisc1"),64,64*64), "disc1");
        storageDisc2 = customItem(()-> new ItemStorageDisc("storageDisc2",key("item/storage_disc_2"),item("storageDisc2"),128,128*64), "disc2");
        storageDisc3 = customItem(()-> new ItemStorageDisc("storageDisc3",key("item/storage_disc_3"),item("storageDisc3"),256,256*64), "disc3");
        storageDisc4 = customItem(()-> new ItemStorageDisc("storageDisc4",key("item/storage_disc_4"),item("storageDisc4"),512,512*64), "disc4");
        storageDisc5 = customItem(()-> new ItemStorageDisc("storageDisc5",key("item/storage_disc_5"),item("storageDisc5"),1024,1024*64), "disc5");
        storageDisc6 = customItem(()-> new ItemStorageDisc("storageDisc6",key("item/storage_disc_6"),item("storageDisc6"),2048,2048*64), "disc6");

        fluidStorageDisc1 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc1",key("item/fluid_storage_disc_1"),item("fluidStorageDisc1"),2,8000), "fluid_disc_1");
        fluidStorageDisc2 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc2",key("item/fluid_storage_disc_2"),item("fluidStorageDisc2"),4,16000), "fluid_disc_2");
        fluidStorageDisc3 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc3",key("item/fluid_storage_disc_3"),item("fluidStorageDisc3"),6,32000), "fluid_disc_3");
        fluidStorageDisc4 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc4",key("item/fluid_storage_disc_4"),item("fluidStorageDisc4"),8,64000), "fluid_disc_4");
        fluidStorageDisc5 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc5",key("item/fluid_storage_disc_5"),item("fluidStorageDisc5"),10,128000), "fluid_disc_5");
        fluidStorageDisc6 = customItem(()-> new ItemFluidStorageDisc("fluidStorageDisc6",key("item/fluid_storage_disc_6"),item("fluidStorageDisc6"),12,256000), "fluid_disc_6");

        goldenDisc = customItem(()-> new ItemStorageDisc("goldenDisc",key("item/golden_disc"),item("goldenDisc"),8192, 8192 * 64), "golden_disc");
        azureDisc = customItem(()-> new ItemFluidStorageDisc("azureDisc",key("item/azure_disc"),item("azureDisc"),64, 1024000), "azure_disc");
        recipeDisc = customItem(()-> new ItemRecipeDisc("recipeDisc",key("item/recipe_disc"),item("recipeDisc")),"recipe_disc");
        advRecipeDisc = customItem(()-> new ItemAdvRecipeDisc("advRecipeDisc",key("item/adv_recipe_disc"),item("advRecipeDisc")),"adv_recipe_disc");

        slotIdFinder = simpleItem("slotIdFinder", "slot_id_finder", "slotIdFinder", "id_finder");
        mobileTerminal = customItem(()-> new ItemMobileTerminal("mobileTerminal",key("item/mobile_terminal"),item("mobileTerminal")),"mobile_terminal");
        mobileFluidTerminal = customItem(()-> new ItemMobileTerminal("mobileFluidTerminal",key("item/mobile_fluid_terminal"),item("mobileFluidTerminal")),"mobile_fluid_terminal");
        mobileRequestTerminal = customItem(()-> new ItemMobileTerminal("mobileRequestTerminal",key("item/mobile_request_terminal"),item("mobileRequestTerminal")),"mobile_request_terminal");

        setInitialized(true);
    }
}
