package net.sunsetsatellite.retrostorage;

import cpw.mods.fml.common.ReflectionHelper;
import forge.MinecraftForgeClient;
import forge.NetworkMod;
import forge.oredict.OreDictionary;
import ic2.common.AdvRecipe;
import ic2.common.AdvShapelessRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.blocks.BlockCable;
import net.sunsetsatellite.retrostorage.blocks.BlockDigitalMachine;
import net.sunsetsatellite.retrostorage.items.*;
import net.sunsetsatellite.retrostorage.tiles.*;
import net.sunsetsatellite.retrostorage.util.Config;
import net.sunsetsatellite.retrostorage.util.DigitalNetwork;
import net.sunsetsatellite.retrostorage.util.InventoryAutocrafting;
import net.sunsetsatellite.retrostorage.util.RenderCable;

import java.util.*;
import java.util.logging.Logger;
public class mod_RetroStorage extends NetworkMod {
    public static Logger LOGGER = Logger.getLogger("RetroStorage");

    public static int digitalControllerTex = 7;
    public static int digitalChestFront = 4;
    public static int machineSide = 5;
    public static int digitalChestTop = 6;
    public static int discDriveFront = 8;
    public static int networkCableTex = 3;
    public static int assemblerSide = 2;
    public static int recipeEncoderTop = 16;
    public static int recipeEncoderFront = 15;
    public static int requestTerminalFront = 17;
    public static int importerTex = 12;
    public static int exporterTex = 11;
    public static int advInterfaceSide = 0;
    public static int processProgrammerFront = 13;
    public static int processProgrammerTop = 14;
    public static int advMachineSide = 1;
    public static int wirelessLinkTex = 18;
    public static int emitterActive = 9;
    public static int emitterInactive = 10;

    public static int[][] sprites = new int[][]{
            {
                    digitalControllerTex,
            },
            {
                    machineSide, //bottom
                    machineSide, //top
                    machineSide, //side
                    digitalChestFront, //front
            },
            {
                    machineSide,
                    machineSide,
                    machineSide,
                    discDriveFront
            },
            {
                    machineSide,
                    recipeEncoderTop,
                    assemblerSide,
                    assemblerSide
            },
            {
                    importerTex
            },
            {
                    exporterTex
            },
            {
                    machineSide,
                    machineSide,
                    machineSide,
                    requestTerminalFront,
            },
            {
                    machineSide,
                    recipeEncoderTop,
                    machineSide,
                    recipeEncoderFront,
            },
            {
                    advInterfaceSide
            },
            {
                    advMachineSide,
                    processProgrammerTop,
                    advMachineSide,
                    processProgrammerFront,
            },
            {
                    wirelessLinkTex,
            },
            {
                    emitterInactive,
            }

    };

    public enum machines {
        digitalController, digitalTerminal, discDrive, assembler, importer, exporter, requestTerminal,
        recipeEncoder, advInterface, processProgrammer, wirelessLink, emitter
    }

    private static int itemId = 5000;

    public static Block digitalMachine = (new BlockDigitalMachine(getId("digitalMachine",251)).setBlockName("digitalMachine"));
    public static Block networkCable = (new BlockCable(getId("networkCable",252)).setBlockName("networkCable"));

