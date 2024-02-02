package sunsetsatellite.retrostorage;


import com.mojang.nbt.CompoundTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.crafting.legacy.CraftingManager;
import net.minecraft.core.crafting.legacy.type.RecipeShaped;
import net.minecraft.core.crafting.legacy.type.RecipeShapeless;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryCrafting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.items.*;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.DigitalNetwork;
import sunsetsatellite.retrostorage.util.InventoryAutocrafting;
import sunsetsatellite.retrostorage.util.Task;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShaped;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class RetroStorage implements ModInitializer, RecipeEntrypoint {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TomlConfigHandler config;

    public static Minecraft mc = null;

    public static int nextItemId = 18000;
    public static int nextBlockId = 1400;

    static {
        Toml configToml = new Toml("Signal Industries configuration file.");
        configToml.addCategory("BlockIDs");
        configToml.addCategory("ItemIDs");
        configToml.addCategory("Other");
        configToml.addEntry("Other.goldenDiscLoot",false);

        List<Field> blockFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F)->Block.class.isAssignableFrom(F.getType())).toList();
        for (Field blockField : blockFields) {
            configToml.addEntry("BlockIDs."+blockField.getName(),nextBlockId++);
        }
        List<Field> itemFields = Arrays.stream(RetroStorage.class.getDeclaredFields()).filter((F)->Item.class.isAssignableFrom(F.getType())).toList();
        for (Field itemField : itemFields) {
            configToml.addEntry("ItemIDs."+itemField.getName(),nextItemId++);
        }

        config = new TomlConfigHandler(MOD_ID,configToml);

        //this is here to possibly fix some class loading issues, do not delete
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static final Item blankDisc = ItemHelper.createItem(MOD_ID, new Item("blankDisc",config.getInt("ItemIDs.blankDisc")),  "blankdisc.png");
    public static final Item storageDisc1 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc1", config.getInt("ItemIDs.storageDisc1"), 64), "disc1.png").setMaxStackSize(1);
    public static final Item storageDisc2 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc2",config.getInt("ItemIDs.storageDisc2"), 128),  "disc2.png").setMaxStackSize(1);
    public static final Item storageDisc3 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc3",config.getInt("ItemIDs.storageDisc3"), 196),  "disc3.png").setMaxStackSize(1);
    public static final Item storageDisc4 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc4",config.getInt("ItemIDs.storageDisc4"), 256),  "disc4.png").setMaxStackSize(1);
    public static final Item storageDisc5 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc5", config.getInt("ItemIDs.storageDisc5"), 320), "disc5.png").setMaxStackSize(1);
    public static final Item storageDisc6 = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("storageDisc6", config.getInt("ItemIDs.storageDisc6"), 384), "disc6.png").setMaxStackSize(1);
    public static final Item virtualDisc = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("virtualDisc", config.getInt("ItemIDs.virtualDisc"), Short.MAX_VALUE * 2), "virtualdisc.png").setMaxStackSize(1).setNotInCreativeMenu();
    public static final Item recipeDisc = ItemHelper.createItem(MOD_ID, new ItemRecipeDisc("recipeDisc", config.getInt("ItemIDs.recipeDisc")), "recipedisc.png").setMaxStackSize(1);
    public static final Item goldenDisc = ItemHelper.createItem(MOD_ID, new ItemStorageDisc("goldenDisc", config.getInt("ItemIDs.goldenDisc"), 1024), "goldendisc.png").setMaxStackSize(1);
    public static final Item advRecipeDisc = ItemHelper.createItem(MOD_ID, new ItemAdvRecipeDisc("advRecipeDisc", config.getInt("ItemIDs.advRecipeDisc")), "advrecipedisc.png").setMaxStackSize(1);

    public static Item machineCasing = ItemHelper.createItem(MOD_ID, new Item("machineCasing", config.getInt("ItemIDs.machineCasing")), "machinecasing.png");
    public static Item advMachineCasing = ItemHelper.createItem(MOD_ID, new Item("advMachineCasing",config.getInt("ItemIDs.advMachineCasing")),  "advmachinecasing.png");
    public static Item energyCore = ItemHelper.createItem(MOD_ID, new Item("energyCore", config.getInt("ItemIDs.energyCore")), "energycore.png");
    public static Item chipShell = ItemHelper.createItem(MOD_ID, new Item("chipShell", config.getInt("ItemIDs.chipShell")), "chipshell.png");
    public static Item chipShellFilled = ItemHelper.createItem(MOD_ID, new Item("chipShellFilled", config.getInt("ItemIDs.chipShellFilled")), "filledchipshell.png");
    public static Item chipDigitizer = ItemHelper.createItem(MOD_ID, new Item("chipDigitizer",config.getInt("ItemIDs.chipDigitizer")),  "digitizerchip.png");
    public static Item chipCrafting = ItemHelper.createItem(MOD_ID, new Item("chipCrafting", config.getInt("ItemIDs.chipCrafting")), "craftingprocessor.png");
    public static Item chipDematerializer = ItemHelper.createItem(MOD_ID, new Item("chipDematerializer", config.getInt("ItemIDs.chipDematerializer")), "dematerializerchip.png");
    public static Item chipRematerializer = ItemHelper.createItem(MOD_ID, new Item("chipRematerializer", config.getInt("ItemIDs.chipRematerializer")), "rematerializerchip.png");
    public static Item chipDieDigitizer = ItemHelper.createItem(MOD_ID, new Item("chipDieDigitizer", config.getInt("ItemIDs.chipDieDigitizer")), "digitizerdie.png");
    public static Item chipDieCrafting = ItemHelper.createItem(MOD_ID, new Item("chipDieCrafting",config.getInt("ItemIDs.chipDieCrafting")),  "craftingdie.png");
    public static Item chipDieRematerializer = ItemHelper.createItem(MOD_ID, new Item("chipDieRematerializer",config.getInt("ItemIDs.chipDieRematerializer")),  "rematerializerdie.png");
    public static Item chipDieDematerializer = ItemHelper.createItem(MOD_ID, new Item("chipDieDematerializer", config.getInt("ItemIDs.chipDieDematerializer")), "dematerializerdie.png");
    public static Item silicon = ItemHelper.createItem(MOD_ID, new Item("silicon", config.getInt("ItemIDs.silicon")), "silicon.png");
    public static Item siliconWafer = ItemHelper.createItem(MOD_ID, new Item("siliconWafer", config.getInt("ItemIDs.siliconWafer")), "siliconwafer.png");
    public static Item ceramicPlate = ItemHelper.createItem(MOD_ID, new Item("ceramicPlate", config.getInt("ItemIDs.ceramicPlate")), "ceramicplate.png");
    public static Item ceramicPlateUnfired = ItemHelper.createItem(MOD_ID, new Item("ceramicPlateUnfired",config.getInt("ItemIDs.ceramicPlateUnfired")),  "ceramicplateunfired.png");
    public static Item chipDieWireless = ItemHelper.createItem(MOD_ID, new Item("chipDieWireless", config.getInt("ItemIDs.chipDieWireless")), "wirelessnetworkingdie.png");
    public static Item chipWireless = ItemHelper.createItem(MOD_ID, new Item("chipWireless", config.getInt("ItemIDs.chipWireless")), "wirelessnetworkingchip.png");
    public static Item wirelessAntenna = ItemHelper.createItem(MOD_ID, new Item("wirelessAntenna", config.getInt("ItemIDs.wirelessAntenna")), "wirelessantenna.png");
    public static Item redstoneCore = ItemHelper.createItem(MOD_ID, new Item("redstoneCore", config.getInt("ItemIDs.redstoneCore")), "redstonecore.png");

    public static Item slotIdFinder = ItemHelper.createItem(MOD_ID, new Item("slotIdFinder", config.getInt("ItemIDs.slotIdFinder")), "idfinder.png").setMaxStackSize(1);
    public static Item portableCell = ItemHelper.createItem(MOD_ID, new ItemPortableCell("portableCell", config.getInt("ItemIDs.portableCell")), "portablecell.png").setMaxStackSize(1);
    public static Item mobileTerminal = ItemHelper.createItem(MOD_ID, new ItemMobileTerminal("mobileTerminal", config.getInt("ItemIDs.mobileTerminal")), "mobileterminal.png").setMaxStackSize(1);
    public static Item mobileRequestTerminal = ItemHelper.createItem(MOD_ID, new ItemMobileTerminal("mobileRequestTerminal",config.getInt("ItemIDs.mobileRequestTerminal")),  "mobilerequestterminal.png").setMaxStackSize(1);


    public static final Item linkingCard = ItemHelper.createItem(MOD_ID, new ItemLinkingCard("linkingCard", config.getInt("ItemIDs.linkingCard")), "linkingcard.png").setMaxStackSize(1);
    public static final Item blankCard = ItemHelper.createItem(MOD_ID, new Item("blankCard", config.getInt("ItemIDs.blankCard")), "blankcard.png");

    public static final Block digitalChest = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("digitalchestfront.png")
            .setTopTexture("digitalchesttopfilled.png")
            .build(new BlockDigitalChest("digitalChest", config.getInt("BlockIDs.digitalChest"), Material.stone));
    public static final Block digitalController = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("digitalcontroller.png")
            .build(new BlockDigitalController("digitalController", config.getInt("BlockIDs.digitalController"), Material.stone));
    public static final Block networkCable = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("blockcable.png")
            .build(new BlockNetworkCable("networkCable", config.getInt("BlockIDs.networkCable"), Material.stone));
    public static final Block discDrive = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("discdrive.png")
            .build(new BlockDiscDrive("discDrive", config.getInt("BlockIDs.discDrive"), Material.stone));
    public static final Block digitalTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("digitalchestfront.png")
            .build(new BlockDigitalTerminal("digitalTerminal", config.getInt("BlockIDs.digitalTerminal"), Material.stone));
    public static final Block recipeEncoder = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setTopTexture("recipeencodertopfilled.png")
            .setNorthTexture("recipeencoderfront.png")
            .build(new BlockRecipeEncoder("recipeEncoder", config.getInt("BlockIDs.recipeEncoder"), Material.stone));
    public static final Block assembler = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setTopTexture("recipeencodertopfilled.png")
            .setSideTextures("assemblerside.png")
            .build(new BlockAssembler("assembler", config.getInt("BlockIDs.assembler"), Material.stone));
    public static final Block requestTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("requestterminalfront.png")
            .build(new BlockRequestTerminal("requestTerminal", config.getInt("BlockIDs.requestTerminal"), Material.stone));
    public static final Block importer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("importer.png")
            .build(new BlockImporter("importer", config.getInt("BlockIDs.importer"), Material.stone));
    ;
    public static final Block exporter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("exporter.png")
            .build(new BlockExporter("exporter", config.getInt("BlockIDs.exporter"), Material.stone));
    public static final Block processProgrammer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("advmachineside.png")
            .setTopTexture("processprogrammertopfilled.png")
            .setNorthTexture("processprogrammerfront.png")
            .build(new BlockProcessProgrammer("processProgrammer", config.getInt("BlockIDs.processProgrammer"), Material.stone));
    public static final Block advInterface = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("advinterfaceside.png")
            .build(new BlockAdvInterface("advInterface", config.getInt("BlockIDs.advInterface"), Material.stone));
    ;
    public static final Block wirelessLink = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("wirelesslink.png")
            .build(new BlockWirelessLink("wirelessLink", config.getInt("BlockIDs.wirelessLink"), Material.stone));
    public static final Block energyAcceptor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("energyacceptor.png")
            .build(new BlockEnergyAcceptor("energyAcceptor", config.getInt("BlockIDs.energyAcceptor"), Material.stone));
    public static final Block redstoneEmitter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("redstoneemitteroff.png")
            .build(new BlockRedstoneEmitter("redstoneEmitter", config.getInt("BlockIDs.redstoneEmitter"), Material.stone));

    public static HashMap<String, Vec3i> directions = new HashMap<>();

    public static final int[] emitterOnTex = TextureHelper.getOrCreateBlockTexture(RetroStorage.MOD_ID, "redstoneemitteron.png");

    public RetroStorage() {
        directions.put("X+", new Vec3i(1, 0, 0));
        directions.put("X-", new Vec3i(-1, 0, 0));
        directions.put("Y+", new Vec3i(0, 1, 0));
        directions.put("Y-", new Vec3i(0, -1, 0));
        directions.put("Z+", new Vec3i(0, 0, 1));
        directions.put("Z-", new Vec3i(0, 0, -1));
    }

    @Override
    public void onInitialize() {
        EntityHelper.Core.createTileEntity(TileEntityDigitalChest.class, "Digital Chest");
        EntityHelper.Core.createTileEntity(TileEntityDigitalTerminal.class, "Digital Terminal");
        EntityHelper.Core.createTileEntity(TileEntityDigitalController.class, "Digital COntroller");
        EntityHelper.Core.createTileEntity(TileEntityDiscDrive.class, "Disc Drive");
        EntityHelper.Core.createTileEntity(TileEntityRecipeEncoder.class, "Recipe Encoder");
        EntityHelper.Core.createTileEntity(TileEntityAssembler.class, "Assembler");
        EntityHelper.Core.createTileEntity(TileEntityRequestTerminal.class, "Request Terminal");
        EntityHelper.Core.createTileEntity(TileEntityImporter.class, "Item Importer");
        EntityHelper.Core.createTileEntity(TileEntityExporter.class, "Item Exporter");
        EntityHelper.Core.createTileEntity(TileEntityProcessProgrammer.class, "Process Programmer");
        EntityHelper.Core.createTileEntity(TileEntityAdvInterface.class, "Adv. Interface");
        EntityHelper.Core.createTileEntity(TileEntityWirelessLink.class, "Wireless Link");
        EntityHelper.Core.createTileEntity(TileEntityEnergyAcceptor.class, "Energy Acceptor");
        EntityHelper.Core.createTileEntity(TileEntityRedstoneEmitter.class, "Energy Acceptor");
        LOGGER.info("RetroStorage initialized.");
    }

    @Override
    public void onRecipesReady() {
        /*RecipeNamespace namespace = new RecipeNamespace();
        RecipeGroup<RecipeEntryCrafting<?,?>> workbench = new RecipeGroup<>(new RecipeSymbol(Block.workbench.getDefaultStack()));
        RecipeGroup<RecipeEntryCrafting<?,?>> furnace = new RecipeGroup<>(new RecipeSymbol(Block.furnaceStoneIdle.getDefaultStack()));
        namespace.register("workbench",workbench);
        namespace.register("furnace",furnace);
        Registries.RECIPES.register("retrostorage",namespace);
        DataLoader.loadRecipes("/assets/retrostorage/recipe/workbench.json");
        DataLoader.loadRecipes("/assets/retrostorage/recipe/furnace.json");*/
        RecipeBuilder.Shaped(MOD_ID,"GGG", "GRG", "GGG")
                .addInput('G',Block.glass)
                .addInput('R',Item.dustRedstone)
                .create("blank_disc",new ItemStack(blankDisc,1));
        RecipeBuilder.Shaped(MOD_ID,"GPG", "PRP", "GPG")
                .addInput('G',Block.glass)
                .addInput('R',Item.dustRedstone)
                .addInput('P', new ItemStack(Item.dye, 1, 5))
                .create("recipe_disc",new ItemStack(recipeDisc,1));
        RecipeBuilder.Shaped(MOD_ID,"RRR", "RDR", "RRR")
                .addInput('R',Item.dustRedstone)
                .addInput('D',blankDisc)
                .create("storage_disc_1",new ItemStack(storageDisc1,1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R',Item.dustRedstone)
                .addInput('g',Item.ingotGold)
                .addInput('G',Block.glass)
                .addInput('X',storageDisc1)
                .addInput('#',new ItemStack(Item.dye, 1, 14))
                .create("storage_disc_2",new ItemStack(storageDisc2,1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R',Item.dustRedstone)
                .addInput('g',Item.ingotGold)
                .addInput('G',Block.glass)
                .addInput('X',storageDisc2)
                .addInput('#',new ItemStack(Item.dye, 1, 11))
                .create("storage_disc_3",new ItemStack(storageDisc2,1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R',Item.dustRedstone)
                .addInput('g',Item.ingotGold)
                .addInput('G',Block.glass)
                .addInput('X',storageDisc3)
                .addInput('#',new ItemStack(Item.dye, 1, 10))
                .create("storage_disc_4",new ItemStack(storageDisc2,1));
        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R',Item.dustRedstone)
                .addInput('g',Item.ingotGold)
                .addInput('G',Block.glass)
                .addInput('X',storageDisc4)
                .addInput('#',new ItemStack(Item.dye, 1, 4))
                .create("storage_disc_5",new ItemStack(storageDisc2,1));

        RecipeBuilder.Shaped(MOD_ID, "RgG", "X#X", "GgR")
                .addInput('R',Item.dustRedstone)
                .addInput('g',Item.ingotGold)
                .addInput('G',Block.glass)
                .addInput('X',storageDisc5)
                .addInput('#',new ItemStack(Item.dye, 1, 5))
                .create("storage_disc_6",new ItemStack(storageDisc2,1));

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

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
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

        RecipeBuilder.Shaped(MOD_ID, "1 3", " 5 ", "7 8")
                .addInput('1', Item.ingotGold)
                .addInput('3', Item.ingotGold)
                .addInput('5', RetroStorage.ceramicPlate)
                .addInput('7', Item.ingotGold)
                .addInput('8', Item.ingotGold)
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

        RecipeBuilder.Shaped(MOD_ID,"2", "5", "8")
                .addInput('2',Item.bucketLava)
                .addInput('5',RetroStorage.siliconWafer)
                .addInput('8',Item.dustRedstone)
                .create("chip_die_dematerializer",new ItemStack(RetroStorage.chipDieDematerializer,1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.recipeDisc)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', new ItemStack(Item.dye, 1, 5))
                .create("chip_die_crafting", new ItemStack(RetroStorage.chipDieCrafting,1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', Item.diamond)
                .addInput('5', RetroStorage.siliconWafer)
                .addInput('8', new ItemStack(Item.dye, 1, 4))
                .create("chip_die_digitizer", new ItemStack(RetroStorage.chipDieDigitizer,1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieDematerializer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_dematerializer", new ItemStack(RetroStorage.chipDematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieDematerializer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_rematerializer", new ItemStack(RetroStorage.chipRematerializer, 1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieCrafting)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_crafting", new ItemStack(RetroStorage.chipCrafting,1));

        RecipeBuilder.Shaped(MOD_ID, "2", "5", "8")
                .addInput('2', RetroStorage.ceramicPlate)
                .addInput('5', RetroStorage.chipDieDigitizer)
                .addInput('8', RetroStorage.chipShellFilled)
                .create("chip_digitizer", new ItemStack(RetroStorage.chipDigitizer,1));

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
                .addInput('1',Item.ingotIron)
                .addInput('2',Block.blockLapis)
                .addInput('3',Item.ingotIron)
                .addInput('4',Block.blockLapis)
                .addInput('5',Item.diamond)
                .addInput('6',Block.blockLapis)
                .addInput('7',Item.ingotIron)
                .addInput('8',Item.stick)
                .addInput('9',Item.ingotIron)
                .create("wireless_antenna", new ItemStack(RetroStorage.wirelessAntenna, 1));

        RecipeBuilder.Shaped(MOD_ID, "RRR", "RBR", "RRR")
                .addInput('R',Item.dustRedstone)
                .addInput('B',Block.blockRedstone)
                .create("redstone_core", new ItemStack(RetroStorage.redstoneCore, 1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('1', new ItemStack(Item.dye, 1, 12))
                .addInput('2', new ItemStack(Item.dye, 1, 4))
                .addInput('3', new ItemStack(Item.dye, 1, 12))
                .addInput('4', new ItemStack(Item.dye, 1, 4))
                .addInput('5', blankCard)
                .addInput('6', new ItemStack(Item.dye, 1, 4))
                .addInput('7', new ItemStack(Item.dye, 1, 12))
                .addInput('8', chipWireless)
                .addInput('9', new ItemStack(Item.dye, 1, 12))
                .create("linking_card", new ItemStack(linkingCard,1));

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', digitalTerminal)
                .addInput('W', chipWireless)
                .create("mobile_terminal", new ItemStack(mobileTerminal,1));

        RecipeBuilder.Shaped(MOD_ID, "A", "T", "W")
                .addInput('A', wirelessAntenna)
                .addInput('T', requestTerminal)
                .addInput('W', chipWireless)
                .create("mobile_request_terminal", new ItemStack(mobileRequestTerminal,1));

        RecipeBuilder.Shaped(MOD_ID, "D", "C", "I")
                .addInput('I', chipDigitizer)
                .addInput('C', digitalChest)
                .addInput('D', storageDisc1)
                .create("portable_cell", new ItemStack(portableCell,1));

        RecipeBuilder.Shaped(MOD_ID,"ISI", "SPS", "ISI")
                .addInput('I', Item.ingotIron)
                .addInput('S', Block.stone)
                .addInput('P', Block.pressureplateStone)
                .create("blank_card", new ItemStack(blankCard,1));

        RecipeBuilder.Shaped(MOD_ID, "C", "S")
                .addInput('C', chipCrafting)
                .addInput('S', Item.stick)
                .create("slot_id_finder", new ItemStack(slotIdFinder,1));

        RecipeBuilder.Shaped(MOD_ID,"WLW", "GGG", "WLW")
                .addInput('W', Block.wool)
                .addInput('G', Block.glass)
                .addInput('L', new ItemStack(Item.dye, 1, 4))
                .create("network_cable", new ItemStack(networkCable, 8));

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
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

        RecipeBuilder.Shaped(MOD_ID," 2 ", "456", "789")
                .addInput('2', chipDigitizer)
                .addInput('4', chipRematerializer)
                .addInput('5', machineCasing)
                .addInput('6', chipDematerializer)
                .addInput('7', networkCable)
                .addInput('8', Block.chestPlanksOak)
                .addInput('9', networkCable)
                .create("digital_terminal", new ItemStack(digitalTerminal, 1));

        RecipeBuilder.Shaped(MOD_ID," 2 ", "456", " 8 ")
                .addInput('2', chipDigitizer)
                .addInput('4', blankDisc)
                .addInput('5', machineCasing)
                .addInput('6', blankDisc)
                .addInput('8', networkCable)
                .create("disc_drive", new ItemStack(discDrive, 1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', blankDisc)
                .addInput('2', chipRematerializer)
                .addInput('3', machineCasing)
                .addInput('4', chipDematerializer)
                .addInput('5', Block.chestPlanksOak)
                .create("digital_chest", new ItemStack(digitalChest,1));

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
                .create("assembler", new ItemStack(assembler,1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipRematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("importer", new ItemStack(importer,1));

        RecipeBuilder.Shaped(MOD_ID, " 1 ", "234", " 5 ")
                .addInput('1', chipDematerializer)
                .addInput('2', networkCable)
                .addInput('3', machineCasing)
                .addInput('4', networkCable)
                .addInput('5', chipDigitizer)
                .create("exporter", new ItemStack(exporter,1));

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
                .addInput('2',machineCasing)
                .addInput('4',chipCrafting)
                .addInput('5',digitalTerminal)
                .addInput('6',chipCrafting)
                .addInput('8',networkCable)
                .create("request_terminal", new ItemStack(requestTerminal,1));

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
                .addInput('2',machineCasing)
                .addInput('4',recipeDisc)
                .addInput('5',Block.workbench)
                .addInput('6',recipeDisc)
                .addInput('8',chipCrafting)
                .create("recipe_encoder", new ItemStack(recipeEncoder,1));

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
                .addInput('1',Block.workbench)
                .addInput('2',advRecipeDisc)
                .addInput('3',Block.workbench)
                .addInput('4',chipCrafting)
                .addInput('5',recipeEncoder)
                .addInput('6',chipCrafting)
                .addInput('7',Block.workbench)
                .addInput('8',advMachineCasing)
                .addInput('9',Block.workbench)
                .create("process_programmer", new ItemStack(processProgrammer,1));

        RecipeBuilder.Shaped(MOD_ID,"123", "456", "789")
                .addInput('1',Block.obsidian)
                .addInput('2',advRecipeDisc)
                .addInput('3',Block.obsidian)
                .addInput('4',chipCrafting)
                .addInput('5',assembler)
                .addInput('6',chipDigitizer)
                .addInput('7',Block.obsidian)
                .addInput('8',advMachineCasing)
                .addInput('9',Block.obsidian)
                .create("adv_interface", new ItemStack(advInterface,1));

        RecipeBuilder.Shaped(MOD_ID, "123", "456", "789")
                .addInput('2', chipWireless)
                .addInput('4', networkCable)
                .addInput('5', machineCasing)
                .addInput('6', wirelessAntenna)
                .addInput('8', chipWireless)
                .create("wireless_link", new ItemStack(wirelessLink, 1));

        RecipeBuilder.Shaped(MOD_ID, "SRS", "RER", "SRS")
                .addInput('S', machineCasing)
                .addInput('R', redstoneCore)
                .addInput('E', energyCore)
                .create("energy_acceptor", new ItemStack(energyAcceptor, 1));

        RecipeBuilder.Shaped(MOD_ID, "MTM", "CRD", "MEM")
                .addInput('M', machineCasing)
                .addInput('T', Block.torchRedstoneActive)
                .addInput('C', networkCable)
                .addInput('R', redstoneCore)
                .addInput('D', chipDigitizer)
                .addInput('E', Item.repeater)
                .create("redstone_emitter", new ItemStack(redstoneEmitter, 1));

        if (config.getBoolean("Other.goldenDiscLoot")) {
            RecipeBuilder.Shaped(MOD_ID, "GgG", "6R6", "GgG")
                    .addInput('G', Block.blockGold)
                    .addInput('g', Block.glass)
                    .addInput('R', Block.blockRedstone)
                    .addInput('6', storageDisc6)
                    .create("golden_disc", new ItemStack(goldenDisc, 1));
        }

        RecipeBuilder.Furnace(MOD_ID).setInput(Block.glass).create("silicon",new ItemStack(silicon,1));
        RecipeBuilder.Furnace(MOD_ID).setInput(ceramicPlateUnfired).create("ceramic_plate",new ItemStack(ceramicPlate,1));
    }

    public static void printTaskTree(Task rootTask) {
        if (rootTask.parent == null) {
            RetroStorage.LOGGER.debug("-B-");
            RetroStorage.LOGGER.debug("ROOT: " + rootTask);
            RetroStorage.LOGGER.debug("Requires: ");
            ArrayList<Task> already = new ArrayList<>();
            already.add(rootTask);
            int index = 0;
            for (Task requires : rootTask.requires) {
                printTaskTreeRecursive(requires, 0, index++);
            }
            RetroStorage.LOGGER.debug("-E-");
        }
    }

    public static void printTaskTreeRecursive(Task task, int depth, int index) {
        String space = String.join("", Collections.nCopies(depth + 2, " "));
        RetroStorage.LOGGER.debug(space + "-B-");
        RetroStorage.LOGGER.debug(space + "TASK " + index + " DEPTH " + depth + ": " + task.toString());
        RetroStorage.LOGGER.debug(space + "Requires: ");
        if (!task.requires.isEmpty()) {
            for (Task requires : task.requires) {
                printTaskTreeRecursive(requires, depth + 1, index);
            }
        } else {
            RetroStorage.LOGGER.debug(space + "  Nothing");
        }
        RetroStorage.LOGGER.debug(space + "-E-");
    }

    public static ItemStack findRecipeResultFromNBT(CompoundTag nbt) {
        RecipeEntryCrafting<?,?> recipe = findRecipeFromNBT(nbt);
        if(recipe != null){
            return (ItemStack) recipe.getOutput();
        }
        return null;
    }

    public static RecipeEntryCrafting<?,?> findRecipeFromNBT(CompoundTag nbt) {
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

    public static RecipeEntryCrafting<?,?> findRecipeFromList(ArrayList<ItemStack> stacks) {
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

    public static ArrayList<RecipeEntryCrafting<?,?>> findRecipesByOutput(ItemStack output) {
        ArrayList<RecipeEntryCrafting<?,?>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            if(recipe instanceof RecipeEntryCraftingShaped){
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                if(r.getOutput().isItemEqual(output)){
                    foundRecipes.add(recipe);
                }
            } else if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                if(r.getOutput().isItemEqual(output)){
                    foundRecipes.add(recipe);
                }
            }
        }
        return foundRecipes;
    }

    public static ArrayList<RecipeEntryCrafting<?,?>> findRecipesByOutput(ItemStack output, DigitalNetwork network) {
        ArrayList<RecipeEntryCrafting<?,?>> foundRecipes = new ArrayList<>();
        for (RecipeEntryCrafting<?, ?> recipe : network.getAvailableRecipes()) {
            if(recipe instanceof RecipeEntryCraftingShaped){
                RecipeEntryCraftingShaped r = (RecipeEntryCraftingShaped) recipe;
                if(r.getOutput().isItemEqual(output)){
                    foundRecipes.add(recipe);
                }
            } else if (recipe instanceof RecipeEntryCraftingShapeless) {
                RecipeEntryCraftingShapeless r = (RecipeEntryCraftingShapeless) recipe;
                if(r.getOutput().isItemEqual(output)){
                    foundRecipes.add(recipe);
                }
            }
        }
        return foundRecipes;
    }

    public static ArrayList<ArrayList<CompoundTag>> findProcessesByOutput(ItemStack output, DigitalNetwork network) {
        ArrayList<ArrayList<CompoundTag>> foundProcesses = new ArrayList<>();
        if (network != null) {
            ArrayList<ArrayList<CompoundTag>> availableProcesses = network.getAvailableProcesses();
            if (output != null) {
                for (ArrayList<CompoundTag> process : availableProcesses) {
                    ItemStack processOutput = getMainOutputOfProcess(process);
                    if (processOutput != null) {
                        if (processOutput.isItemEqual(output)) {
                            foundProcesses.add(process);
                        }
                    }
                }
            }
        }
        return foundProcesses;
    }

    public static ArrayList<RecipeEntryCrafting<?,?>> findRecipesByOutputUsingList(ItemStack output, ArrayList<RecipeEntryCrafting<?,?>> list) {
        ArrayList<RecipeEntryCrafting<?,?>> foundRecipes = new ArrayList<>();
        ;
        for (RecipeEntryCrafting<?,?> recipe : list) {
            if (((ItemStack)recipe.getOutput()).isItemEqual(output)) {
                foundRecipes.add(recipe);
            }
        }
        return foundRecipes;
    }

    public static RecipeEntryCrafting<?,?> findMatchingRecipe(InventoryCrafting inventorycrafting) {
        for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
            if(recipe.matches(inventorycrafting)){
                return recipe;
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> itemsNBTToArray(CompoundTag nbt) {
        return null;
    }

    public static ArrayList<ItemStack> getRecipeItems(RecipeEntryCrafting<?,?> recipe) {
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if (recipe instanceof RecipeEntryCraftingShapeless r) {
            inputs = r.getInput().stream().map((S) -> S.resolve().get(0)).collect(Collectors.toCollection(ArrayList::new));
        }
        if (recipe instanceof RecipeEntryCraftingShaped r) {
            inputs = new ArrayList<>();
            inputs = Arrays.stream(r.getInput()).map((S)-> S.resolve().get(0)).collect(Collectors.toCollection(ArrayList::new));
        }
        inputs.removeIf(Objects::isNull);
        for (ItemStack input : inputs) {
            input.stackSize = 1;
        }

        return inputs;
    }

    public static CompoundTag itemsArrayToNBT(ArrayList<ItemStack> list) {
        CompoundTag recipeNBT = (new CompoundTag());
        //System.out.println(recipe.size());
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

    public static String recipeToString(RecipeEntryCrafting<?,?> recipe) {
        String string = "";
        if (recipe instanceof RecipeEntryCraftingShaped) {
            string = "RecipeShaped{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getOutput() + "}";
        } else if (recipe instanceof RecipeEntryCraftingShapeless) {
            string = "RecipeShapeless{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getOutput() + "}";
        }
        return string;
    }

    public static ArrayList<ItemStack> condenseItemList(ArrayList<ItemStack> list) {
        ArrayList<ItemStack> condensed = new ArrayList<>();
        list = (ArrayList<ItemStack>) list.clone();
        for (ItemStack stack : list) {
            stack = stack.copy();
            boolean already = false;
            for (ItemStack condensedStack : condensed) {
                if (condensedStack != null) {
                    if (condensedStack.isItemEqual(stack)) {
                        condensedStack.stackSize += stack.stackSize;
                        already = true;
                        break;
                    }
                }
            }
            if (already) {
                continue;
            }
            condensed.add(stack);
        }
        return condensed;
    }

    public static ItemStack getMainOutputOfProcess(ArrayList<CompoundTag> tasks) {
        for (CompoundTag task : tasks) {
            boolean isOutput = task.getBoolean("isOutput");
            if (isOutput) {
                return ItemStack.readItemStackFromNbt(task.getCompound("stack"));
            }
        }
        return null;
    }
}
