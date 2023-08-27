package sunsetsatellite.retrostorage;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;

import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.crafting.recipe.RecipeShapeless;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.player.inventory.InventoryCrafting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.items.*;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.sunsetutils.util.Config;
import sunsetsatellite.sunsetutils.util.Vec3i;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.mixin.accessors.BlockAccessor;

import java.lang.reflect.Field;
import java.util.*;

public class RetroStorage implements ModInitializer {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Config config;

    public static Minecraft mc = null;

    static {
        HashMap<String,String> map = new HashMap<>();
        map.put("enableGoldenDiscLoot","0");
        config = new Config(MOD_ID,map,new Class[]{RetroStorage.class});

        //this is here to possibly fix some class loading issues, do not delete
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {}
    }

    public static int nextItemId = 18000;

    public static final Item blankDisc = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("blankDisc",nextItemId++)),"blankDisc","blankdisc.png");
    public static final Item storageDisc1 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc1",nextItemId++),64),"storageDisc1","disc1.png").setMaxStackSize(1);
    public static final Item storageDisc2 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc2",nextItemId++),128),"storageDisc2","disc2.png").setMaxStackSize(1);
    public static final Item storageDisc3 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc3",nextItemId++),196),"storageDisc3","disc3.png").setMaxStackSize(1);
    public static final Item storageDisc4 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc4",nextItemId++),256),"storageDisc4","disc4.png").setMaxStackSize(1);
    public static final Item storageDisc5 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc5",nextItemId++),320),"storageDisc5","disc5.png").setMaxStackSize(1);
    public static final Item storageDisc6 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("storageDisc6",nextItemId++),384),"storageDisc6","disc6.png").setMaxStackSize(1);
    public static final Item virtualDisc = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("virtualDisc",nextItemId++),Short.MAX_VALUE*2),"virtualDisc","virtualdisc.png").setMaxStackSize(1).setNotInCreativeMenu();
    public static final Item recipeDisc = ItemHelper.createItem(MOD_ID,new ItemRecipeDisc(config.getFromConfig("recipeDisc",nextItemId++)),"recipeDisc","recipedisc.png").setMaxStackSize(1);
    public static final Item goldenDisc = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(config.getFromConfig("goldenDisc",nextItemId++),1024),"goldenDisc","goldendisc.png").setMaxStackSize(1);
    public static final Item advRecipeDisc = ItemHelper.createItem(MOD_ID,new ItemAdvRecipeDisc(config.getFromConfig("advRecipeDisc",nextItemId++)),"advRecipeDisc","advrecipedisc.png").setMaxStackSize(1);

    public static Item machineCasing = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("machineCasing",nextItemId++)),"machineCasing","machinecasing.png");
    public static Item advMachineCasing = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("advMachineCasing",nextItemId++)),"advMachineCasing","advmachinecasing.png");
    public static Item energyCore = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("energyCore",nextItemId++)),"energyCore","energycore.png");
    public static Item chipShell = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipShell",nextItemId++)),"chipShell","chipshell.png");
    public static Item chipShellFilled = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipShellFilled",nextItemId++)),"chipShellFilled","filledchipshell.png");
    public static Item chipDigitizer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDigitizer",nextItemId++)),"chipDigitizer","digitizerchip.png");
    public static Item chipCrafting = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipCrafting",nextItemId++)),"chipCrafting","craftingprocessor.png");
    public static Item chipDematerializer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chupDematerializer",nextItemId++)),"chipDematerializer","dematerializerchip.png");
    public static Item chipRematerializer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipRematerializer",nextItemId++)),"chipRematerializer","rematerializerchip.png");
    public static Item chipDieDigitizer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDieDigitizer",nextItemId++)),"chipDieDigitizer","digitizerdie.png");
    public static Item chipDieCrafting = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDieCraftng",nextItemId++)),"chipDieCrafting","craftingdie.png");
    public static Item chipDieRematerializer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDieRematerializer",nextItemId++)),"chipDieRematerializer","rematerializerdie.png");
    public static Item chipDieDematerializer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDieDematerializer",nextItemId++)),"chipDieDematerializer","dematerializerdie.png");
    public static Item silicon = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("silicon",nextItemId++)),"silicon","silicon.png");
    public static Item siliconWafer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("siliconWafer",nextItemId++)),"siliconWafer","siliconwafer.png");
    public static Item ceramicPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("ceramicPlate",nextItemId++)),"ceramicPlate","ceramicplate.png");
    public static Item ceramicPlateUnfired = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("ceramicPlateUnfired",nextItemId++)),"ceramicPlateUnfired","ceramicplateunfired.png");
    public static Item chipDieWireless = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipDieWireless",nextItemId++)),"chipDieWireless","wirelessnetworkingdie.png");
    public static Item chipWireless = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("chipWireless",nextItemId++)),"chipWireless","wirelessnetworkingchip.png");
    public static Item wirelessAntenna = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("wirelessAntenna",nextItemId++)),"wirelessAntenna","wirelessantenna.png");
    public static Item redstoneCore = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("redstoneCore",nextItemId++)),"redstoneCore","redstonecore.png");

    public static Item slotIdFinder = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("slotIdFinder",nextItemId++)),"slotIdFinder","idfinder.png").setMaxStackSize(1);
    public static Item portableCell = ItemHelper.createItem(MOD_ID,new ItemPortableCell(config.getFromConfig("portableCell",nextItemId++)),"portableCell","portablecell.png").setMaxStackSize(1);
    public static Item mobileTerminal = ItemHelper.createItem(MOD_ID,new ItemMobileTerminal(config.getFromConfig("mobileTerminal",nextItemId++)),"mobileTerminal","mobileterminal.png").setMaxStackSize(1);
    public static Item mobileRequestTerminal = ItemHelper.createItem(MOD_ID,new ItemMobileTerminal(config.getFromConfig("mobileRequestTerminal",nextItemId++)),"mobileRequestTerminal","mobilerequestterminal.png").setMaxStackSize(1);


    public static final Item linkingCard = ItemHelper.createItem(MOD_ID,new ItemLinkingCard(config.getFromConfig("linkingCard",nextItemId++)),"linkingCard","linkingcard.png").setMaxStackSize(1);
    public static final Item blankCard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("blankCard",nextItemId++)),"blankCard","blankcard.png");

    public static int nextBlockId = 1400;

    /*public static final Block digitalChest = BlockHelper.createBlock(MOD_ID,new BlockDigitalChest(config.getFromConfig("digitalChest",900), Material.stone),"digitalChest","digitalchesttopfilled.png","machineside.png","digitalchestfront.png","machineside.png","machineside.png","machineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalController = BlockHelper.createBlock(MOD_ID,new BlockDigitalController(config.getFromConfig("digitalController",901), Material.stone),"digitalController","digitalcontroller.png",Block.soundStoneFootstep,2,5,1);
    public static final Block networkCable = BlockHelper.createBlock(MOD_ID,new BlockNetworkCable(config.getFromConfig("networkCable",902)),"networkCable","blockcable.png",Block.soundClothFootstep,1,1,0);
    public static final Block discDrive = BlockHelper.createBlock(MOD_ID,new BlockDiscDrive(config.getFromConfig("discDrive",903), Material.stone),"discDrive","machineside.png","machineside.png","discdrive.png","machineside.png","machineside.png","machineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalTerminal = BlockHelper.createBlock(MOD_ID,new BlockDigitalTerminal(config.getFromConfig("digitalTerminal",904), Material.stone),"digitalTerminal","machineside.png","machineside.png","digitalchestfront.png","machineside.png","machineside.png","machineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block recipeEncoder = BlockHelper.createBlock(MOD_ID,new BlockRecipeEncoder(config.getFromConfig("recipeEncoder",905),Material.stone),"recipeEncoder","recipeencodertopfilled.png","machineside.png","recipeencoderfront.png","machineside.png","machineside.png","machineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block assembler = BlockHelper.createBlock(MOD_ID,new BlockAssembler(config.getFromConfig("assembler",906),Material.stone),"assembler","recipeencodertopfilled.png","machineside.png","assemblerside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block requestTerminal = BlockHelper.createBlock(MOD_ID,new BlockRequestTerminal(config.getFromConfig("requestTerminal",907),Material.stone),"requestTerminal","machineside.png","machineside.png","requestterminalfront.png","machineside.png","machineside.png","machineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block importer = BlockHelper.createBlock(MOD_ID,new BlockImporter(config.getFromConfig("importer",908),Material.stone),"importer","importer.png",Block.soundStoneFootstep,2,5,1);
    public static final Block exporter = BlockHelper.createBlock(MOD_ID,new BlockExporter(config.getFromConfig("exporter",909),Material.stone),"exporter","exporter.png",Block.soundStoneFootstep,2,5,1);
    public static final Block processProgrammer = BlockHelper.createBlock(MOD_ID,new BlockProcessProgrammer(config.getFromConfig("processProgrammer",910),Material.stone),"processProgrammer","processprogrammertopfilled.png","advmachineside.png","processprogrammerfront.png","advmachineside.png","advmachineside.png","advmachineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block advInterface = BlockHelper.createBlock(MOD_ID,new BlockAdvInterface(config.getFromConfig("advInterface",911),Material.stone),"advInterface","advinterfaceside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block wirelessLink = BlockHelper.createBlock(MOD_ID,new BlockWirelessLink(config.getFromConfig("wirelessLink",912),Material.stone),"wirelessLink","wirelesslink.png",Block.soundStoneFootstep,2,5,1);
    public static final Block energyAcceptor = BlockHelper.createBlock(MOD_ID,new BlockEnergyAcceptor(config.getFromConfig("energyAcceptor",913),Material.stone),"energyAcceptor","energyacceptor.png",Block.soundStoneFootstep,2,5,1);*/

    public static final Block digitalChest = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("digitalchestfront.png")
            .setTopTexture("digitalchesttopfilled.png")
            .build(new BlockDigitalChest("digitalChest",config.getFromConfig("digitalChest",nextBlockId++),Material.stone));
    public static final Block digitalController = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("digitalcontroller.png")
            .build(new BlockDigitalController("digitalController",config.getFromConfig("digitalController",nextBlockId++),Material.stone));
    public static final Block networkCable = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("blockcable.png")
            .build(new BlockNetworkCable("networkCable",config.getFromConfig("networkCable",nextBlockId++),Material.stone));
    public static final Block discDrive = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("discdrive.png")
            .build(new BlockDiscDrive("discDrive",config.getFromConfig("discDrive",nextBlockId++),Material.stone));
    public static final Block digitalTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("digitalchestfront.png")
            .build(new BlockDigitalTerminal("digitalTerminal",config.getFromConfig("digitalTerminal",nextBlockId++),Material.stone));
    public static final Block recipeEncoder = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setTopTexture("recipeencodertopfilled.png")
            .setNorthTexture("recipeencoderfront.png")
            .build(new BlockRecipeEncoder("recipeEncoder",config.getFromConfig("recipeEncoder",nextBlockId++),Material.stone));
    public static final Block assembler = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setTopTexture("recipeencodertopfilled.png")
            .setSideTextures("assemblerside.png")
            .build(new BlockAssembler("assembler",config.getFromConfig("assembler",nextBlockId++),Material.stone));
    public static final Block requestTerminal = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("machineside.png")
            .setNorthTexture("requestterminalfront.png")
            .build(new BlockRequestTerminal("requestTerminal",config.getFromConfig("requestTerminal",nextBlockId++),Material.stone));
    public static final Block importer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("importer.png")
            .build(new BlockImporter("importer",config.getFromConfig("importer",nextBlockId++),Material.stone));;
    public static final Block exporter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("exporter.png")
            .build(new BlockExporter("exporter",config.getFromConfig("exporter",nextBlockId++),Material.stone));
    public static final Block processProgrammer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("advmachineside.png")
            .setTopTexture("processprogrammertopfilled.png")
            .setNorthTexture("processprogrammerfront.png")
            .build(new BlockProcessProgrammer("processProgrammer",config.getFromConfig("processProgrammer",nextBlockId++),Material.stone));
    public static final Block advInterface = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("advinterfaceside.png")
            .build(new BlockAdvInterface("advInterface",config.getFromConfig("advInterface",nextBlockId++),Material.stone));;
    public static final Block wirelessLink = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("wirelesslink.png")
            .build(new BlockWirelessLink("wirelessLink",config.getFromConfig("wirelessLink",nextBlockId++),Material.stone));
    public static final Block energyAcceptor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("energyacceptor.png")
            .build(new BlockEnergyAcceptor("energyAcceptor",config.getFromConfig("energyAcceptor",nextBlockId++),Material.stone));
    public static final Block redstoneEmitter = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(5)
            .setLuminance(1)
            .setTextures("redstoneemitteroff.png")
            .build(new BlockRedstoneEmitter("redstoneEmitter",config.getFromConfig("redstoneEmitter",nextBlockId++),Material.stone));

    public static HashMap<String, Vec3i> directions = new HashMap<>();

    public static final int[] emitterOnTex = TextureHelper.getOrCreateBlockTexture(RetroStorage.MOD_ID,"redstoneemitteron.png");


    /*public static Block createBlockWithItem(String modId, Block block, ItemBlock itemblock, String translationKey, String texture, StepSound stepSound, float hardness, float resistance, float lightValue) {
        int[] one = TextureHelper.registerBlockTexture(modId, texture);
        return createBlockWithItem(modId, block, itemblock, translationKey, one[0], one[1], one[0], one[1], one[0], one[1], one[0], one[1], one[0], one[1], one[0], one[1], stepSound, hardness, resistance, lightValue);
    }

    public static Block createBlockWithItem(String modId, Block block, ItemBlock itemblock, String translationKey, int topX, int topY, int bottomX, int bottomY, int northX, int northY, int southX, int southY, int eastX, int eastY, int westX, int westY, StepSound stepSound, float hardness, float resistance, float lightValue) {
        block.setTexCoords(topX, topY, bottomX, bottomY, northX, northY, eastX, eastY, southX, southY, westX, westY);
        block.setBlockName(HalpLibe.addModId(modId, translationKey));
        ((BlockAccessor)block).callSetStepSound(stepSound);
        ((BlockAccessor)block).callSetHardness(hardness);
        ((BlockAccessor)block).callSetResistance(resistance);
        ((BlockAccessor)block).callSetLightValue(lightValue);
        Item.itemsList[block.id] = itemblock;
        return block;
    }*/

    public RetroStorage(){
        directions.put("X+",new Vec3i(1,0,0));
        directions.put("X-",new Vec3i(-1,0,0));
        directions.put("Y+",new Vec3i(0,1,0));
        directions.put("Y-",new Vec3i(0,-1,0));
        directions.put("Z+",new Vec3i(0,0,1));
        directions.put("Z-",new Vec3i(0,0,-1));
    }

    @Override
    public void onInitialize() {
        RecipeHelper.Crafting.createRecipe(blankDisc, 1, new Object[]{"GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(recipeDisc, 1,  new Object[]{"GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.dustRedstone, 'P', new ItemStack(Item.dye, 1, 5)});
        RecipeHelper.Crafting.createRecipe(storageDisc1, 1, new Object[]{"RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.dustRedstone});

        RecipeHelper.Crafting.createRecipe(storageDisc2,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc1, '#', new ItemStack(Item.dye, 1, 14), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc3,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc2, '#', new ItemStack(Item.dye, 1, 11), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc4,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc3, '#', new ItemStack(Item.dye, 1, 10), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc5,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc4, '#', new ItemStack(Item.dye, 1, 4), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc6,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc5, '#', new ItemStack(Item.dye, 1, 5), 'R', Item.dustRedstone});
        if(config.getFromConfig("enableGoldenDiscRecipe",0)==1){
            RecipeHelper.Crafting.createRecipe(goldenDisc,1,new Object[]{"GgG","6R6","GgG",'G',Block.blockGold,'g',Block.glass,'R',Block.blockRedstone,'6',storageDisc6});
        }

        RecipeHelper.Crafting.createRecipe(siliconWafer,1,new Object[]{"SS","SS",'S',silicon});
        RecipeHelper.Crafting.createRecipe(RetroStorage.ceramicPlateUnfired,1,new Object[]{"123","456","789",'4',Item.clay,'5',Item.clay,'6',Item.clay,'7',Item.clay,'8',Item.clay,'9',Item.clay});
        RecipeHelper.Crafting.createRecipe(RetroStorage.energyCore,1,new Object[]{"123","456","789",'1',Block.glass,'2',Block.glowstone,'3',Block.glass,'4',Block.glowstone,'5',Block.blockDiamond,'6',Block.glowstone,'7',Block.glass,'8',Block.glowstone,'9',Block.glass});
        RecipeHelper.Crafting.createRecipe(RetroStorage.machineCasing,1,new Object[]{"123","456","789",'1',Block.stone,'2',Item.ingotIron,'3',Block.stone,'4',Item.ingotIron,'6',Item.ingotIron,'7',Block.stone,'8',Item.ingotIron,'9',Block.stone});
        RecipeHelper.Crafting.createRecipe(RetroStorage.advMachineCasing,1,new Object[]{"123","456","789",'1',Block.obsidian,'2',Item.diamond,'3',Block.obsidian,'4',Item.diamond,'5',RetroStorage.machineCasing,'6',Item.diamond,'7',Block.obsidian,'8',Item.diamond,'9',Block.obsidian});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipShell,1,new Object[]{"123","456","789",'1',Item.ingotGold,'3',Item.ingotGold,'5',RetroStorage.ceramicPlate,'7',Item.ingotGold,'9',Item.ingotGold});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipShellFilled,1,new Object[]{"123","456","789",'1',Item.dustRedstone,'2',Item.dustRedstone,'3',Item.dustRedstone,'4',Item.dustRedstone,'5',RetroStorage.chipShell,'6',Item.dustRedstone,'7',Item.dustRedstone,'8',Item.dustRedstone,'9',Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDieRematerializer,1,new Object[]{"2","5","8",'2',Block.obsidian,'5',RetroStorage.siliconWafer,'8',new ItemStack(Item.dye,1,10)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDieDematerializer,1,new Object[]{"2","5","8",'2',Item.bucketLava,'5',RetroStorage.siliconWafer,'8',Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDieCrafting,1,new Object[]{"2","5","8",'2',RetroStorage.recipeDisc,'5',RetroStorage.siliconWafer,'8',new ItemStack(Item.dye,1,5)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDieDigitizer,1,new Object[]{"2","5","8",'2',Item.diamond,'5',RetroStorage.siliconWafer,'8',new ItemStack(Item.dye,1,4)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDematerializer,1,new Object[]{"2","5","8",'2',RetroStorage.ceramicPlate,'5',RetroStorage.chipDieDematerializer,'8',RetroStorage.chipShellFilled});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipRematerializer,1,new Object[]{"2","5","8",'2',RetroStorage.ceramicPlate,'5',RetroStorage.chipDieRematerializer,'8',RetroStorage.chipShellFilled});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipCrafting,1,new Object[]{"2","5","8",'2',RetroStorage.ceramicPlate,'5',RetroStorage.chipDieCrafting,'8',RetroStorage.chipShellFilled});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDigitizer,1,new Object[]{"2","5","8",'2',RetroStorage.ceramicPlate,'5',RetroStorage.chipDieDigitizer,'8',RetroStorage.chipShellFilled});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipDieWireless,1,new Object[]{"123","456","789",'2',Item.diamond,'4',Block.blockLapis,'5',RetroStorage.siliconWafer,'6',Block.blockLapis,'8',Item.diamond});
        RecipeHelper.Crafting.createRecipe(RetroStorage.chipWireless,1,new Object[]{"123","456","789",'2',RetroStorage.ceramicPlate,'5',RetroStorage.chipDieWireless,'8',RetroStorage.chipShellFilled});
        RecipeHelper.Crafting.createRecipe(RetroStorage.wirelessAntenna,1,new Object[]{"123","456","789",'2',Block.blockLapis,'4',Block.blockLapis,'5',Item.diamond,'6',Block.blockLapis,'7',Item.ingotIron,'8',Item.stick,'9',Item.ingotIron});
        RecipeHelper.Crafting.createRecipe(RetroStorage.redstoneCore,1,new Object[]{"RRR","RBR","RRR",'R',Item.dustRedstone,'B',Block.blockRedstone});
        
        RecipeHelper.Smelting.createRecipe(silicon,Block.glass);
        RecipeHelper.Smelting.createRecipe(ceramicPlate,ceramicPlateUnfired);

        RecipeHelper.Crafting.createRecipe(networkCable,8,new Object[]{"WLW", "GGG", "WLW", 'W', Block.wool, 'G', Block.glass, 'L', new ItemStack(Item.dye, 1, 4)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.digitalController,1,new Object[]{"123","456","789",'1',RetroStorage.machineCasing,'2',RetroStorage.networkCable,'3',Block.blockLapis,'4',RetroStorage.networkCable,'5',RetroStorage.energyCore,'6',RetroStorage.networkCable,'7',Block.blockLapis,'8',RetroStorage.networkCable,'9',RetroStorage.machineCasing});
        RecipeHelper.Crafting.createRecipe(RetroStorage.digitalTerminal,1,new Object[]{"123","456","789",'2',RetroStorage.chipDigitizer,'4',RetroStorage.chipRematerializer,'5',RetroStorage.machineCasing,'6',RetroStorage.chipDematerializer,'7',RetroStorage.networkCable,'8',Block.chestPlanksOak,'9',RetroStorage.networkCable});
        RecipeHelper.Crafting.createRecipe(RetroStorage.discDrive,1,new Object[]{"123","456","789",'2',RetroStorage.chipDigitizer,'4',RetroStorage.blankDisc,'5',RetroStorage.machineCasing,'6',RetroStorage.blankDisc,'8',RetroStorage.networkCable});
        RecipeHelper.Crafting.createRecipe(RetroStorage.digitalChest,1,new Object[]{"123","456","789",'2',RetroStorage.blankDisc,'4',RetroStorage.chipRematerializer,'5',RetroStorage.machineCasing,'6',RetroStorage.chipDematerializer,'8',Block.chestPlanksOak});
        RecipeHelper.Crafting.createRecipe(RetroStorage.assembler,1,new Object[]{"123","456","789",'1',RetroStorage.machineCasing,'2',RetroStorage.chipCrafting,'3',RetroStorage.machineCasing,'4',RetroStorage.chipCrafting,'5',recipeDisc,'6',RetroStorage.chipCrafting,'7',RetroStorage.machineCasing,'8',RetroStorage.chipCrafting,'9',RetroStorage.machineCasing});
        RecipeHelper.Crafting.createRecipe(RetroStorage.importer,1,new Object[]{"123","456","789",'2',RetroStorage.chipRematerializer,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.networkCable,'8',RetroStorage.chipDigitizer});
        RecipeHelper.Crafting.createRecipe(RetroStorage.exporter,1,new Object[]{"123","456","789",'2',RetroStorage.chipDematerializer,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.networkCable,'8',RetroStorage.chipDigitizer});
        RecipeHelper.Crafting.createRecipe(RetroStorage.requestTerminal,1,new Object[]{"123","456","789",'2',RetroStorage.machineCasing,'4',RetroStorage.chipCrafting,'5',RetroStorage.digitalTerminal,'6',RetroStorage.chipCrafting,'8',RetroStorage.networkCable});
        RecipeHelper.Crafting.createRecipe(RetroStorage.recipeEncoder,1,new Object[]{"123","456","789",'2',RetroStorage.machineCasing,'4',recipeDisc,'5',Block.workbench,'6',recipeDisc,'8',RetroStorage.chipCrafting});
        RecipeHelper.Crafting.createRecipe(RetroStorage.processProgrammer,1,new Object[]{"123","456","789",'1',Block.workbench,'2',advRecipeDisc,'3',Block.workbench,'4',RetroStorage.chipCrafting,'5',recipeEncoder,'6',RetroStorage.chipCrafting,'7',Block.workbench,'8',RetroStorage.advMachineCasing,'9',Block.workbench});
        RecipeHelper.Crafting.createRecipe(RetroStorage.advInterface,1,new Object[]{"123","456","789",'1',Block.obsidian,'2',advRecipeDisc,'3',Block.obsidian,'4',RetroStorage.chipCrafting,'5',assembler,'6',RetroStorage.chipDigitizer,'7',Block.obsidian,'8',RetroStorage.advMachineCasing,'9',Block.obsidian});
        RecipeHelper.Crafting.createRecipe(RetroStorage.wirelessLink,1,new Object[]{"123","456","789",'2',RetroStorage.chipWireless,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.wirelessAntenna,'8',RetroStorage.chipWireless});
        RecipeHelper.Crafting.createRecipe(RetroStorage.energyAcceptor,1,new Object[]{"SRS","RER","SRS",'S',machineCasing,'R',redstoneCore,'E',energyCore});
        RecipeHelper.Crafting.createRecipe(RetroStorage.redstoneEmitter,1,new Object[]{"MTM","CRD","MEM",'M',machineCasing,'T',Block.torchRedstoneActive,'C',networkCable,'R',redstoneCore,'D',chipDigitizer,'E',Item.repeater});

        RecipeHelper.Crafting.createRecipe(linkingCard,1,new Object[]{"123","456","789",'1',new ItemStack(Item.dye,1,12),'2',new ItemStack(Item.dye,1,4),'3',new ItemStack(Item.dye,1,12),'4',new ItemStack(Item.dye,1,4),'5',blankCard,'6',new ItemStack(Item.dye,1,4),'7',new ItemStack(Item.dye,1,12),'8',chipWireless,'9',new ItemStack(Item.dye,1,12)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.mobileTerminal,1,new Object[]{"A","T","W",'A',wirelessAntenna,'T',digitalTerminal,'W',chipWireless});
        RecipeHelper.Crafting.createRecipe(RetroStorage.mobileRequestTerminal,1,new Object[]{"A","T","W",'A',wirelessAntenna,'T',requestTerminal,'W',chipWireless});
        RecipeHelper.Crafting.createRecipe(RetroStorage.portableCell,1,new Object[]{"D","C","I",'I',chipDigitizer,'C',digitalChest,'D',storageDisc1});
        RecipeHelper.Crafting.createRecipe(blankCard,4,new Object[]{"ISI", "SPS", "ISI", 'I', Item.ingotIron, 'S', Block.stone, 'P', Block.pressureplateStone});
        RecipeHelper.Crafting.createRecipe(slotIdFinder,1,new Object[]{"C","S",'C',chipCrafting,'S',Item.stick});

        CommandHelper.createCommand(new NBTEditCommand());
        EntityHelper.createTileEntity(TileEntityDigitalChest.class, "Digital Chest");
        EntityHelper.createTileEntity(TileEntityDigitalTerminal.class, "Digital Terminal");
        EntityHelper.createTileEntity(TileEntityDigitalController.class, "Digital COntroller");
        EntityHelper.createTileEntity(TileEntityDiscDrive.class, "Disc Drive");
        EntityHelper.createTileEntity(TileEntityRecipeEncoder.class,"Recipe Encoder");
        EntityHelper.createTileEntity(TileEntityAssembler.class,"Assembler");
        EntityHelper.createTileEntity(TileEntityRequestTerminal.class,"Request Terminal");
        EntityHelper.createTileEntity(TileEntityImporter.class,"Item Importer");
        EntityHelper.createTileEntity(TileEntityExporter.class,"Item Exporter");
        EntityHelper.createTileEntity(TileEntityProcessProgrammer.class,"Process Programmer");
        EntityHelper.createTileEntity(TileEntityAdvInterface.class,"Adv. Interface");
        EntityHelper.createTileEntity(TileEntityWirelessLink.class,"Wireless Link");
        EntityHelper.createTileEntity(TileEntityEnergyAcceptor.class, "Energy Acceptor");
        EntityHelper.createTileEntity(TileEntityRedstoneEmitter.class, "Energy Acceptor");
        LOGGER.info("RetroStorage initialized.");
    }

    public static void printTaskTree(Task rootTask){
        if(rootTask.parent == null){
            RetroStorage.LOGGER.debug("-B-");
            RetroStorage.LOGGER.debug("ROOT: " + rootTask);
            RetroStorage.LOGGER.debug("Requires: ");
            ArrayList<Task> already = new ArrayList<>();
            already.add(rootTask);
            int index = 0;
            for(Task requires : rootTask.requires){
                printTaskTreeRecursive(requires, 0,index++);
            }
            RetroStorage.LOGGER.debug("-E-");
        }
    }

    public static void printTaskTreeRecursive(Task task, int depth, int index){
        String space = String.join("", Collections.nCopies(depth + 2, " "));
        RetroStorage.LOGGER.debug(space+"-B-");
        RetroStorage.LOGGER.debug(space+"TASK "+index+" DEPTH "+depth+": " + task.toString());
        RetroStorage.LOGGER.debug(space+"Requires: ");
        if(task.requires.size() > 0){
            for(Task requires : task.requires){
                printTaskTreeRecursive(requires, depth+1,index);
            }
        } else {
            RetroStorage.LOGGER.debug(space+"  Nothing");
        }
        RetroStorage.LOGGER.debug(space+"-E-");
    }

    public static Object getPrivateValue(Class instanceclass, Object instance, int fieldindex) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field e = instanceclass.getDeclaredFields()[fieldindex];
            e.setAccessible(true);
            return e.get(instance);
        } catch (IllegalAccessException illegalAccessException4) {
            illegalAccessException4.printStackTrace();
            /*logger.throwing("ModLoader", "getPrivateValue", illegalAccessException4);
            ThrowException("An impossible error has occured!", illegalAccessException4);*/
            return null;
        }
    }

    public static Object getPrivateValue(Class instanceclass, Object instance, String field) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field e = instanceclass.getDeclaredField(field);
            e.setAccessible(true);
            return e.get(instance);
        } catch (IllegalAccessException illegalAccessException4) {
            illegalAccessException4.printStackTrace();
            /*logger.throwing("ModLoader", "getPrivateValue", illegalAccessException4);
            ThrowException("An impossible error has occured!", illegalAccessException4);*/
            return null;
        }
    }

    public static ItemStack findRecipeResultFromNBT(CompoundTag nbt){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        for(Object tag : nbt.getValues()){
            if(tag instanceof CompoundTag){
                ItemStack stack = ItemStack.readItemStackFromNbt((CompoundTag) tag);
                if(stack == null) continue;
                if(stack.itemID != 0 && stack.stackSize != 0){
                    crafting.setInventorySlotContents(Integer.parseInt(((CompoundTag) tag).getTagName()),stack);
                }
            }
        }
        CraftingManager craftingManager = CraftingManager.getInstance();
        ItemStack result = craftingManager.findMatchingRecipe(crafting);
        if(result != null){
            return result.copy();
        } else {
            return null;
        }
    }

    public static IRecipe findRecipeFromNBT(CompoundTag nbt){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        for(Object tag : nbt.getValues()){
            if(tag instanceof CompoundTag){
                ItemStack stack = ItemStack.readItemStackFromNbt((CompoundTag) tag);
                if(stack == null) continue;
                if(stack.itemID != 0 && stack.stackSize != 0){
                    crafting.setInventorySlotContents(Integer.parseInt(((CompoundTag) tag).getTagName()),stack);
                }
            }
        }
        CraftingManager craftingManager = CraftingManager.getInstance();
        return findMatchingRecipe(crafting, craftingManager);
    }

    public static IRecipe findRecipeFromList(ArrayList<ItemStack> stacks){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        int i = 0;
        for(ItemStack stack : stacks){
            if(stack.itemID != 0 && stack.stackSize != 0){
                crafting.setInventorySlotContents(i,stack);
                i++;
            }
        }
        CraftingManager craftingManager = CraftingManager.getInstance();
        return findMatchingRecipe(crafting, craftingManager);
    }

    public static ArrayList<IRecipe> findRecipesByOutput(ItemStack output){
        CraftingManager craftingManager = CraftingManager.getInstance();
        ArrayList<IRecipe> foundRecipes = new ArrayList<>();
        List<IRecipe> recipes = craftingManager.getRecipeList();
        for(IRecipe recipe : recipes){
           if(recipe.getRecipeOutput().isItemEqual(output)){
               foundRecipes.add(recipe);
           }
        }
        return foundRecipes;
    }

    public static ArrayList<ArrayList<CompoundTag>> findProcessesByOutput(ItemStack output, DigitalNetwork network){
        ArrayList<ArrayList<CompoundTag>> foundProcesses = new ArrayList<>();
        if(network != null){
            ArrayList<ArrayList<CompoundTag>> availableProcesses = network.getAvailableProcesses();
            if(output != null){
                for(ArrayList<CompoundTag> process : availableProcesses){
                    ItemStack processOutput = getMainOutputOfProcess(process);
                    if(processOutput != null){
                        if(processOutput.isItemEqual(output)){
                            foundProcesses.add(process);
                        }
                    }
                }
            }
        }
        return foundProcesses;
    }

    public static ArrayList<IRecipe> findRecipesByOutputUsingList(ItemStack output,ArrayList<IRecipe> list){
        ArrayList<IRecipe> foundRecipes = new ArrayList<>();;
        for(IRecipe recipe : list){
            if(recipe.getRecipeOutput().isItemEqual(output)){
                foundRecipes.add(recipe);
            }
        }
        return foundRecipes;
    }


    public static ArrayList<IRecipe> findRecipesByInput(ItemStack input){
        CraftingManager craftingManager = CraftingManager.getInstance();
        ArrayList<IRecipe> foundRecipes = new ArrayList<>();
        List<IRecipe> recipes = craftingManager.getRecipeList();
        for(IRecipe recipe : recipes){
            ArrayList<ItemStack> inputs = getRecipeItems(recipe);
            for(ItemStack recipeInput : inputs){
                if(recipeInput.isItemEqual(input)){
                    foundRecipes.add(recipe);
                    break;
                }
            }
        }
        return foundRecipes;
    }

    public static IRecipe findMatchingRecipe(InventoryCrafting inventorycrafting, CraftingManager manager) {
        List recipes;
        try {
            recipes = (List) getPrivateValue(manager.getClass(),manager,"recipes");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        if (recipes != null) {
            for (Object recipe : recipes) {
                IRecipe irecipe = (IRecipe) recipe;
                if (irecipe.matches(inventorycrafting)) {
                    return irecipe;
                }
            }
        }

        return null;
    }

    public static ArrayList<ItemStack> itemsNBTToArray(CompoundTag nbt){
        return null;
    }

    public static ArrayList<ItemStack> getRecipeItems(IRecipe recipe){
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if(recipe instanceof RecipeShapeless){
            inputs = new ArrayList<ItemStack>(((RecipeShapeless) recipe).recipeItems);
        }
        if(recipe instanceof RecipeShaped){
            inputs = new ArrayList<>();
            Collections.addAll(inputs, ((RecipeShaped) recipe).recipeItems);
        }
        inputs.removeIf(Objects::isNull);
        for (ItemStack input : inputs) {
            input.stackSize = 1;
        }

        return inputs;
    }

    public static CompoundTag itemsArrayToNBT(ArrayList<ItemStack> list){
        CompoundTag recipeNBT = (new CompoundTag());
        //System.out.println(recipe.size());
        for(int i = 0;i<list.size();i++) {
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

    public static String recipeToString(IRecipe recipe){
        String string = "";
        if(recipe instanceof RecipeShaped){
            string = "RecipeShaped{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getRecipeOutput() + "}";
        } else if(recipe instanceof RecipeShapeless){
            string = "RecipeShapeless{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getRecipeOutput() + "}";
        }
        return string;
    }

    public static ArrayList<ItemStack> condenseItemList(ArrayList<ItemStack> list){
        ArrayList<ItemStack> condensed = new ArrayList<>();
        list = (ArrayList<ItemStack>) list.clone();
        for (ItemStack stack : list) {
            stack = stack.copy();
            boolean already = false;
            for(ItemStack condensedStack : condensed){
                if(condensedStack != null){
                    if(condensedStack.isItemEqual(stack)){
                        condensedStack.stackSize += stack.stackSize;
                        already = true;
                        break;
                    }
                }
            }
            if(already){
                continue;
            }
            condensed.add(stack);
        }
        return condensed;
    }

    public static ItemStack getMainOutputOfProcess(ArrayList<CompoundTag> tasks){
        for(CompoundTag task : tasks){
            boolean isOutput = task.getBoolean("isOutput");
            if(isOutput){
                return ItemStack.readItemStackFromNbt(task.getCompound("stack"));
            }
        }
        return null;
    }

}
