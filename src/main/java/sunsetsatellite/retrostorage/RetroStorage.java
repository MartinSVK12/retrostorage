package sunsetsatellite.retrostorage;


import com.mojang.nbt.CompoundTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.helper.DyeColor;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.model.block.DFBlockModelBuilder;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.core.util.MpGuiEntry;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.FluidType;
import sunsetsatellite.catalyst.multipart.block.model.BlockModelMultipart;
import sunsetsatellite.catalyst.multipart.block.model.MultipartBlockModelBuilder;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.blocks.models.BlockModelRedstoneEmitter;
import sunsetsatellite.retrostorage.blocks.states.NetworkCableStateInterpreter;
import sunsetsatellite.retrostorage.containers.*;
import sunsetsatellite.retrostorage.gui.*;
import sunsetsatellite.retrostorage.items.*;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.InventoryAutocrafting;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;
import sunsetsatellite.retrostorage.util.crafting.CraftableType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RetroStorage implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TomlConfigHandler config;

    public static Minecraft mc = null;

    public static int nextItemId = 18000;
    public static int nextBlockId = 1400;

    public static int itemIdStart = 18000;
    public static int blockIdStart = 1400;

    public static final Set<FluidType> DISALLOWED_FLUIDS = new HashSet<>();

    public static final Tag<Block> NETWORK_CABLES_CONNECT = Tag.of("network_cables_connect");

    /*static {
        Toml configToml = new Toml("RetroStorage configuration file.");
        configToml.addCategory("BlockIDs");
        configToml.addCategory("ItemIDs");
        configToml.addCategory("Other");
        configToml.addEntry("Other.goldenDiscLoot", false);

        List<Field> blockFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        for (Field blockField : blockFields) {
            configToml.addEntry("BlockIDs." + blockField.getName(), nextBlockId++);
        }
        List<Field> itemFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        for (Field itemField : itemFields) {
            configToml.addEntry("ItemIDs." + itemField.getName(), nextItemId++);
        }

        config = new TomlConfigHandler(MOD_ID, configToml);

        //this is here to possibly fix some class loading issues, do not delete
        try {
            Class.forName("net.minecraft.block.Block");
            Class.forName("net.minecraft.item.Item");
        } catch (ClassNotFoundException ignored) {
        }
    }*/

    static {
        List<Field> blockFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

        Toml defaultConfig = new Toml("RetroStorage configuration file.");
        defaultConfig.addCategory("BlockIDs");
        defaultConfig.addCategory("ItemIDs");
        defaultConfig.addCategory("Other");
        defaultConfig.addEntry("Other.goldenDiscLoot", false);

        int blockId = blockIdStart;
        int itemId = itemIdStart;
        for (Field blockField : blockFields) {
            defaultConfig.addEntry("BlockIDs." + blockField.getName(), blockId++);
        }
        for (Field itemField : itemFields) {
            defaultConfig.addEntry("ItemIDs." + itemField.getName(), itemId++);
        }

        config = new TomlConfigHandler(MOD_ID, new Toml("RetroStorage configuration file."), false);

        File configFile = config.getConfigFile();

        if (config.getConfigFile().exists()) {
            config.loadConfig();
            config.setDefaults(config.getRawParsed());
            Toml rawConfig = config.getRawParsed();
            int maxBlocks = ((Toml) rawConfig.get(".BlockIDs")).getOrderedKeys().size();
            int maxItems = ((Toml) rawConfig.get(".ItemIDs")).getOrderedKeys().size();
            int newNextBlockId = blockIdStart + maxBlocks;
            int newNextItemId = itemIdStart + maxItems;
            boolean changed = false;

            for (Field F : blockFields) {
                if (!rawConfig.contains("BlockIDs." + F.getName())) {
                    rawConfig.addEntry("BlockIDs." + F.getName(), newNextBlockId++);
                    changed = true;
                }
            }
            for (Field F : itemFields) {
                if (!rawConfig.contains("ItemIDs." + F.getName())) {
                    rawConfig.addEntry("ItemIDs." + F.getName(), newNextItemId++);
                    changed = true;
                }
            }

            if (!rawConfig.contains("Other.goldenDiscLoot")) {
                rawConfig.addEntry("Other.goldenDiscLoot", false);
                changed = true;
            }

            if (changed) {
                config.setDefaults(rawConfig);
                config.writeConfig();
                config.loadConfig();
            }
        } else {
            config.setDefaults(defaultConfig);
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                config.writeConfig();
                config.loadConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate config!", e);
            }
        }
    }

    public static final Item blankDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/blankdisc").build(new Item("blankDisc", config.getInt("ItemIDs.blankDisc")));
    public static final Item storageDisc1 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc1").build(new ItemStorageDisc("storageDisc1", config.getInt("ItemIDs.storageDisc1"), 64, 64 * 64)).setMaxStackSize(1);
    public static final Item storageDisc2 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc2").build(new ItemStorageDisc("storageDisc2", config.getInt("ItemIDs.storageDisc2"), 128, 128 * 64)).setMaxStackSize(1);
    public static final Item storageDisc3 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc3").build(new ItemStorageDisc("storageDisc3", config.getInt("ItemIDs.storageDisc3"), 256, 256 * 64)).setMaxStackSize(1);
    public static final Item storageDisc4 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc4").build(new ItemStorageDisc("storageDisc4", config.getInt("ItemIDs.storageDisc4"), 512, 512 * 64)).setMaxStackSize(1);
    public static final Item storageDisc5 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc5").build(new ItemStorageDisc("storageDisc5", config.getInt("ItemIDs.storageDisc5"), 1024, 1024 * 64)).setMaxStackSize(1);
    public static final Item storageDisc6 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/disc6").build(new ItemStorageDisc("storageDisc6", config.getInt("ItemIDs.storageDisc6"), 2048, 2048 * 64)).setMaxStackSize(1);
    public static final Item fluidStorageDisc1 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_1").build(new ItemFluidStorageDisc("fluidStorageDisc1", config.getInt("ItemIDs.fluidStorageDisc1"), 2, 2000)).setMaxStackSize(1);
    public static final Item fluidStorageDisc2 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_2").build(new ItemFluidStorageDisc("fluidStorageDisc2", config.getInt("ItemIDs.fluidStorageDisc2"), 4, 4000)).setMaxStackSize(1);
    public static final Item fluidStorageDisc3 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_3").build(new ItemFluidStorageDisc("fluidStorageDisc3", config.getInt("ItemIDs.fluidStorageDisc3"), 6, 8000)).setMaxStackSize(1);
    public static final Item fluidStorageDisc4 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_4").build(new ItemFluidStorageDisc("fluidStorageDisc4", config.getInt("ItemIDs.fluidStorageDisc4"), 8, 16000)).setMaxStackSize(1);
    public static final Item fluidStorageDisc5 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_5").build(new ItemFluidStorageDisc("fluidStorageDisc5", config.getInt("ItemIDs.fluidStorageDisc5"), 10, 32000)).setMaxStackSize(1);
    public static final Item fluidStorageDisc6 = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/fluid_disc_6").build(new ItemFluidStorageDisc("fluidStorageDisc6", config.getInt("ItemIDs.fluidStorageDisc6"), 12, 64000)).setMaxStackSize(1);

    //public static final Item virtualDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/virtualdisc").build(new ItemStorageDisc("virtualDisc", config.getInt("ItemIDs.virtualDisc"), Short.MAX_VALUE * 2, (Short.MAX_VALUE * 2) * 64).withTags(ItemTags.NOT_IN_CREATIVE_MENU)).setMaxStackSize(1);
    //public static final Item virtualFluidDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/virtualdisc").build(new ItemFluidStorageDisc("virtualFluidDisc", config.getInt("ItemIDs.virtualFluidDisc"), Short.MAX_VALUE * 2, Integer.MAX_VALUE).withTags(ItemTags.NOT_IN_CREATIVE_MENU)).setMaxStackSize(1);
    public static final Item recipeDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/recipedisc").build(new ItemRecipeDisc("recipeDisc", config.getInt("ItemIDs.recipeDisc"))).setMaxStackSize(1);
    public static final Item goldenDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/goldendisc").build(new ItemStorageDisc("goldenDisc", config.getInt("ItemIDs.goldenDisc"), 8192, 8192 * 64)).setMaxStackSize(1);
    public static final Item azureDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/azuredisc").build(new ItemFluidStorageDisc("azureDisc", config.getInt("ItemIDs.azureDisc"), 64, 1024000)).setMaxStackSize(1);
    public static final Item advRecipeDisc = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/advrecipedisc").build(new ItemAdvRecipeDisc("advRecipeDisc", config.getInt("ItemIDs.advRecipeDisc"))).setMaxStackSize(1);
    public static final Item machineCasing = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/machinecasing").build(new Item("machineCasing", config.getInt("ItemIDs.machineCasing")));
    public static final Item advMachineCasing = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/advmachinecasing").build(new Item("advMachineCasing", config.getInt("ItemIDs.advMachineCasing")));
    public static final Item energyCore = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/energycore").build(new Item("energyCore", config.getInt("ItemIDs.energyCore")));
    public static final Item chipShell = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/chipshell").build(new Item("chipShell", config.getInt("ItemIDs.chipShell")));
    public static final Item chipShellFilled = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/filledchipshell").build(new Item("chipShellFilled", config.getInt("ItemIDs.chipShellFilled")));
    public static final Item chipDigitizer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/digitizerchip").build(new Item("chipDigitizer", config.getInt("ItemIDs.chipDigitizer")));
    public static final Item chipCrafting = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/craftingprocessor").build(new Item("chipCrafting", config.getInt("ItemIDs.chipCrafting")));
    public static final Item chipDematerializer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/dematerializerchip").build(new Item("chipDematerializer", config.getInt("ItemIDs.chipDematerializer")));
    public static final Item chipRematerializer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/rematerializerchip").build(new Item("chipRematerializer", config.getInt("ItemIDs.chipRematerializer")));
    public static final Item chipDieDigitizer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/digitizerdie").build(new Item("chipDieDigitizer", config.getInt("ItemIDs.chipDieDigitizer")));
    public static final Item chipDieCrafting = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/craftingdie").build(new Item("chipDieCrafting", config.getInt("ItemIDs.chipDieCrafting")));
    public static final Item chipDieRematerializer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/rematerializerdie").build(new Item("chipDieRematerializer", config.getInt("ItemIDs.chipDieRematerializer")));
    public static final Item chipDieDematerializer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/dematerializerdie").build(new Item("chipDieDematerializer", config.getInt("ItemIDs.chipDieDematerializer")));
    public static final Item silicon = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/silicon").build(new Item("silicon", config.getInt("ItemIDs.silicon")));
    public static final Item siliconWafer = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/siliconwafer").build(new Item("siliconWafer", config.getInt("ItemIDs.siliconWafer")));
    public static final Item ceramicPlate = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/ceramicplate").build(new Item("ceramicPlate", config.getInt("ItemIDs.ceramicPlate")));
    public static final Item ceramicPlateUnfired = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/ceramicplateunfired").build(new Item("ceramicPlateUnfired", config.getInt("ItemIDs.ceramicPlateUnfired")));
    public static final Item chipDieWireless = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/wirelessnetworkingdie").build(new Item("chipDieWireless", config.getInt("ItemIDs.chipDieWireless")));

    public static final Item chipWireless = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/wirelessnetworkingchip").build(new Item("chipWireless", config.getInt("ItemIDs.chipWireless")));
    public static final Item wirelessAntenna = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/wirelessantenna").build(new Item("wirelessAntenna", config.getInt("ItemIDs.wirelessAntenna")));
    public static final Item redstoneCore = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/redstonecore").build(new Item("redstoneCore", config.getInt("ItemIDs.redstoneCore")));
    public static final Item slotIdFinder = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/idfinder").build(new Item("slotIdFinder", config.getInt("ItemIDs.slotIdFinder"))).setMaxStackSize(1);
    public static final Item mobileTerminal = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/mobileterminal").build(new ItemMobileTerminal("mobileTerminal", config.getInt("ItemIDs.mobileTerminal"))).setMaxStackSize(1);
    public static final Item mobileFluidTerminal = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/mobilefluidterminal").build(new ItemMobileTerminal("mobileFluidTerminal", config.getInt("ItemIDs.mobileFluidTerminal"))).setMaxStackSize(1);
    public static final Item mobileRequestTerminal = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/mobilerequestterminal").build(new ItemMobileTerminal("mobileRequestTerminal", config.getInt("ItemIDs.mobileRequestTerminal"))).setMaxStackSize(1);
    public static final Item linkingCard = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/linkingcard").build(new ItemLinkingCard("linkingCard", config.getInt("ItemIDs.linkingCard"))).setMaxStackSize(1);
    public static final Item blankCard = new ItemBuilder(MOD_ID).setItemModel((item) -> new ItemModelStandard(item, MOD_ID)).setIcon("retrostorage:item/blankcard").build(new Item("blankCard", config.getInt("ItemIDs.blankCard")));

    public static final Block digitalController = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/digital_controller")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockDigitalController("digitalController", config.getInt("BlockIDs.digitalController"), Material.stone));

    public static final Block networkCable = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.CLOTH)
            .setHardness(0.2f)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/block_cable")
            .setBlockModel(
                    block -> {
                        BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                                .build(block);
                        modelMultipart.parentModel = new DFBlockModelBuilder(MOD_ID)
                                .setBlockModel("network_cable/cable_base.json")
                                .setBlockState("network_cable.json")
                                .setMetaStateInterpreter(new NetworkCableStateInterpreter())
                                .build(block);
                        return modelMultipart;
                    }
            )
            .build(new BlockNetworkCable("networkCable", config.getInt("BlockIDs.networkCable"), Material.cloth));

    public static final Block discDrive = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/machine_side")
            .setNorthTexture("retrostorage:block/disc_drive")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockDiscDrive("discDrive", config.getInt("BlockIDs.discDrive"), Material.stone));

    public static final Block fluidDiscDrive = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setSideTextures("retrostorage:block/fluid_machine_side")
            .setTopBottomTextures("retrostorage:block/machine_side")
            .setNorthTexture("retrostorage:block/fluid_disc_drive")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockFluidDiscDrive("fluidDiscDrive", config.getInt("BlockIDs.fluidDiscDrive"), Material.stone));

    public static final Block digitalTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/machine_side")
            .setNorthTexture("retrostorage:block/terminal_front")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockDigitalTerminal("digitalTerminal", config.getInt("BlockIDs.digitalTerminal"), Material.stone));

    public static final Block digitalFluidTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTopBottomTextures("retrostorage:block/machine_side")
            .setSideTextures("retrostorage:block/fluid_machine_side")
            .setNorthTexture("retrostorage:block/fluid_terminal_front")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockDigitalFluidTerminal("digitalFluidTerminal", config.getInt("BlockIDs.digitalFluidTerminal"), Material.stone));

    public static final Block recipeEncoder = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/machine_side")
            .setTopTexture("retrostorage:block/recipe_encoder_top_filled")
            .setNorthTexture("retrostorage:block/recipe_encoder_front")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockRecipeEncoder("recipeEncoder", config.getInt("BlockIDs.recipeEncoder"), Material.stone));
    public static final Block assembler = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/machine_side")
            .setTopTexture("retrostorage:block/recipe_encoder_top_filled")
            .setSideTextures("retrostorage:block/assembler_side")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockAssembler("assembler", config.getInt("BlockIDs.assembler"), Material.stone));
    public static final Block requestTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/machine_side")
            .setNorthTexture("retrostorage:block/request_terminal_front")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockRequestTerminal("requestTerminal", config.getInt("BlockIDs.requestTerminal"), Material.stone));
    public static final Block importer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/importer")
            .build(new BlockImporter("importer", config.getInt("BlockIDs.importer"), Material.stone));
    public static final Block fluidImporter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/fluid_importer")
            .build(new BlockFluidImporter("fluidImporter", config.getInt("BlockIDs.fluidImporter"), Material.stone));
    public static final Block exporter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/exporter")
            .build(new BlockExporter("exporter", config.getInt("BlockIDs.exporter"), Material.stone));
    public static final Block fluidExporter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/fluid_exporter")
            .build(new BlockFluidExporter("fluidExporter", config.getInt("BlockIDs.fluidExporter"), Material.stone));
    public static final Block processProgrammer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/adv_machine_side")
            .setTopTexture("retrostorage:block/process_programmer_top_filled")
            .setNorthTexture("retrostorage:block/process_programmer_front")
            .setBlockModel(BlockModelHorizontalRotation::new)
            .build(new BlockProcessProgrammer("processProgrammer", config.getInt("BlockIDs.processProgrammer"), Material.stone));
    public static final Block advInterface = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/adv_interface_side")
            .build(new BlockAdvInterface("advInterface", config.getInt("BlockIDs.advInterface"), Material.stone));
    public static final Block wirelessLink = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/wireless_link")
            .build(new BlockWirelessLink("wirelessLink", config.getInt("BlockIDs.wirelessLink"), Material.stone));
    public static final Block redstoneEmitter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setBlockModel(BlockModelRedstoneEmitter::new)
            .setTextures("retrostorage:block/redstone_emitter_off")
            .build(new BlockRedstoneEmitter("redstoneEmitter", config.getInt("BlockIDs.redstoneEmitter"), Material.stone));
    public static final Block craftingCoprocessor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/coprocessor")
            .build(new BlockCoprocessor("craftingCoprocessor", config.getInt("BlockIDs.craftingCoprocessor"), Material.stone));

    public static final Block storageBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/external_storage_bus")
            .build(new BlockStorageBus("externalStorageBus", config.getInt("BlockIDs.storageBus"), Material.stone));

    public static final Block fluidStorageBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/external_fluid_storage_bus")
            .build(new BlockFluidStorageBus("externalFluidStorageBus", config.getInt("BlockIDs.fluidStorageBus"), Material.stone));


    public static final Block energyAcceptor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/energy_acceptor")
            .build(new BlockEnergyAcceptor("energyAcceptor", config.getInt("BlockIDs.energyAcceptor"), Material.stone));
    public static final Block creativeEnergyAcceptor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("retrostorage:block/creative_energy_acceptor")
            .build(new BlockCreativeEnergyAcceptor("creativeEnergyAcceptor", config.getInt("BlockIDs.creativeEnergyAcceptor"), Material.stone));

    public RetroStorage() {

        List<Field> fields = new ArrayList<>(Arrays.asList(RetroStorage.class.getDeclaredFields()));
        fields.removeIf((F) -> F.getType() != Block.class);

        for (Field field : fields) {
            try {
                Block block = (Block) field.get(null);
                ItemToolPickaxe.miningLevels.put(block, 2);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("UnreachableCode")
    @Override
    public void onInitialize() {
        EntityHelper.createTileEntity(TileEntityDigitalTerminal.class, "Digital Terminal");
        EntityHelper.createTileEntity(TileEntityDigitalFluidTerminal.class, "Digital Fluid Terminal");
        EntityHelper.createTileEntity(TileEntityDigitalController.class, "Digital Controller");
        EntityHelper.createTileEntity(TileEntityDiscDrive.class, "Disc Drive");
        EntityHelper.createTileEntity(TileEntityFluidDiscDrive.class, "Fluid Disc Drive");
        EntityHelper.createTileEntity(TileEntityNetworkCable.class, "Network Cable");
        EntityHelper.createTileEntity(TileEntityRecipeEncoder.class, "Recipe Encoder");
        EntityHelper.createTileEntity(TileEntityAssembler.class, "Assembler");
        EntityHelper.createTileEntity(TileEntityRequestTerminal.class, "Request Terminal");
        EntityHelper.createTileEntity(TileEntityImporter.class, "Item Importer");
        EntityHelper.createTileEntity(TileEntityFluidImporter.class, "Fluid Importer");
        EntityHelper.createTileEntity(TileEntityExporter.class, "Item Exporter");
        EntityHelper.createTileEntity(TileEntityFluidExporter.class, "Fluid Exporter");
        EntityHelper.createTileEntity(TileEntityProcessProgrammer.class, "Process Programmer");
        EntityHelper.createTileEntity(TileEntityAdvInterface.class, "Adv. Interface");
        EntityHelper.createTileEntity(TileEntityWirelessLink.class, "Wireless Link");
        EntityHelper.createTileEntity(TileEntityEnergyAcceptor.class, "Energy Acceptor");
        EntityHelper.createTileEntity(TileEntityCreativeEnergyAcceptor.class, "Creative Energy Acceptor");
        EntityHelper.createTileEntity(TileEntityRedstoneEmitter.class, "Redstone Emitter");
        EntityHelper.createTileEntity(TileEntityCoprocessor.class, "Crafting Coprocessor");
        EntityHelper.createTileEntity(TileEntityStorageBus.class, "Storage Bus");
        EntityHelper.createTileEntity(TileEntityFluidStorageBus.class, "Fluid Storage Bus");


        Catalyst.GUIS.register("Digital Terminal", new MpGuiEntry(TileEntityDigitalTerminal.class, GuiDigitalTerminal.class, ContainerDigitalTerminal.class));
        Catalyst.GUIS.register("Digital Fluid Terminal", new MpGuiEntry(TileEntityDigitalFluidTerminal.class, GuiDigitalFluidTerminal.class, ContainerDigitalFluidTerminal.class));
        Catalyst.GUIS.register("Digital Controller", new MpGuiEntry(TileEntityDigitalController.class, GuiDigitalController.class, null));
        Catalyst.GUIS.register("Disc Drive", new MpGuiEntry(TileEntityDiscDrive.class, GuiDiscDrive.class, ContainerDiscDrive.class));
        Catalyst.GUIS.register("Fluid Disc Drive", new MpGuiEntry(TileEntityFluidDiscDrive.class, GuiFluidDiscDrive.class, ContainerFluidDiscDrive.class));
        Catalyst.GUIS.register("Recipe Encoder", new MpGuiEntry(TileEntityRecipeEncoder.class, GuiRecipeEncoder.class, ContainerRecipeEncoder.class));
        Catalyst.GUIS.register("Assembler", new MpGuiEntry(TileEntityAssembler.class, GuiAssembler.class, ContainerAssembler.class));
        Catalyst.GUIS.register("Request Terminal", new MpGuiEntry(TileEntityRequestTerminal.class, GuiRequestTerminal.class, ContainerRequestTerminal.class));
        Catalyst.GUIS.register("Item Importer", new MpGuiEntry(TileEntityImporter.class, GuiImporter.class, ContainerImporter.class));
        Catalyst.GUIS.register("Fluid Importer", new MpGuiEntry(TileEntityFluidImporter.class, GuiFluidImporter.class, ContainerFluidImporter.class));
        Catalyst.GUIS.register("Item Exporter", new MpGuiEntry(TileEntityExporter.class, GuiExporter.class, ContainerExporter.class));
        Catalyst.GUIS.register("Fluid Exporter", new MpGuiEntry(TileEntityFluidExporter.class, GuiFluidExporter.class, ContainerFluidExporter.class));
        Catalyst.GUIS.register("Process Programmer", new MpGuiEntry(TileEntityProcessProgrammer.class, GuiProcessProgrammer.class, ContainerProcessProgrammer.class));
        Catalyst.GUIS.register("Adv. Interface", new MpGuiEntry(TileEntityAdvInterface.class, GuiAdvInterface.class, ContainerAdvInterface.class));
        Catalyst.GUIS.register("Energy Acceptor", new MpGuiEntry(TileEntityEnergyAcceptor.class, GuiEnergyAcceptor.class, ContainerEnergyAcceptor.class));
        Catalyst.GUIS.register("Creative Energy Acceptor", new MpGuiEntry(TileEntityCreativeEnergyAcceptor.class, GuiCreativeEnergyAcceptor.class, ContainerCreativeEnergyAcceptor.class));
        Catalyst.GUIS.register("Redstone Emitter", new MpGuiEntry(TileEntityRedstoneEmitter.class, GuiRedstoneEmitter.class, ContainerRedstoneEmitter.class));
        Catalyst.GUIS.register("Storage Bus", new MpGuiEntry(TileEntityStorageBus.class, GuiStorageBus.class, ContainerStorageBus.class));
        Catalyst.GUIS.register("Fluid Storage Bus", new MpGuiEntry(TileEntityFluidStorageBus.class, GuiFluidStorageBus.class, ContainerFluidStorageBus.class));
        LOGGER.info("RetroStorage initialized.");
    }

    @Override
    public void onRecipesReady() {
        RecipeBuilder.Shaped(MOD_ID, "GGG", "GRG", "GGG")
                .addInput('G', Block.glass)
                .addInput('R', Item.dustRedstone)
                .create("blank_disc", new ItemStack(blankDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "GPG", "PRP", "GPG")
                .addInput('G', Block.glass)
                .addInput('R', Item.dustRedstone)
                .addInput('P', new ItemStack(Item.dye, 1, 5))
                .create("recipe_disc", new ItemStack(recipeDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "OPO", "PDP", "OPO")
                .addInput('O', Block.obsidian)
                .addInput('D', recipeDisc)
                .addInput('P', new ItemStack(Item.dye, 1, 5))
                .create("recipe_disc", new ItemStack(advRecipeDisc, 1));
        RecipeBuilder.Shaped(MOD_ID, "RRR", "RDR", "RRR")
                .addInput('R', Item.dustRedstone)
                .addInput('D', blankDisc)
                .create("storage_disc_1", new ItemStack(storageDisc1, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Item.dustRedstone)
                .addInput('g', Item.ingotGold)
                .addInput('G', Block.glass)
                .addInput('X', storageDisc1)
                .addInput('#', new ItemStack(Item.dye, 1, 14))
                .create("storage_disc_2", new ItemStack(storageDisc2, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Item.dustRedstone)
                .addInput('g', Item.ingotGold)
                .addInput('G', Block.glass)
                .addInput('X', storageDisc2)
                .addInput('#', new ItemStack(Item.dye, 1, 11))
                .create("storage_disc_3", new ItemStack(storageDisc3, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Item.dustRedstone)
                .addInput('g', Item.ingotGold)
                .addInput('G', Block.glass)
                .addInput('X', storageDisc3)
                .addInput('#', new ItemStack(Item.dye, 1, 10))
                .create("storage_disc_4", new ItemStack(storageDisc4, 1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Item.dustRedstone)
                .addInput('g', Item.ingotGold)
                .addInput('G', Block.glass)
                .addInput('X', storageDisc4)
                .addInput('#', new ItemStack(Item.dye, 1, 4))
                .create("storage_disc_5", new ItemStack(storageDisc5, 1));

        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R', Item.dustRedstone)
                .addInput('g', Item.ingotGold)
                .addInput('G', Block.glass)
                .addInput('X', storageDisc5)
                .addInput('#', new ItemStack(Item.dye, 1, 5))
                .create("storage_disc_6", new ItemStack(storageDisc6, 1));

        RecipeBuilder.Shaped(MOD_ID, "SS", "SS")
                .addInput('S', silicon)
                .create("silicon_wafer", new ItemStack(siliconWafer, 1));

        RecipeBuilder.Shaped(MOD_ID, "   ", "456", "789")
                .addInput('4', Item.clay)
                .addInput('5', Item.clay)
                .addInput('6', Item.clay)
                .addInput('7', Item.clay)
                .addInput('8', Item.clay)
                .addInput('9', Item.clay)
                .create("ceramic_plate_unfired", new ItemStack(RetroStorage.ceramicPlateUnfired, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Block.glass)
                .addInput('2', Block.glowstone)
                .addInput('3', Block.glass)
                .addInput('4', Block.glowstone)
                .addInput('5', Block.blockDiamond)
                .addInput('6', Block.glowstone)
                .addInput('7', Block.glass)
                .addInput('8', Block.glowstone)
                .addInput('9', Block.glass)
                .create("energy_core", new ItemStack(RetroStorage.energyCore, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Block.stone)
                .addInput('2', Item.ingotIron)
                .addInput('3', Block.stone)
                .addInput('4', Item.ingotIron)
                .addInput('6', Item.ingotIron)
                .addInput('7', Block.stone)
                .addInput('8', Item.ingotIron)
                .addInput('9', Block.stone)
                .create("machine_casing", new ItemStack(RetroStorage.machineCasing, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Block.obsidian)
                .addInput('2', Item.diamond)
                .addInput('3', Block.obsidian)
                .addInput('4', Item.diamond)
                .addInput('5', RetroStorage.machineCasing)
                .addInput('6', Item.diamond)
                .addInput('7', Block.obsidian)
                .addInput('8', Item.diamond)
                .addInput('9', Block.obsidian)
                .create("adv_machine_casing", new ItemStack(RetroStorage.advMachineCasing, 1));

        RecipeBuilder.Shaped(MOD_ID, "1", "5")
                .addInput('1', Item.ingotGold)
                .addInput('5', RetroStorage.ceramicPlate)
                .create("chip_shell", new ItemStack(RetroStorage.chipShell, 1));

        RecipeBuilder.Shaped(MOD_ID, "1", "5", "8")
                .addInput('1', Item.dustRedstone)
                .addInput('5', RetroStorage.chipShell)
                .addInput('8', Item.dustRedstone)
                .create("chip_shell_filled", new ItemStack(RetroStorage.chipShellFilled, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Block.obsidian)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', new ItemStack(Item.dye, 1, 10))
                .create("chip_die_rematerlializer", new ItemStack(RetroStorage.chipDieRematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Item.bucketLava)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', Item.dustRedstone)
                .create("chip_die_dematerializer", new ItemStack(RetroStorage.chipDieDematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.recipeDisc)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', new ItemStack(Item.dye, 1, 5))
                .create("chip_die_crafting", new ItemStack(RetroStorage.chipDieCrafting, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Item.diamond)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', new ItemStack(Item.dye, 1, 4))
                .create("chip_die_digitizer", new ItemStack(RetroStorage.chipDieDigitizer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieDematerializer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_dematerializer", new ItemStack(RetroStorage.chipDematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieRematerializer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_rematerializer", new ItemStack(RetroStorage.chipRematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieCrafting)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_crafting", new ItemStack(RetroStorage.chipCrafting, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieDigitizer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_digitizer", new ItemStack(RetroStorage.chipDigitizer, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", " 8 ")
                .addInput('2', Item.diamond)
                .addInput('4', Block.blockLapis)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('6', Block.blockLapis)
                .addInput('8', Item.diamond)
                .create("chip_die_wireless", new ItemStack(RetroStorage.chipDieWireless, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieWireless)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_wireless", new ItemStack(RetroStorage.chipWireless, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Item.ingotIron)
                .addInput('2', Block.blockLapis)
                .addInput('3', Item.ingotIron)
                .addInput('4', Block.blockLapis)
                .addInput('5', Item.diamond)
                .addInput('6', Block.blockLapis)
                .addInput('7', Item.ingotIron)
                .addInput('8', Item.stick)
                .addInput('9', Item.ingotIron)
                .create("wireless_antenna", new ItemStack(RetroStorage.wirelessAntenna, 1));

        RecipeBuilder.Shaped(MOD_ID, "RRR", "RBR", "RRR")
                .addInput('R', Item.dustRedstone)
                .addInput('B', Block.blockRedstone)
                .create("redstone_core", new ItemStack(RetroStorage.redstoneCore, 1));

        /*RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', new ItemStack(Item.dye, 1, 12))
                .addInput('2', new ItemStack(Item.dye, 1, 4))
                .addInput('3', new ItemStack(Item.dye, 1, 12))
                .addInput('4', new ItemStack(Item.dye, 1, 4))
                .addInput('5', blankCard)
                .addInput('6', new ItemStack(Item.dye, 1, 4))
                .addInput('7', new ItemStack(Item.dye, 1, 12))
                .addInput('8', chipWireless)
                .addInput('9', new ItemStack(Item.dye, 1, 12))
                .create("linking_card", new ItemStack(linkingCard, 1));*/

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', digitalTerminal)
                .addInput('W', chipWireless)
                .create("mobile_terminal", new ItemStack(mobileTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', requestTerminal)
                .addInput('W', chipWireless)
                .create("mobile_request_terminal", new ItemStack(mobileRequestTerminal, 1));

        /*RecipeBuilder.Shaped(MOD_ID, "D", "C", "I")
                .addInput('I', chipDigitizer)
                .addInput('C', digitalChest)
                .addInput('D', storageDisc1)
                .create("portable_cell", new ItemStack(portableCell,1));*/

        RecipeBuilder.Shaped(MOD_ID, "ISI", "SPS", "ISI")
                .addInput('I', Item.ingotIron)
                .addInput('S', Block.stone)
                .addInput('P', Block.pressureplateStone)
                .create("blank_card", new ItemStack(blankCard, 1));

        RecipeBuilder.Shaped(MOD_ID, "C", "S")
                .addInput('C', chipCrafting)
                .addInput('S', Item.stick)
                .create("slot_id_finder", new ItemStack(slotIdFinder, 1));

        RecipeBuilder.Shaped(MOD_ID, "WLW", "GGG", "WLW")
                .addInput('W', Block.wool)
                .addInput('G', Block.glass)
                .addInput('L', new ItemStack(Item.dye, 1, 4))
                .create("network_cable", new ItemStack(networkCable, 8));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', RetroStorage.machineCasing)
                .addInput('2', RetroStorage.networkCable)
                .addInput('3', Block.blockLapis)
                .addInput('4', RetroStorage.networkCable)
                .addInput('5', RetroStorage.energyCore)
                .addInput('6', RetroStorage.networkCable)
                .addInput('7', Block.blockLapis)
                .addInput('8', RetroStorage.networkCable)
                .addInput('9', RetroStorage.machineCasing)
                .create("digital_controller", new ItemStack(digitalController, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", "789")
                .addInput('2', chipDigitizer)
                .addInput('4', chipRematerializer)
                .addInput('5', machineCasing)
                .addInput('6', chipDematerializer)
                .addInput('7', networkCable)
                .addInput('8', "minecraft:chests")
                .addInput('9', networkCable)
                .create("digital_terminal", new ItemStack(digitalTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, " 2 ", "456", " 8 ")
                .addInput('2', chipDigitizer)
                .addInput('4', blankDisc)
                .addInput('5', machineCasing)
                .addInput('6', blankDisc)
                .addInput('8', networkCable)
                .create("disc_drive", new ItemStack(discDrive, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', machineCasing)
                .addInput('2', chipCrafting)
                .addInput('3', machineCasing)
                .addInput('4', chipCrafting)
                .addInput('5', recipeDisc)
                .addInput('6', chipCrafting)
                .addInput('7', machineCasing)
                .addInput('8', chipCrafting)
                .addInput('9', machineCasing)
                .create("assembler", new ItemStack(assembler, 1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipRematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("importer", new ItemStack(importer, 1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipDematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("exporter", new ItemStack(exporter, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', machineCasing)
                .addInput('4', chipCrafting)
                .addInput('5', digitalTerminal)
                .addInput('6', chipCrafting)
                .addInput('8', networkCable)
                .create("request_terminal", new ItemStack(requestTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', machineCasing)
                .addInput('4', recipeDisc)
                .addInput('5', Block.workbench)
                .addInput('6', recipeDisc)
                .addInput('8', chipCrafting)
                .create("recipe_encoder", new ItemStack(recipeEncoder, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Block.workbench)
                .addInput('2', advRecipeDisc)
                .addInput('3', Block.workbench)
                .addInput('4', chipCrafting)
                .addInput('5', recipeEncoder)
                .addInput('6', chipCrafting)
                .addInput('7', Block.workbench)
                .addInput('8', advMachineCasing)
                .addInput('9', Block.workbench)
                .create("process_programmer", new ItemStack(processProgrammer, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', Block.obsidian)
                .addInput('2', advRecipeDisc)
                .addInput('3', Block.obsidian)
                .addInput('4', chipCrafting)
                .addInput('5', assembler)
                .addInput('6', chipDigitizer)
                .addInput('7', Block.obsidian)
                .addInput('8', advMachineCasing)
                .addInput('9', Block.obsidian)
                .create("adv_interface", new ItemStack(advInterface, 1));

        /*RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', chipWireless)
                .addInput('4', networkCable)
                .addInput('5', machineCasing)
                .addInput('6', wirelessAntenna)
                .addInput('8', chipWireless)
                .create("wireless_link", new ItemStack(wirelessLink, 1));*/

        RecipeBuilder.Shaped(MOD_ID, "SRS", "R R", "SRS")
                .addInput('S', machineCasing)
                .addInput('R', redstoneCore)
                .create("energy_acceptor", new ItemStack(energyAcceptor, 1));

        RecipeBuilder.Shaped(MOD_ID, "MTM", "CRD", "MEM")
                .addInput('M', machineCasing)
                .addInput('T', Block.torchRedstoneActive)
                .addInput('C', networkCable)
                .addInput('R', redstoneCore)
                .addInput('D', chipDigitizer)
                .addInput('E', Item.repeater)
                .create("redstone_emitter", new ItemStack(redstoneEmitter, 1));

        RecipeBuilder.Shaped(MOD_ID, " M ", "RCR", " M ")
                .addInput('M', advMachineCasing)
                .addInput('C', energyCore)
                .addInput('R', chipCrafting)
                .create("crafting_coprocessor", new ItemStack(craftingCoprocessor, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc1)
                .create("fluid_storage_disc_1", new ItemStack(fluidStorageDisc1, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc2)
                .create("fluid_storage_disc_2", new ItemStack(fluidStorageDisc2, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc3)
                .create("fluid_storage_disc_3", new ItemStack(fluidStorageDisc3, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc4)
                .create("fluid_storage_disc_4", new ItemStack(fluidStorageDisc4, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc5)
                .create("fluid_storage_disc_5", new ItemStack(fluidStorageDisc5, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', storageDisc6)
                .create("fluid_storage_disc_6", new ItemStack(fluidStorageDisc6, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', discDrive)
                .create("fluid_disc_drive", new ItemStack(fluidDiscDrive, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', digitalTerminal)
                .create("fluid_terminal", new ItemStack(digitalFluidTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', importer)
                .create("fluid_importer", new ItemStack(fluidImporter, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', exporter)
                .create("fluid_importer", new ItemStack(fluidExporter, 1));

        RecipeBuilder.Shaped(MOD_ID,  " C ", "IHE", " C ")
                .addInput('C', machineCasing)
                .addInput('I', fluidImporter)
                .addInput('H',Item.bucket)
                .addInput('E', fluidExporter)
                .create("fluid_storage_bus", new ItemStack(fluidStorageBus, 1));

        RecipeBuilder.Shaped(MOD_ID, " C ", "IHE", " C ")
                .addInput('C', machineCasing)
                .addInput('I', importer)
                .addInput('H',"minecraft:chests")
                .addInput('E', exporter)
                .create("storage_bus", new ItemStack(storageBus, 1));

        RecipeBuilder.Shaped(MOD_ID, "LBL", "BDB", "LBL")
                .addInput('B', new ItemStack(Item.dye, 1, DyeColor.DYE_LIGHT_BLUE.dyeMeta))
                .addInput('L', new ItemStack(Item.dye, 1, DyeColor.DYE_BLUE.dyeMeta))
                .addInput('D', mobileTerminal)
                .create("mobile_fluid_terminal", new ItemStack(mobileFluidTerminal, 1));

        if (config.getBoolean("Other.goldenDiscLoot")) {
            RecipeBuilder.Shaped(MOD_ID, "GgG", "6R6", "GgG")
                    .addInput('G', Block.blockGold)
                    .addInput('g', Block.glass)
                    .addInput('R', Block.blockRedstone)
                    .addInput('6', storageDisc6)
                    .create("golden_disc", new ItemStack(goldenDisc, 1));
        }

        RecipeBuilder.Furnace(MOD_ID).setInput(Block.glass).create("silicon", new ItemStack(silicon, 1));
        RecipeBuilder.Furnace(MOD_ID).setInput(ceramicPlateUnfired).create("ceramic_plate", new ItemStack(ceramicPlate, 1));
    }

    @Override
    public void initNamespaces() {
        RecipeNamespace namespace = new RecipeNamespace();
        final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));
        final RecipeGroup<RecipeEntryFurnace> FURNACE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.furnaceStoneActive)));
        namespace.register("workbench", WORKBENCH);
        namespace.register("furnace", FURNACE);
        Registries.RECIPES.register("retrostorage", namespace);
    }

    public static ItemStack findRecipeResultFromNBT(CompoundTag nbt) {
        RecipeEntryCrafting<?, ?> recipe = findRecipeFromNBT(nbt);
        if (recipe != null) {
            return (ItemStack) recipe.getOutput();
        }
        return null;
    }

    public static RecipeEntryCrafting<?, ItemStack> findRecipeFromNBT(CompoundTag nbt) {
        InventoryAutocrafting crafting = new InventoryAutocrafting(3, 3);
        for (Object tag : nbt.getValues()) {
            if (tag instanceof CompoundTag) {
                ItemStack stack = ItemStack.readItemStackFromNbt((CompoundTag) tag);
                if (stack == null) continue;
                if (stack.itemID != 0 && stack.stackSize != 0) {
                    crafting.setInventorySlotContents(Integer.parseInt(((CompoundTag) tag).getTagName()), stack);
                }
            }
        }
        return findMatchingRecipe(crafting);
    }

    public static RecipeEntryCrafting<?, ?> findRecipeFromList(ArrayList<ItemStack> stacks) {
        InventoryAutocrafting crafting = new InventoryAutocrafting(3, 3);
        int i = 0;
        for (ItemStack stack : stacks) {
            if (stack.itemID != 0 && stack.stackSize != 0) {
                crafting.setInventorySlotContents(i, stack);
                i++;
            }
        }
        return findMatchingRecipe(crafting);
    }

    public static ArrayList<RecipeEntryCrafting<?, ?>> findRecipesByOutput(ItemStack output) {
        ArrayList<RecipeEntryCrafting<?, ?>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            if (recipe instanceof RecipeEntryCraftingShaped) {
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                if (r.getOutput().isItemEqual(output)) {
                    foundRecipes.add(recipe);
                }
            } else if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                if (r.getOutput().isItemEqual(output)) {
                    foundRecipes.add(recipe);
                }
            }
        }
        return foundRecipes;
    }

    public static <T> boolean listContains(List<T> list, T o, BiFunction<T, T, Boolean> equals) {
        for (T obj : list) {
            if (equals.apply(o, obj)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<RecipeEntryCrafting<?, ItemStack>> findRecipesByOutputUsingList(ItemStack output, ArrayList<RecipeEntryCrafting<?, ItemStack>> list) {
        ArrayList<RecipeEntryCrafting<?, ItemStack>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ItemStack> recipe : list) {
            if (recipe.getOutput().isItemEqual(output)) {
                foundRecipes.add(recipe);
            }
        }
        return foundRecipes;
    }

    public static NetworkCraftable findRecipeByOutputUsingList(VariantStack output, List<NetworkCraftable> list) {
        ArrayList<NetworkCraftable> foundRecipes = new ArrayList<>();
        for (NetworkCraftable craftable : list) {
            for (VariantStack stack : craftable.getOutput()) {
                if(stack.getType() == StackType.ITEM && output.getType() == StackType.ITEM) {
                    if(stack.getItem().isItemEqual(output.getItem())){
                        foundRecipes.add(craftable);
                        break;
                    }
                } else if (stack.getType() == StackType.FLUID && output.getType() == StackType.FLUID) {
                    if(stack.getFluid().isFluidEqual(output.getFluid())){
                        foundRecipes.add(craftable);
                        break;
                    }
                }
            }
        }
        if (foundRecipes.isEmpty()) {
            return null;
        }
        return foundRecipes.get(0);
    }

    public static RecipeEntryCrafting<?, ItemStack> findMatchingRecipe(InventoryCrafting inventorycrafting) {
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            RecipeEntryCrafting<?, ItemStack> r = (RecipeEntryCrafting<?, ItemStack>) recipe;
            if (recipe.matches(inventorycrafting)) {
                return r;
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> getRecipeItems(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            RecipeEntryCrafting<?, ItemStack> recipe = craftable.getRecipe();
            ArrayList<ItemStack> inputs = new ArrayList<>();
            if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                inputs = r.getInput().stream().map((S) -> S == null ? null : S.resolve().get(0)).map((s)-> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            if (recipe instanceof RecipeEntryCraftingShaped) {
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                inputs = Arrays.stream(r.getInput()).map((S) -> S == null ? null : S.resolve().get(0)).map((s)-> s != null ? s.copy() : null).collect(Collectors.toCollection(ArrayList::new));
            }
            inputs.removeIf(Objects::isNull);
            for (ItemStack input : inputs) {
                input.stackSize = 1;
            }
            return inputs;
        } else if (craftable.getType() == CraftableType.PROCESS) {
            ArrayList<ItemStack> inputs = new ArrayList<>();
            for (CraftingProcess.Step step : craftable.getProcess().steps) {
                if (!step.output && step.type == StackType.ITEM) {
                    inputs.add(step.stack.copy());
                }
            }
            return inputs;
        }
        return new ArrayList<>();
    }

    public static ArrayList<FluidStack> getRecipeFluids(NetworkCraftable craftable) {
        if (craftable.getType() == CraftableType.RECIPE) {
            //it's not really possible to use fluids themselves in the crafting table
            return new ArrayList<>();
        } else if (craftable.getType() == CraftableType.PROCESS) {
            ArrayList<FluidStack> inputs = new ArrayList<>();
            for (CraftingProcess.Step step : craftable.getProcess().steps) {
                if (!step.output && step.type == StackType.FLUID) {
                    inputs.add(step.fluidStack.copy());
                }
            }
            return inputs;
        }
        return new ArrayList<>();
    }

    public static CompoundTag itemsArrayToNBT(ArrayList<ItemStack> list) {
        CompoundTag recipeNBT = (new CompoundTag());
        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemNBT = (new CompoundTag());
            ItemStack item = list.get(i);
            if (item == null) {
                recipeNBT.putCompound(Integer.toString(i), itemNBT);
                continue;
            }
            item.writeToNBT(itemNBT);
            recipeNBT.putCompound(Integer.toString(i), itemNBT);
        }
        return recipeNBT;
    }

    public static ArrayList<FluidStack> condenseFluidList(List<FluidStack> list) {
        ArrayList<FluidStack> stacks = new ArrayList<>();
        for (FluidStack stack : list) {
            if (stack != null) {
                boolean found = false;
                for (FluidStack S : stacks) {
                    if (S.isFluidEqual(stack)) {
                        S.amount += stack.amount;
                        found = true;
                    }
                }
                if(!found) stacks.add(stack.copy());
            }
        }
        return stacks;
    }

    public static int sortById(ItemStack item1, ItemStack item2) {
        if (item1.itemID == item2.itemID) {
            return Integer.compare(item1.getMetadata(), item2.getMetadata());
        } else {
            return Integer.compare(item1.itemID, item2.itemID);
        }
    }

    public static int sortByStack(ItemStack item1, ItemStack item2) {
        if (item1.stackSize == item2.stackSize) {
            return Integer.compare(item1.getMetadata(), item2.getMetadata());
        } else {
            return Integer.compare(item1.stackSize, item2.stackSize);
        }
    }

    public static int sortByIdFluid(FluidStack E1, FluidStack E2) {
       return Integer.compare(E1.liquid.id, E2.liquid.id);
    }

    public static @UnmodifiableView List<FluidStack> collectFluidStacks(IFluidInventory inv){
        if(inv == null) return Collections.emptyList();
        ArrayList<FluidStack> stacks = new ArrayList<>();

        for (int i = 0; i < inv.getFluidInventorySize(); i++) {
            stacks.add(i,inv.getFluidInSlot(i));
        }

        return Collections.unmodifiableList(stacks);
    }

    public static @UnmodifiableView List<FluidStack> collectAndCondenseFluidStacks(IFluidInventory inv){
        return condenseFluidList(collectFluidStacks(inv));
    }

    public static ItemStack getFirstOutputOfProcess(ArrayList<CompoundTag> tasks) {
        for (CompoundTag task : tasks) {
            boolean isOutput = task.getBoolean("isOutput");
            if (isOutput) {
                if (Objects.equals(task.getString("type"), "fluid")) {
                    return new FluidStack(task.getCompound("stack")).toItemStack();
                }
                return ItemStack.readItemStackFromNbt(task.getCompound("stack"));
            }
        }
        return null;
    }

    @Override
    public void beforeGameStart() {
        try {
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.blockAtlas,true);
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.itemAtlas,true);
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.particleAtlas,true);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterGameStart() {

    }
}
