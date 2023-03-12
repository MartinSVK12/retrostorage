package sunsetsatellite.retrostorage;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.items.*;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.*;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.mixin.accessors.BlockAccessor;

import java.lang.reflect.Field;
import java.util.*;

public class RetroStorage implements ModInitializer {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Minecraft mc = null;

    public static final Item blankDisc = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("blankDisc",300)),"blankDisc","blankdisc.png");
    public static final Item storageDisc1 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc1",301),64),"storageDisc1","disc1.png").setMaxStackSize(1);
    public static final Item storageDisc2 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc2",302),128),"storageDisc2","disc2.png").setMaxStackSize(1);
    public static final Item storageDisc3 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc3",303),196),"storageDisc3","disc3.png").setMaxStackSize(1);
    public static final Item storageDisc4 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc4",304),256),"storageDisc4","disc4.png").setMaxStackSize(1);
    public static final Item storageDisc5 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc5",305),320),"storageDisc5","disc5.png").setMaxStackSize(1);
    public static final Item storageDisc6 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("storageDisc6",306),384),"storageDisc6","disc6.png").setMaxStackSize(1);
    public static final Item virtualDisc = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("virtualDisc",307),Short.MAX_VALUE*2),"virtualDisc","virtualdisc.png").setMaxStackSize(1).setNotInCreativeMenu();
    public static final Item recipeDisc = ItemHelper.createItem(MOD_ID,new ItemRecipeDisc(Config.getFromConfig("recipeDisc",308)),"recipeDisc","recipedisc.png").setMaxStackSize(1);
    public static final Item goldenDisc = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(Config.getFromConfig("goldenDisc",330),1024),"goldenDisc","goldendisc.png").setMaxStackSize(1);
    public static final Item advRecipeDisc = ItemHelper.createItem(MOD_ID,new ItemAdvRecipeDisc(Config.getFromConfig("advRecipeDisc",331)),"advRecipeDisc","advrecipedisc.png").setMaxStackSize(1);

    public static Item machineCasing = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("machineCasing",309)),"machineCasing","machinecasing.png");
    public static Item advMachineCasing = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("advMachineCasing",310)),"advMachineCasing","advmachinecasing.png");
    public static Item energyCore = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("energyCore",311)),"energyCore","energycore.png");
    public static Item chipShell = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipShell",312)),"chipShell","chipshell.png");
    public static Item chipShellFilled = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipShellFilled",313)),"chipShellFilled","filledchipshell.png");
    public static Item chipDigitizer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDigitizer",314)),"chipDigitizer","digitizerchip.png");
    public static Item chipCrafting = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipCrafting",315)),"chipCrafting","craftingprocessor.png");
    public static Item chipDematerializer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chupDematerializer",316)),"chipDematerializer","dematerializerchip.png");
    public static Item chipRematerializer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipRematerializer",317)),"chipRematerializer","rematerializerchip.png");
    public static Item chipDieDigitizer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDieDigitizer",318)),"chipDieDigitizer","digitizerdie.png");
    public static Item chipDieCrafting = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDieCraftng",319)),"chipDieCrafting","craftingdie.png");
    public static Item chipDieRematerializer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDieRematerializer",320)),"chipDieRematerializer","rematerializerdie.png");
    public static Item chipDieDematerializer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDieDematerializer",321)),"chipDieDematerializer","dematerializerdie.png");
    public static Item silicon = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("silicon",322)),"silicon","silicon.png");
    public static Item siliconWafer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("siliconWafer",323)),"siliconWafer","siliconwafer.png");
    public static Item ceramicPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("ceramicPlate",324)),"ceramicPlate","ceramicplate.png");
    public static Item ceramicPlateUnfired = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("ceramicPlateUnfired",325)),"ceramicPlateUnfired","ceramicplateunfired.png");
    public static Item chipDieWireless = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipDieWireless",326)),"chipDieWireless","wirelessnetworkingdie.png");
    public static Item chipWireless = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("chipWireless",327)),"chipWireless","wirelessnetworkingchip.png");
    public static Item wirelessAntenna = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("wirelessAntenna",328)),"wirelessAntenna","wirelessantenna.png");
    public static Item redstoneCore = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("redstoneCore",329)),"redstoneCore","redstonecore.png");

    public static Item slotIdFinder = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("slotIdFinder",332)),"slotIdFinder","idfinder.png").setMaxStackSize(1);
    public static Item portableCell = ItemHelper.createItem(MOD_ID,new ItemPortableCell(Config.getFromConfig("portableCell",333)),"portableCell","portablecell.png").setMaxStackSize(1);
    public static Item mobileTerminal = ItemHelper.createItem(MOD_ID,new ItemMobileTerminal(Config.getFromConfig("mobileTerminal",334)),"mobileTerminal","mobileterminal.png").setMaxStackSize(1);
    public static Item mobileRequestTerminal = ItemHelper.createItem(MOD_ID,new ItemMobileTerminal(Config.getFromConfig("mobileRequestTerminal",335)),"mobileRequestTerminal","mobilerequestterminal.png").setMaxStackSize(1);


    public static final Item linkingCard = ItemHelper.createItem(MOD_ID,new ItemLinkingCard(Config.getFromConfig("linkingCard",336)),"linkingCard","linkingcard.png").setMaxStackSize(1);
    public static final Item blankCard = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("blankCard",337)),"blankCard","blankcard.png");


    public static final Block digitalChest = BlockHelper.createBlock(MOD_ID,new BlockDigitalChest(Config.getFromConfig("digitalChest",900), Material.rock),"digitalChest","digitalchesttopfilled.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalController = BlockHelper.createBlock(MOD_ID,new BlockDigitalController(Config.getFromConfig("digitalController",901), Material.rock),"digitalController","digitalcontroller.png",Block.soundStoneFootstep,2,5,1);
    public static final Block networkCable = BlockHelper.createBlock(MOD_ID,new BlockNetworkCable(Config.getFromConfig("networkCable",902)),"networkCable","blockcable.png",Block.soundClothFootstep,1,1,0);
    public static final Block discDrive = BlockHelper.createBlock(MOD_ID,new BlockDiscDrive(Config.getFromConfig("discDrive",903), Material.rock),"discDrive","digitalchestside.png","digitalchestside.png","discdrive.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalTerminal = BlockHelper.createBlock(MOD_ID,new BlockDigitalTerminal(Config.getFromConfig("digitalTerminal",904), Material.rock),"digitalTerminal","digitalchestside.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block recipeEncoder = BlockHelper.createBlock(MOD_ID,new BlockRecipeEncoder(Config.getFromConfig("recipeEncoder",905),Material.rock),"recipeEncoder","recipeencodertopfilled.png","digitalchestside.png","recipeencoderfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block assembler = BlockHelper.createBlock(MOD_ID,new BlockAssembler(Config.getFromConfig("assembler",906),Material.rock),"assembler","recipeencodertopfilled.png","digitalchestside.png","assemblerside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block requestTerminal = BlockHelper.createBlock(MOD_ID,new BlockRequestTerminal(Config.getFromConfig("requestTerminal",907),Material.rock),"requestTerminal","digitalchestside.png","digitalchestside.png","requestterminalfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block importer = BlockHelper.createBlock(MOD_ID,new BlockImporter(Config.getFromConfig("importer",908),Material.rock),"importer","importer.png",Block.soundStoneFootstep,2,5,1);
    public static final Block exporter = BlockHelper.createBlock(MOD_ID,new BlockExporter(Config.getFromConfig("exporter",909),Material.rock),"exporter","exporter.png",Block.soundStoneFootstep,2,5,1);
    public static final Block processProgrammer = BlockHelper.createBlock(MOD_ID,new BlockProcessProgrammer(Config.getFromConfig("processProgrammer",910),Material.rock),"processProgrammer","processprogrammertopfilled.png","advmachineside.png","processprogrammerfront.png","advmachineside.png","advmachineside.png","advmachineside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block advInterface = BlockHelper.createBlock(MOD_ID,new BlockAdvInterface(Config.getFromConfig("advInterface",911),Material.rock),"advInterface","advinterfaceside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block wirelessLink = BlockHelper.createBlock(MOD_ID,new BlockWirelessLink(Config.getFromConfig("wirelessLink",912),Material.rock),"wirelessLink","wirelesslink.png",Block.soundStoneFootstep,2,5,1);
    public static final Block energyAcceptor = BlockHelper.createBlock(MOD_ID,new BlockEnergyAcceptor(Config.getFromConfig("energyAcceptor",913),Material.rock),"energyAcceptor","energyacceptor.png",Block.soundStoneFootstep,2,5,1);


    public static HashMap<String, Vec3> directions = new HashMap<>();

    public int[] cableTex = TextureHelper.registerItemTexture(RetroStorage.MOD_ID,"itemcable.png");


    public static Block createBlockWithItem(String modId, Block block, ItemBlock itemblock, String translationKey, String texture, StepSound stepSound, float hardness, float resistance, float lightValue) {
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
        Item.itemsList[block.blockID] = itemblock;
        return block;
    }

    public RetroStorage(){
        Config.init();
        directions.put("X+",new Vec3(1,0,0));
        directions.put("X-",new Vec3(-1,0,0));
        directions.put("Y+",new Vec3(0,1,0));
        directions.put("Y-",new Vec3(0,-1,0));
        directions.put("Z+",new Vec3(0,0,1));
        directions.put("Z-",new Vec3(0,0,-1));
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
        if(Config.getFromConfig("enableGoldenDiscRecipe",0)==1){
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
        //ModLoader.AddRecipe(new ItemStack(RetroStorage.storageContainer,1,0),"123","456","789",'1',RetroStorage.machineCasing,'2',RetroStorage.machineCasing,'3',RetroStorage.machineCasing,'4',Item.ingotIron,'5',Block.chestPlanksOak,'6',Item.ingotIron,'7',RetroStorage.machineCasing,'8',RetroStorage.machineCasing,'9',RetroStorage.machineCasing});
        RecipeHelper.Crafting.createRecipe(RetroStorage.assembler,1,new Object[]{"123","456","789",'1',RetroStorage.machineCasing,'2',RetroStorage.chipCrafting,'3',RetroStorage.machineCasing,'4',RetroStorage.chipCrafting,'5',recipeDisc,'6',RetroStorage.chipCrafting,'7',RetroStorage.machineCasing,'8',RetroStorage.chipCrafting,'9',RetroStorage.machineCasing});
        RecipeHelper.Crafting.createRecipe(RetroStorage.importer,1,new Object[]{"123","456","789",'2',RetroStorage.chipRematerializer,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.networkCable,'8',RetroStorage.chipDigitizer});
        RecipeHelper.Crafting.createRecipe(RetroStorage.exporter,1,new Object[]{"123","456","789",'2',RetroStorage.chipDematerializer,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.networkCable,'8',RetroStorage.chipDigitizer});
        //RecipeHelper.Crafting.createRecipe(RetroStorage.,1,new Object[]{"123","456","789",'2',new ItemStack(RetroStorage.multiID,1,5),'3',RetroStorage.chipDigitizer,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.networkCable,'7',RetroStorage.chipCrafting,'8',new ItemStack(RetroStorage.multiID,1,4)});
        RecipeHelper.Crafting.createRecipe(RetroStorage.requestTerminal,1,new Object[]{"123","456","789",'2',RetroStorage.machineCasing,'4',RetroStorage.chipCrafting,'5',RetroStorage.digitalTerminal,'6',RetroStorage.chipCrafting,'8',RetroStorage.networkCable});
        RecipeHelper.Crafting.createRecipe(RetroStorage.recipeEncoder,1,new Object[]{"123","456","789",'2',RetroStorage.machineCasing,'4',recipeDisc,'5',Block.workbench,'6',recipeDisc,'8',RetroStorage.chipCrafting});
        RecipeHelper.Crafting.createRecipe(RetroStorage.processProgrammer,1,new Object[]{"123","456","789",'1',Block.workbench,'2',advRecipeDisc,'3',Block.workbench,'4',RetroStorage.chipCrafting,'5',recipeEncoder,'6',RetroStorage.chipCrafting,'7',Block.workbench,'8',RetroStorage.advMachineCasing,'9',Block.workbench});
        RecipeHelper.Crafting.createRecipe(RetroStorage.advInterface,1,new Object[]{"123","456","789",'1',Block.obsidian,'2',advRecipeDisc,'3',Block.obsidian,'4',RetroStorage.chipCrafting,'5',assembler,'6',RetroStorage.chipDigitizer,'7',Block.obsidian,'8',RetroStorage.advMachineCasing,'9',Block.obsidian});
        RecipeHelper.Crafting.createRecipe(RetroStorage.wirelessLink,1,new Object[]{"123","456","789",'2',RetroStorage.chipWireless,'4',RetroStorage.networkCable,'5',RetroStorage.machineCasing,'6',RetroStorage.wirelessAntenna,'8',RetroStorage.chipWireless});
        //RecipeHelper.Crafting.createRecipe(RetroStorage.,1,new Object[]{"123","456","789",'1',RetroStorage.machineCasing,'2',Block.torchRedstoneActive,'3',RetroStorage.machineCasing,'4',RetroStorage.networkCable,'5',RetroStorage.redstoneCore,'6',RetroStorage.chipDigitizer,'7',RetroStorage.machineCasing,'8',Item.redstoneRepeater,'9',RetroStorage.machineCasing});*/
        RecipeHelper.Crafting.createRecipe(RetroStorage.energyAcceptor,1,new Object[]{"SRS","RER","SRS",'S',machineCasing,'R',redstoneCore,'E',energyCore});

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
        LOGGER.info("RetroStorage initialized.");
    }

    public static void printTaskTree(Task rootTask){
        if(rootTask.parent == null){
            RetroStorage.LOGGER.info("-B-");
            RetroStorage.LOGGER.info("ROOT: " + rootTask);
            RetroStorage.LOGGER.info("Requires: ");
            ArrayList<Task> already = new ArrayList<>();
            already.add(rootTask);
            int index = 0;
            for(Task requires : rootTask.requires){
                printTaskTreeRecursive(requires, 0,index++);
            }
            RetroStorage.LOGGER.info("-E-");
        }
    }

    public static void printTaskTreeRecursive(Task task, int depth, int index){
        String space = String.join("", Collections.nCopies(depth + 2, " "));
        RetroStorage.LOGGER.info(space+"-B-");
        RetroStorage.LOGGER.info(space+"TASK "+index+" DEPTH "+depth+": " + task.toString());
        RetroStorage.LOGGER.info(space+"Requires: ");
        if(task.requires.size() > 0){
            for(Task requires : task.requires){
                printTaskTreeRecursive(requires, depth+1,index);
            }
        } else {
            RetroStorage.LOGGER.info(space+"  Nothing");
        }
        RetroStorage.LOGGER.info(space+"-E-");
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

    public static void printCompound(NBTTagCompound tag) {
        printCompound(tag, 0);
    }

    public static void printCompound(NBTTagCompound tag, int n) {
        HashMap<Object, Object> map;
        n++;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append(" ");
        }
        try {
            map = (HashMap<Object, Object>) getPrivateValue(NBTTagCompound.class, tag, 0);
            if (map != null) {
                int finalN = n;
                map.forEach((K, V) -> {
                    LOGGER.info(str.toString() + K + ": " + V);
                    //entityPlayer.addChatMessage(str.toString()+K + ": " + V);
                    if (V instanceof NBTTagCompound) {
                        printCompound((NBTTagCompound) V, finalN);
                    }
                });
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack findRecipeResultFromNBT(NBTTagCompound nbt){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        for(Object tag : nbt.func_28110_c()){
            if(tag instanceof NBTTagCompound){
                ItemStack stack = new ItemStack((NBTTagCompound) tag);
                if(stack.itemID != 0 && stack.stackSize != 0){
                    crafting.setInventorySlotContents(Integer.parseInt(((NBTTagCompound) tag).getKey()),stack);
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

    public static IRecipe findRecipeFromNBT(NBTTagCompound nbt){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        for(Object tag : nbt.func_28110_c()){
            if(tag instanceof NBTTagCompound){
                ItemStack stack = new ItemStack((NBTTagCompound) tag);
                if(stack.itemID != 0 && stack.stackSize != 0){
                    crafting.setInventorySlotContents(Integer.parseInt(((NBTTagCompound) tag).getKey()),stack);
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

    public static ArrayList<ArrayList<NBTTagCompound>> findProcessesByOutput(ItemStack output, DigitalNetwork network){
        ArrayList<ArrayList<NBTTagCompound>> foundProcesses = new ArrayList<>();
        if(network != null){
            ArrayList<ArrayList<NBTTagCompound>> availableProcesses = network.getAvailableProcesses();
            if(output != null){
                for(ArrayList<NBTTagCompound> process : availableProcesses){
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

    public static ArrayList<ItemStack> itemsNBTToArray(NBTTagCompound nbt){
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

    public static NBTTagCompound itemsArrayToNBT(ArrayList<ItemStack> list){
        NBTTagCompound recipeNBT = (new NBTTagCompound());
        //System.out.println(recipe.size());
        for(int i = 0;i<list.size();i++) {
            NBTTagCompound itemNBT = (new NBTTagCompound());
            ItemStack item = list.get(i);
            if (item == null) {
                recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
                continue;
            }
            item.writeToNBT(itemNBT);
            recipeNBT.setCompoundTag(Integer.toString(i), itemNBT);
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

    public static ItemStack getMainOutputOfProcess(ArrayList<NBTTagCompound> tasks){
        for(NBTTagCompound task : tasks){
            boolean isOutput = task.getBoolean("isOutput");
            if(isOutput){
                return new ItemStack(task.getCompoundTag("stack"));
            }
        }
        return null;
    }



}