    public static ItemReS blankDisc = simpleItem(itemId++,"blankDisc",3);
    public static ItemStorageDisc storageDisc1 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc1",itemId++),64).setItemName("storageDisc1").setIconIndex(14);
    public static ItemStorageDisc storageDisc2 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc2",itemId++),64*2).setItemName("storageDisc2").setIconIndex(15);
    public static ItemStorageDisc storageDisc3 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc3",itemId++),64*3).setItemName("storageDisc3").setIconIndex(16);
    public static ItemStorageDisc storageDisc4 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc4",itemId++),64*4).setItemName("storageDisc4").setIconIndex(17);
    public static ItemStorageDisc storageDisc5 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc5",itemId++),64*5).setItemName("storageDisc5").setIconIndex(18);
    public static ItemStorageDisc storageDisc6 = (ItemStorageDisc) new ItemStorageDisc(getId("storageDisc6",itemId++),64*6).setItemName("storageDisc6").setIconIndex(19);
    public static ItemStorageDisc virtualDisc = (ItemStorageDisc) new ItemStorageDisc(getId("virtualDisc",itemId++),(Short.MAX_VALUE*2)+1).setItemName("virtualDisc").setIconIndex(37);
    public static ItemRecipeDisc recipeDisc = (ItemRecipeDisc) new ItemRecipeDisc(getId("recipeDisc",itemId++)).setItemName("recipeDisc").setIconIndex(31);
    public static ItemAdvRecipeDisc advRecipeDisc = (ItemAdvRecipeDisc) new ItemAdvRecipeDisc(getId("advRecipeDisc",itemId++)).setItemName("advRecipeDisc").setIconIndex(1);
    public static Item blankCard = simpleItem(itemId++,"blankCard",2);
    public static ItemLinkingCard linkingCard = (ItemLinkingCard) new ItemLinkingCard(getId("linkingCard",itemId++)).setItemName("linkingCard").setIconIndex(25);

    public static Item machineCasing = simpleItem(itemId++,"machineCasing",27);
    public static Item advMachineCasing = simpleItem(itemId++,"advMachineCasing",0); 
    public static Item energyCore = simpleItem(itemId++,"energyCore",20); 
    public static Item chipShell = simpleItem(itemId++,"chipShell",7); 
    public static Item chipShellFilled = simpleItem(itemId++,"chipShellFilled",21); 
    public static Item chipDigitizer = simpleItem(itemId++,"chipDigitizer",12); 
    public static Item chipCrafting = simpleItem(itemId++,"chipCrafting",9); 
    public static Item chipDematerializer = simpleItem(itemId++,"chipDematerializer",10); 
    public static Item chipRematerializer = simpleItem(itemId++,"chipRematerializer",33); 
    public static Item chipDieDigitizer = simpleItem(itemId++,"chipDieDigitizer",13); 
    public static Item chipDieCrafting = simpleItem(itemId++,"chipDieCrafting",8); 
    public static Item chipDieRematerializer = simpleItem(itemId++,"chipDieRematerializer",34); 
    public static Item chipDieDematerializer = simpleItem(itemId++,"chipDieDematerializer",11); 
    public static Item silicon = simpleItem(itemId++,"silicon",35); 
    public static Item siliconWafer = simpleItem(itemId++,"siliconWafer",36); 
    public static Item ceramicPlate = simpleItem(itemId++,"ceramicPlate",5); 
    public static Item ceramicPlateUnfired = simpleItem(itemId++,"ceramicPlateUnfired",6); 
    public static Item chipDieWireless = simpleItem(itemId++,"chipDieWireless",40); 
    public static Item chipWireless = simpleItem(itemId++,"chipWireless",39); 
    public static Item wirelessAntenna = simpleItem(itemId++,"wirelessAntenna",38); 
    public static Item redstoneCore = simpleItem(itemId++,"redstoneCore",32); 
    
    public static int networkCableRenderID;

    @Override
    public String getVersion() {
        return "b1.0-1.2.5";
    }


    @Override
    public void load() {
        Config.init();
        ModLoader.setInGameHook(this, true, false);
        ModLoader.setInGUIHook(this, true, false);

        BlockCable.loadSprites();
        networkCableRenderID = ModLoader.getUniqueBlockModelID(this, false);

        ModLoader.registerBlock(digitalMachine, ItemDigitalMachineBlock.class);
        ModLoader.registerBlock(networkCable, ItemCableBlock.class);
        ModLoader.addName(networkCable,"Network Cable");
        ModLoader.addLocalization("tile.digitalMachine.name","Digital Machine");
        ModLoader.addLocalization("tile.digitalController.name", "Digital Controller");
        ModLoader.addLocalization("tile.discDrive.name", "Disc Drive");
        ModLoader.addLocalization("tile.digitalTerminal.name", "Digital Terminal");
        ModLoader.addLocalization("tile.importer.name", "Item Importer");
        ModLoader.addLocalization("tile.exporter.name", "Item Exporter");
        ModLoader.addLocalization("tile.recipeEncoder.name", "Recipe Encoder");
        ModLoader.addLocalization("tile.assembler.name", "Assembler");
        ModLoader.addLocalization("tile.requestTerminal.name", "Request Terminal");
        ModLoader.addLocalization("tile.advInterface.name", "Adv. Item Interface");
        ModLoader.addLocalization("tile.processProgrammer.name", "Process Programmer");
        ModLoader.addLocalization("tile.wirelessLink.name","Wireless Link");
        ModLoader.addLocalization("tile.emitter.name","Redstone Emitter");
        ModLoader.addLocalization("action.retrostorage.terminalBound","Bound!");
        ModLoader.addLocalization("action.retrostorage.clearTaskQueue","Task queue cleared!");
        ModLoader.addLocalization("action.retrostorage.cardUnbound","Card unbound!");
        ModLoader.addLocalization("action.retrostorage.linkInvalidBlock","Link failed! Card bound to invalid block!");
        ModLoader.addLocalization("action.retrostorage.linkEstablished","Link established!");
        ModLoader.addLocalization("action.retrostorage.cardBound","Card bound!");
        ModLoader.addLocalization("action.retrostorage.linkBroken","Link broken!");
        ModLoader.addLocalization("action.retrostorage.linkUnlinked","Unlinked.");
        ModLoader.addLocalization("action.retrostorage.linkLinked","Linked to:");
        ModLoader.addName(blankDisc,"Blank Disc");
        ModLoader.addName(storageDisc1, "Storage Disc MK I");
        ModLoader.addName(storageDisc2, "Storage Disc MK II");
        ModLoader.addName(storageDisc3, "Storage Disc MK III");
        ModLoader.addName(storageDisc4, "Storage Disc MK IV");
        ModLoader.addName(storageDisc5, "Storage Disc MK V");
        ModLoader.addName(storageDisc6, "Storage Disc MK VI");
        ModLoader.addName(recipeDisc,"Recipe Disc");
        ModLoader.addName(virtualDisc,"Virtual Disc");
        ModLoader.addName(advRecipeDisc,"Adv. Recipe Disc");
        ModLoader.addName(linkingCard,"Linking Card");
        ModLoader.addName(blankCard, "Blank Card");
        ModLoader.addName(advRecipeDisc, "Adv. Recipe Disc");
        ModLoader.addName(linkingCard,"Linking Card");
        ModLoader.addName(machineCasing,"Machine Casing");
        ModLoader.addName(advMachineCasing,"Adv. Machine Casing");
        ModLoader.addName(energyCore,"Energy Core");
        ModLoader.addName(chipShell,"Chip Shell");
        ModLoader.addName(chipShellFilled,"Filled Chip Shell");
        ModLoader.addName(chipDigitizer,"Digitizer Chip");
        ModLoader.addName(chipCrafting,"Crafting Processor");
        ModLoader.addName(chipRematerializer,"Rematerializer Chip");
        ModLoader.addName(chipDematerializer,"Dematerializer Chip");
        ModLoader.addName(chipDieDigitizer,"Digitizer Chip Die");
        ModLoader.addName(chipDieCrafting,"Crafting Processor Die");
        ModLoader.addName(chipDieDematerializer,"Dematerializer Chip Die");
        ModLoader.addName(chipDieRematerializer,"Rematerializer Chip Die");
        ModLoader.addName(silicon,"Raw Silicon");
        ModLoader.addName(siliconWafer,"Silicon Wafer");
        ModLoader.addName(ceramicPlate,"Ceramic Plate");
        ModLoader.addName(ceramicPlateUnfired,"Unfired Ceramic Plate");
        ModLoader.addName(chipWireless,"Wireless Networking Chip");
        ModLoader.addName(chipDieWireless,"Wireless Networking Chip Die");
        ModLoader.addName(wirelessAntenna,"Wireless Antenna");
        ModLoader.addName(redstoneCore, "Redstone Core");
        
        ModLoader.addRecipe(new ItemStack(blankDisc, 1), "GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(recipeDisc, 1), "GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.redstone, 'P', new ItemStack(Item.dyePowder, 1, 5));
        ModLoader.addRecipe(new ItemStack(storageDisc1, 1), "RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(networkCable, 16), "WLW", "GGG", "WLW", 'W', Block.cloth, 'G', Block.glass, 'L', new ItemStack(Item.dyePowder, 1, 4));
        ModLoader.addRecipe(new ItemStack(storageDisc2, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc1, '#', new ItemStack(Item.dyePowder, 1, 14), 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(storageDisc3, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc2, '#', new ItemStack(Item.dyePowder, 1, 11), 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(storageDisc4, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc3, '#', new ItemStack(Item.dyePowder, 1, 10), 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(storageDisc5, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc4, '#', new ItemStack(Item.dyePowder, 1, 4), 'R', Item.redstone);
        ModLoader.addRecipe(new ItemStack(storageDisc6, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc5, '#', new ItemStack(Item.dyePowder, 1, 5), 'R', Item.redstone);
        
        ModLoader.addRecipe(new ItemStack(blankCard, 4), "ISI", "SPS", "ISI", 'I', Item.ingotIron, 'S', Block.stone, 'P', Block.pressurePlateStone);
        //ModLoader.addRecipe(new ItemStack(lockingCard, 1), "RTR", "TCT", "RTR", 'R', Item.redstone, 'T', Block.torchRedstoneActive, 'C', blankCard);
        ModLoader.addRecipe(new ItemStack(advRecipeDisc, 1), "OPO", "RDR", "OPO", 'R', Item.redstone, 'D', recipeDisc, 'P', new ItemStack(Item.dyePowder, 1, 5), 'O', Block.obsidian);
        ModLoader.addRecipe(new ItemStack(siliconWafer,1),"SS","SS",'S',silicon);
        ModLoader.addRecipe(new ItemStack(linkingCard,1,0),"123","456","789",'1',new ItemStack(Item.dyePowder,1,12),'2',new ItemStack(Item.dyePowder,1,4),'3',new ItemStack(Item.dyePowder,1,12),'4',new ItemStack(Item.dyePowder,1,4),'5',blankCard,'6',new ItemStack(Item.dyePowder,1,4),'7',new ItemStack(Item.dyePowder,1,12),'8',chipWireless,'9',new ItemStack(Item.dyePowder,1,12));
        ModLoader.addRecipe(new ItemStack(ceramicPlateUnfired,1),"123","456","789",'4',Item.clay,'5',Item.clay,'6',Item.clay,'7',Item.clay,'8',Item.clay,'9',Item.clay);
        ModLoader.addRecipe(new ItemStack(energyCore,1,0),"123","456","789",'1',Block.glass,'2',Block.glowStone,'3',Block.glass,'4',Block.glowStone,'5',Block.blockDiamond,'6',Block.glowStone,'7',Block.glass,'8',Block.glowStone,'9',Block.glass);
        ModLoader.addRecipe(new ItemStack(machineCasing,1,0),"123","456","789",'1',Block.stone,'2',Item.ingotIron,'3',Block.stone,'4',Item.ingotIron,'6',Item.ingotIron,'7',Block.stone,'8',Item.ingotIron,'9',Block.stone);
        ModLoader.addRecipe(new ItemStack(advMachineCasing,1,0),"123","456","789",'1',Block.obsidian,'2',Item.diamond,'3',Block.obsidian,'4',Item.diamond,'5',machineCasing,'6',Item.diamond,'7',Block.obsidian,'8',Item.diamond,'9',Block.obsidian);
        ModLoader.addRecipe(new ItemStack(chipShell,1,0),"123","456","789",'1',Item.ingotGold,'3',Item.ingotGold,'5',ceramicPlate,'7',Item.ingotGold,'9',Item.ingotGold);
        ModLoader.addRecipe(new ItemStack(chipShellFilled,1,0),"123","456","789",'1',Item.redstone,'2',Item.redstone,'3',Item.redstone,'4',Item.redstone,'5',chipShell,'6',Item.redstone,'7',Item.redstone,'8',Item.redstone,'9',Item.redstone);
        ModLoader.addRecipe(new ItemStack(chipDieRematerializer,1,0),"2","5","8",'2',Block.obsidian,'5',siliconWafer,'8',new ItemStack(Item.dyePowder,1,10));
        ModLoader.addRecipe(new ItemStack(chipDieDematerializer,1,0),"2","5","8",'2',Item.bucketLava,'5',siliconWafer,'8',Item.redstone);
        ModLoader.addRecipe(new ItemStack(chipDieCrafting,1,0),"2","5","8",'2',recipeDisc,'5',siliconWafer,'8',new ItemStack(Item.dyePowder,1,5));
        ModLoader.addRecipe(new ItemStack(chipDieDigitizer,1,0),"2","5","8",'2',Item.diamond,'5',siliconWafer,'8',new ItemStack(Item.dyePowder,1,4));
        ModLoader.addRecipe(new ItemStack(chipDematerializer,1,0),"2","5","8",'2',ceramicPlate,'5',chipDieDematerializer,'8',chipShellFilled);
        ModLoader.addRecipe(new ItemStack(chipRematerializer,1,0),"2","5","8",'2',ceramicPlate,'5',chipDieRematerializer,'8',chipShellFilled);
        ModLoader.addRecipe(new ItemStack(chipCrafting,1,0),"2","5","8",'2',ceramicPlate,'5',chipDieCrafting,'8',chipShellFilled);
        ModLoader.addRecipe(new ItemStack(chipDigitizer,1,0),"2","5","8",'2',ceramicPlate,'5',chipDieDigitizer,'8',chipShellFilled);
        ModLoader.addRecipe(new ItemStack(chipDieWireless,1,0),"123","456","789",'2',Item.diamond,'4',Block.blockLapis,'5',siliconWafer,'6',Block.blockLapis,'8',Item.diamond);
        ModLoader.addRecipe(new ItemStack(chipWireless,1,0),"123","456","789",'2',ceramicPlate,'5',chipDieWireless,'8',chipShellFilled);
        ModLoader.addRecipe(new ItemStack(wirelessAntenna,1,0),"123","456","789",'2',Block.blockLapis,'4',Block.blockLapis,'5',Item.diamond,'6',Block.blockLapis,'7',Item.ingotIron,'8',Item.stick,'9',Item.ingotIron);
        ModLoader.addRecipe(new ItemStack(redstoneCore,1,0),"RRR","RRR","RRR",'R',Item.redstone);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.digitalController.ordinal()),"123","456","789",'1',machineCasing,'2',networkCable,'3',Block.blockLapis,'4',networkCable,'5',energyCore,'6',networkCable,'7',Block.blockLapis,'8',networkCable,'9',machineCasing);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.digitalTerminal.ordinal()),"123","456","789",'2',chipDigitizer,'4',chipRematerializer,'5',machineCasing,'6',chipDematerializer,'7',networkCable,'8',Block.chest,'9',networkCable);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.discDrive.ordinal()),"123","456","789",'2',chipDigitizer,'4',blankDisc,'5',machineCasing,'6',blankDisc,'8',networkCable);
        //ModLoader.addRecipe(new ItemStack(digitalChest,1,0),"123","456","789",'2',blankDisc,'4',chipRematerializer,'5',machineCasing,'6',chipDematerializer,'8',Block.chest);
        //ModLoader.addRecipe(new ItemStack(storageContainer,1,0),"123","456","789",'1',machineCasing,'2',machineCasing,'3',machineCasing,'4',Item.ingotIron,'5',Block.chest,'6',Item.ingotIron,'7',machineCasing,'8',machineCasing,'9',machineCasing);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.assembler.ordinal()),"123","456","789",'1',machineCasing,'2',chipCrafting,'3',machineCasing,'4',chipCrafting,'5',recipeDisc,'6',chipCrafting,'7',machineCasing,'8',chipCrafting,'9',machineCasing);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.importer.ordinal()),"123","456","789",'2',chipRematerializer,'4',networkCable,'5',machineCasing,'6',networkCable,'8',chipDigitizer);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.exporter.ordinal()),"123","456","789",'2',chipDematerializer,'4',networkCable,'5',machineCasing,'6',networkCable,'8',chipDigitizer);
        //ModLoader.addRecipe(new ItemStack(digitalMachine,1,7),"123","456","789",'2',new ItemStack(digitalMachine,1,5),'3',chipDigitizer,'4',networkCable,'5',machineCasing,'6',networkCable,'7',chipCrafting,'8',new ItemStack(digitalMachine,1,4));
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.requestTerminal.ordinal()),"123","456","789",'2',machineCasing,'4',chipCrafting,'5',new ItemStack(digitalMachine,1,machines.digitalTerminal.ordinal()),'6',chipCrafting,'8',networkCable);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.recipeEncoder.ordinal()),"123","456","789",'2',machineCasing,'4',recipeDisc,'5',Block.workbench,'6',recipeDisc,'8',chipCrafting);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.advInterface.ordinal()),"123","456","789",'1',Block.workbench,'2',advRecipeDisc,'3',Block.workbench,'4',chipCrafting,'5',new ItemStack(digitalMachine,1,machines.recipeEncoder.ordinal()),'6',chipCrafting,'7',Block.workbench,'8',advMachineCasing,'9',Block.workbench);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.processProgrammer.ordinal()),"123","456","789",'1',Block.obsidian,'2',advRecipeDisc,'3',Block.obsidian,'4',chipCrafting,'5',new ItemStack(digitalMachine,1,machines.recipeEncoder.ordinal()),'6',chipDigitizer,'7',Block.obsidian,'8',advMachineCasing,'9',Block.obsidian);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.wirelessLink.ordinal()),"123","456","789",'2',chipWireless,'4',networkCable,'5',machineCasing,'6',wirelessAntenna,'8',chipWireless);
        ModLoader.addRecipe(new ItemStack(digitalMachine,1,machines.emitter.ordinal()),"123","456","789",'1',machineCasing,'2',Block.torchRedstoneActive,'3',machineCasing,'4',networkCable,'5',redstoneCore,'6',chipDigitizer,'7',machineCasing,'8',Item.redstoneRepeater,'9',machineCasing);

        //ModLoader.addRecipe(new ItemStack(mobileTerminal,1,0),"123","456","789",'1',Item.ingotIron,'2',wirelessAntenna,'3',Item.ingotIron,'4',Block.glass,'5',new ItemStack(digitalMachine,1,1),'6',Block.glass,'7',Item.ingotIron,'8',chipWireless,'9',Item.ingotIron);
        //ModLoader.addRecipe(new ItemStack(mobileRequestTerminal,1,0),"123","456","789",'1',Item.ingotIron,'2',wirelessAntenna,'3',Item.ingotIron,'4',Block.glass,'5',new ItemStack(digitalMachine,1,6),'6',Block.glass,'7',Item.ingotIron,'8',chipWireless,'9',Item.ingotIron);

        ModLoader.addSmelting(Block.glass.blockID,new ItemStack(silicon,1));
        ModLoader.addSmelting(ceramicPlateUnfired.shiftedIndex,new ItemStack(ceramicPlate,1));
        
        ModLoader.registerTileEntity(TileEntityNetworkDevice.class,"Network Device");
        ModLoader.registerTileEntity(TileEntityDigitalController.class,"Digital Controller");
        ModLoader.registerTileEntity(TileEntityDiscDrive.class,"Disc Drive");
        ModLoader.registerTileEntity(TileEntityDigitalTerminal.class,"Digital Terminal");
        ModLoader.registerTileEntity(TileEntityRequestTerminal.class,"Request Terminal");
        ModLoader.registerTileEntity(TileEntityRecipeEncoder.class,"Recipe Encoder");
        ModLoader.registerTileEntity(TileEntityImporter.class,"Item Importer");
        ModLoader.registerTileEntity(TileEntityExporter.class,"Item Exporter");
        ModLoader.registerTileEntity(TileEntityAssembler.class,"Assembler");
        ModLoader.registerTileEntity(TileEntityWirelessLink.class,"Wireless Link");
        ModLoader.registerTileEntity(TileEntityAdvInterface.class,"Adv. Item Interface");
        ModLoader.registerTileEntity(TileEntityProcessProgrammer.class,"Process Programmer");
        ModLoader.registerTileEntity(TileEntityRedstoneEmitter.class,"Redstone Emitter");

        MinecraftForgeClient.preloadTexture("/assets/retrostorage/blocks.png");
        MinecraftForgeClient.preloadTexture("/assets/retrostorage/items.png");
        LOGGER.info("RetroStorage "+getVersion()+" loaded!");
    }


    public static ItemReS simpleItem(int id, String name, int texture){
        return (ItemReS) new ItemReS(getId(name,id)).setItemName(name).setIconIndex(texture);
    }

    public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l) {
        return block.getRenderType() == networkCableRenderID && RenderCable.render(renderblocks, iblockaccess, i, j, k, block, l);
    }

    public static IRecipe findRecipeFromNBT(NBTTagCompound nbt){
        InventoryAutocrafting crafting = new InventoryAutocrafting(3,3);
        for(Object tag : nbt.getTags()){
            if(tag instanceof NBTTagCompound){
                ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);//new ItemStack((NBTTagCompound) tag);
                if(stack != null && stack.itemID != 0 && stack.stackSize != 0){
                    crafting.setInventorySlotContents(Integer.parseInt(((NBTTagCompound) tag).getName()),stack);
                }
            }
        }
        CraftingManager craftingManager = CraftingManager.getInstance();
        return findMatchingRecipe(crafting, craftingManager);
    }

    public static int getId(String s, int base) {
        Integer id = Config.getFromConfig(s, base);
        LOGGER.info("Getting id for " + s + ": " + (id == null ? "null (using default: " + base + ")" : id.toString()));
        return id == null ? base : Config.getFromConfig(s, base);
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

    public static IRecipe findMatchingRecipe(InventoryCrafting inventorycrafting, CraftingManager manager) {
        List recipes;
        recipes = ReflectionHelper.getPrivateValue(CraftingManager.class,manager,1);
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

    public static ArrayList<ItemStack> getRecipeItems(IRecipe recipe){
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if(recipe instanceof ShapelessRecipes){
            List<ItemStack> recipeItems = ReflectionHelper.getPrivateValue(ShapelessRecipes.class,(ShapelessRecipes) recipe,1);
            inputs = new ArrayList<>(recipeItems);
        }
        if(recipe instanceof AdvRecipe){
            Object[] recipeInputs = ((AdvRecipe) recipe).input;
            ItemStack[] recipeItems = new ItemStack[recipeInputs.length];
            for (int i = 0; i < recipeInputs.length; i++) {
                Object input = recipeInputs[i];
                if(input instanceof ItemStack){
                    recipeItems[i] = (ItemStack) input;
                } else if(input instanceof String) {
                    ItemStack oreDictStack = OreDictionary.getOres((String) input).get(0);
                    recipeItems[i] = oreDictStack;
                }
            }
            Collections.addAll(inputs,recipeItems);
        }
        if(recipe instanceof AdvShapelessRecipe){
            Object[] recipeInputs = ((AdvRecipe) recipe).input;
            ItemStack[] recipeItems = new ItemStack[recipeInputs.length];
            for (int i = 0; i < recipeInputs.length; i++) {
                Object input = recipeInputs[i];
                if(input instanceof ItemStack){
                    recipeItems[i] = (ItemStack) input;
                } else if(input instanceof String) {
                    ItemStack oreDictStack = OreDictionary.getOres((String) input).get(0);
                    recipeItems[i] = oreDictStack;
                }
            }
            Collections.addAll(inputs,recipeItems);
        }
        if(recipe instanceof ShapedRecipes){
            ItemStack[] recipeItems = ReflectionHelper.getPrivateValue(ShapedRecipes.class,(ShapedRecipes) recipe,2);
            Collections.addAll(inputs, recipeItems);
        }
        inputs.removeIf(Objects::isNull);
        for (ItemStack input : inputs) {
            input.stackSize = 1;
        }

        return inputs;
    }

    public static String recipeToString(IRecipe recipe){
        String string = "";
        if(recipe instanceof ShapedRecipes || recipe instanceof AdvRecipe){
            string = "RecipeShaped{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getRecipeOutput() + "}";
        } else if(recipe instanceof ShapelessRecipes || recipe instanceof AdvShapelessRecipe){
            string = "RecipeShapeless{";
            string += condenseItemList(getRecipeItems(recipe)) + " -> " + recipe.getRecipeOutput() + "}";
        }
        return string;
    }

    public static ArrayList<IRecipe> findRecipesByOutput(ItemStack output){
        CraftingManager craftingManager = CraftingManager.getInstance();
        ArrayList<IRecipe> foundRecipes = new ArrayList<>();
        List<IRecipe> recipes = craftingManager.getRecipeList();
        for(IRecipe recipe : recipes){
            if(recipe.getRecipeOutput().getItemDamage() == -1){
                if(recipe.getRecipeOutput().itemID == output.itemID){
                    foundRecipes.add(recipe);
                }
            } else if(recipe.getRecipeOutput().isItemEqual(output)){
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
                        if(processOutput.getItemDamage() == -1){
                            if(processOutput.itemID == output.itemID){
                                foundProcesses.add(process);
                            }
                        } else if(processOutput.isItemEqual(output)){
                            foundProcesses.add(process);
                        }
                    }
                }
            }
        }
        return foundProcesses;
    }

    public static ArrayList<IRecipe> findRecipesByOutputUsingList(ItemStack output,ArrayList<IRecipe> list){
        ArrayList<IRecipe> foundRecipes = new ArrayList<>();
        for(IRecipe recipe : list){
            if(recipe.getRecipeOutput().getItemDamage() == -1){
                if(recipe.getRecipeOutput().itemID == output.itemID){
                    foundRecipes.add(recipe);
                }
            } else if(recipe.getRecipeOutput().isItemEqual(output)){
                foundRecipes.add(recipe);
            }
        }
        return foundRecipes;
    }

    public static ItemStack getMainOutputOfProcess(ArrayList<NBTTagCompound> tasks){
        for(NBTTagCompound task : tasks){
            boolean isOutput = task.getBoolean("isOutput");
            if(isOutput){
                return ItemStack.loadItemStackFromNBT(task.getCompoundTag("stack"));//new ItemStack(task.getCompoundTag("stack"));
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> condenseItemList(ArrayList<ItemStack> list){
        ArrayList<ItemStack> condensed = new ArrayList<>();
        list = (ArrayList<ItemStack>) list.clone();
        for (ItemStack stack : list) {
            stack = stack.copy();
            boolean already = false;
            for(ItemStack condensedStack : condensed){
                if(condensedStack != null){
                    if(condensedStack.getItemDamage() == -1 && condensedStack.itemID == stack.itemID){
                        condensedStack.stackSize += stack.stackSize;
                        already = true;
                        break;
                    } else if(condensedStack.isItemEqual(stack)){
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

    boolean firstTick = true;



    public boolean onTickInGame(float f, Minecraft minecraft)
    {
        if(minecraft.currentScreen == null)
        {
            creativeInventory = null;
        }
        return true;
    }

    public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen)
    {
        if((guiscreen instanceof GuiContainerCreative) && !(creativeInventory instanceof GuiContainerCreative) && !minecraft.theWorld.isRemote)
        {
            Container container = ((GuiContainer)guiscreen).inventorySlots;
            List<ItemStack> list = ((ContainerCreative)container).itemList;
            for (int i = 0; i < 12; i++) {
                list.add(new ItemStack(digitalMachine, 1, i));
            }
            list.add(new ItemStack(networkCable, 1, 0));
        }
        creativeInventory = guiscreen;
        return true;
    }

    private static GuiScreen creativeInventory;
}
