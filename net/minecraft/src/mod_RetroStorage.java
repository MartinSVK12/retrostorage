package net.minecraft.src;

import ic2.RenderBlockCable;
import net.sunsetsatellite.retrostorage.*;

import java.util.HashMap;
import java.util.Random;


public class mod_RetroStorage extends BaseMod {

    public static Config config;

    public mod_RetroStorage() {

        Config.init();

        digitalChestFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "digitalchestfront.png");
        digitalChestSide = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "digitalchestside.png");
        digitalChestTop = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "digitalchesttop.png");
        digitalChestTop2 = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "digitalchesttopfilled.png");
        discDriveFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "discdrive.png");
        cableTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "blockcable.png");
        relayOnTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "relayon.png");
        relayOffTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "relayoff.png");
        assemblerSide = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "assemblerside.png");
        recipeEncoderTop = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "recipeencodertopfilled.png");
        recipeEncoderFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "recipeencoderfront.png");
        virtualDiscTop = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "virtualdisctop.png");
        interfaceSide = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "interfaceside.png");
        requestTerminalFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "requestterminalfront.png");
        storageContainerFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "storagecontainer.png");
        digitalControllerTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "digitalcontroller.png");
        importerTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "importer.png");
        exporterTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "exporter.png");
        advInterfaceSide = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "advinterfaceside.png");
        processProgrammerTop = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "processprogrammertopfilled.png");
        processProgrammerFront = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "processprogrammerfront.png");
        advMachineSide = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "advmachineside.png");
        wirelessLinkTex = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "wirelesslink.png");
        emitterActive = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "emitteractive.png");
        emitterInactive = ModLoader.addOverride("/terrain.png", "/retrostorage/" + "emitterinactive.png");

        sprites = new int[][]{
                {
                        mod_RetroStorage.digitalControllerTex,
                },
                {
                        mod_RetroStorage.digitalChestSide, //bottom
                        mod_RetroStorage.digitalChestSide, //top
                        mod_RetroStorage.digitalChestSide, //side
                        mod_RetroStorage.digitalChestFront, //front
                },
                {
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.discDriveFront
                },
                {
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.recipeEncoderTop,
                        mod_RetroStorage.assemblerSide,
                        mod_RetroStorage.assemblerSide
                },
                {
                        mod_RetroStorage.importerTex
                },
                {
                        mod_RetroStorage.exporterTex
                },
                {
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.requestTerminalFront,
                },
                {
                        mod_RetroStorage.interfaceSide
                },
                {
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.recipeEncoderTop,
                        mod_RetroStorage.digitalChestSide,
                        mod_RetroStorage.recipeEncoderFront,
                },
                {
                        mod_RetroStorage.advInterfaceSide
                },
                {
                        mod_RetroStorage.advMachineSide,
                        mod_RetroStorage.processProgrammerTop,
                        mod_RetroStorage.advMachineSide,
                        mod_RetroStorage.processProgrammerFront,
                },
                {
                        mod_RetroStorage.wirelessLinkTex,
                },
                {
                        mod_RetroStorage.emitterInactive,
                }

        };

        BlockDigitalChest.loadSprites();
        BlockCable.loadSprites();
        BlockStorageContainer.loadSprites();

        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalChest.class, "Digital Chest");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalController.class, "Digital Controller");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDiscDrive.class, "Disc Drive");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalTerminal.class, "Digital Terminal");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityImporter.class, "Importer");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityExporter.class, "Exporter");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityRecipeEncoder.class, "Recipe Encoder");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityAssembler.class, "Assembler");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityRequestTerminal.class, "Request Terminal");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityInterface.class, "Interface");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityStorageContainer.class, "Storage Container", new TileEntityContainerRenderer());
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityProcessProgrammer.class, "Process Programmer");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityAdvInterface.class, "Adv. Interface");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityWirelessLink.class,"Wireless Link");
        ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityRedstoneEmitter.class, "Redstone Emitter");
        ModLoader.AddName(digitalChest, "Digital Chest");
        ModLoader.AddName(blankDisc, "Blank Storage Disc");
        ModLoader.AddName(storageDisc1, "Storage Disc MK I");
        ModLoader.AddName(storageDisc2, "Storage Disc MK II");
        ModLoader.AddName(storageDisc3, "Storage Disc MK III");
        ModLoader.AddName(storageDisc4, "Storage Disc MK IV");
        ModLoader.AddName(storageDisc5, "Storage Disc MK V");
        ModLoader.AddName(storageDisc6, "Storage Disc MK VI");
        ModLoader.AddName(recipeDisc, "Recipe Disc");
        ModLoader.AddLocalization("tile.digitalController.name", "Digital Controller");
        ModLoader.AddName(cable, "Network Cable");
        ModLoader.AddLocalization("tile.discDrive.name", "Disc Drive");
        ModLoader.AddLocalization("tile.digitalTerminal.name", "Digital Terminal");
        ModLoader.AddName(virtualDisc, "Virtual Disc");
        ModLoader.AddLocalization("tile.importer.name", "Item Importer");
        ModLoader.AddLocalization("tile.exporter.name", "Item Exporter");
        ModLoader.AddName(goldenDisc, "Golden Disc");
        ModLoader.AddLocalization("tile.recipeEncoder.name", "Recipe Encoder");
        ModLoader.AddLocalization("tile.assembler.name", "Assembler");
        ModLoader.AddLocalization("tile.requestTerminal.name", "Request Terminal");
        //ModLoader.AddName(itemCable, "Network Cable");
        ModLoader.AddLocalization("tile.interface.name", "Item Interface");
        ModLoader.AddLocalization("tile.advInterface.name", "Adv. Item Interface");
        ModLoader.AddName(mobileTerminal, "Mobile Terminal");
        ModLoader.AddName(mobileRequestTerminal, "Mobile Request Terminal");
        ModLoader.AddName(blankCard, "Blank Card");
        ModLoader.AddName(boosterCard, "Booster Card");
        ModLoader.AddName(lockingCard, "Locking Card");
        ModLoader.AddName(storageContainer, "Storage Container");
        ModLoader.AddName(advRecipeDisc, "Adv. Recipe Disc");
        ModLoader.AddName(linkingCard,"Linking Card");
        ModLoader.AddLocalization("tile.processProgrammer.name", "Process Programmer");
        ModLoader.AddLocalization("tile.wirelessLink.name","Wireless Link");
        ModLoader.AddLocalization("tile.emitter.name","Redstone Emitter");
        ModLoader.AddName(machineCasing,"Machine Casing");
        ModLoader.AddName(advNachineCasing,"Adv. Machine Casing");
        ModLoader.AddName(energyCore,"Energy Core");
        ModLoader.AddName(chipShell,"Chip Shell");
        ModLoader.AddName(chipShellFilled,"Filled Chip Shell");
        ModLoader.AddName(chipDigitizer,"Digitizer Chip");
        ModLoader.AddName(chipCrafting,"Crafting Processor");
        ModLoader.AddName(chipRematerializer,"Rematerializer Chip");
        ModLoader.AddName(chipDematerializer,"Dematerializer Chip");
        ModLoader.AddName(chipDieDigitizer,"Digitizer Chip Die");
        ModLoader.AddName(chipDieCrafting,"Crafting Processor Die");
        ModLoader.AddName(chipDieDematerializer,"Dematerializer Chip Die");
        ModLoader.AddName(chipDieRematerializer,"Rematerializer Chip Die");
        ModLoader.AddName(silicon,"Raw Silicon");
        ModLoader.AddName(siliconWafer,"Silicon Wafer");
        ModLoader.AddName(ceramicPlate,"Ceramic Plate");
        ModLoader.AddName(ceramicPlateUnfired,"Unfired Ceramic Plate");
        ModLoader.AddName(chipWireless,"Wireless Networking Chip");
        ModLoader.AddName(chipDieWireless,"Wireless Networking Chip Die");
        ModLoader.AddName(wirelessAntenna,"Wireless Antenna");
        ModLoader.AddName(redstoneCore, "Redstone Core");

        ModLoader.RegisterBlock(digitalChest);
        ModLoader.RegisterBlock(cable, ItemBlockCable.class);
        ModLoader.RegisterBlock(storageContainer, ItemBlockStorageContainer.class);
        ModLoader.RegisterBlock(multiID, ItemDigitalMachineBlock.class);


        //ModLoader.AddRecipe(new ItemStack(digitalChest, 1), "ISI", "RCR", "IDI", 'S', blankDisc, 'I', Item.ingotIron, 'R', Item.redstone, 'C', Block.chest, 'D', Block.blockDiamond);
        ModLoader.AddRecipe(new ItemStack(blankDisc, 1), "GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(recipeDisc, 1), "GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.redstone, 'P', new ItemStack(Item.dyePowder, 1, 5));
        ModLoader.AddRecipe(new ItemStack(storageDisc1, 1), "RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(cable, 16), "WLW", "GGG", "WLW", 'W', Block.cloth, 'G', Block.glass, 'L', new ItemStack(Item.dyePowder, 1, 4));
        /*ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalController.ordinal()), "ILI", "LDL", "ILI", 'I', Block.blockSteel, 'L', Block.blockLapis, 'D', Block.blockDiamond);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.discDrive.ordinal()), "III", "DDD", "III", 'I', Item.ingotIron, 'D', blankDisc);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalTerminal.ordinal()), "III", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.importer.ordinal()), "ILI", "GHG", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis, 'G', new ItemStack(Item.dyePowder, 1, 10), 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.exporter.ordinal()), "ILI", "RHR", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis, 'R', new ItemStack(Item.dyePowder, 1, 1), 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalInterface.ordinal()), "IRI", "PHP", "ICI", 'I', Item.ingotIron, 'P', new ItemStack(Item.dyePowder, 1, 5), 'R', recipeDisc, 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.assembler.ordinal()), "ICI", "CRC", "ICI", 'I', Block.blockSteel, 'C', Block.workbench, 'R', recipeDisc);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.recipeEncoder.ordinal()), "IRI", "PCP", "IcI", 'I', Item.ingotIron, 'C', Block.workbench, 'R', recipeDisc, 'P', new ItemStack(Item.dyePowder, 1, 5), 'c', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, 6), "ITI", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable, 'T', Block.workbench);*/
        ModLoader.AddRecipe(new ItemStack(storageDisc2, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc1, '#', new ItemStack(Item.dyePowder, 1, 14), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc3, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc2, '#', new ItemStack(Item.dyePowder, 1, 11), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc4, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc3, '#', new ItemStack(Item.dyePowder, 1, 10), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc5, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc4, '#', new ItemStack(Item.dyePowder, 1, 4), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc6, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc5, '#', new ItemStack(Item.dyePowder, 1, 5), 'R', Item.redstone);
        //ModLoader.AddRecipe(new ItemStack(mobileTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass, 'I', Item.ingotIron, 'D', Item.diamond, 'T', new ItemStack(multiID, 1, 1));
        //ModLoader.AddRecipe(new ItemStack(mobileRequestTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass, 'I', Item.ingotIron, 'D', Item.diamond, 'T', new ItemStack(multiID, 1, 6));
        //ModLoader.AddRecipe(new ItemStack(storageContainer, 1), "SSS", "ICI", "SSS", 'S', Block.stone, 'I', Item.ingotIron, 'C', Block.chest);
        ModLoader.AddRecipe(new ItemStack(blankCard, 4), "ISI", "SPS", "ISI", 'I', Item.ingotIron, 'S', Block.stone, 'P', Block.pressurePlateStone);
        ModLoader.AddRecipe(new ItemStack(lockingCard, 1), "RTR", "TCT", "RTR", 'R', Item.redstone, 'T', Block.torchRedstoneActive, 'C', blankCard);
        //ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.advInterface.ordinal()), "ODO", "PIP", "OCO", 'O', Block.obsidian, 'D', advRecipeDisc, 'I', new ItemStack(multiID, 1, machines.digitalInterface.ordinal()), 'C', itemCable, 'P', new ItemStack(Item.dyePowder, 1, 5));
        ModLoader.AddRecipe(new ItemStack(advRecipeDisc, 1), "OPO", "RDR", "OPO", 'R', Item.redstone, 'D', recipeDisc, 'P', new ItemStack(Item.dyePowder, 1, 5), 'O', Block.obsidian);
        //ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.processProgrammer.ordinal()), "ODO", "CRC", "OIO", 'O', Block.obsidian, 'D', advRecipeDisc, 'C', Block.workbench, 'I', new ItemStack(multiID, 1, machines.advInterface.ordinal()), 'R', new ItemStack(multiID, 1, machines.recipeEncoder.ordinal()));
       // ModLoader.AddRecipe(new ItemStack(linkingCard,1), "lLl","LCL","lLl",'L',new ItemStack(Item.dyePowder,1,4),'l',new ItemStack(Item.dyePowder,1,12),'C',blankCard);
        //ModLoader.AddRecipe(new ItemStack(multiID,1,machines.wirelessLink.ordinal()),"ILI","ClC","ILI",'I',Item.ingotIron,'L',Block.blockLapis,'C',itemCable,'l',linkingCard);
        ModLoader.AddRecipe(new ItemStack(siliconWafer,1),"SS","SS",'S',silicon);

        //these were autogenerated
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.ceramicPlateUnfired,1),"123","456","789",'4',Item.clay,'5',Item.clay,'6',Item.clay,'7',Item.clay,'8',Item.clay,'9',Item.clay);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.energyCore,1,0),"123","456","789",'1',Block.glass,'2',Block.glowStone,'3',Block.glass,'4',Block.glowStone,'5',Block.blockDiamond,'6',Block.glowStone,'7',Block.glass,'8',Block.glowStone,'9',Block.glass);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.machineCasing,1,0),"123","456","789",'1',Block.stone,'2',Item.ingotIron,'3',Block.stone,'4',Item.ingotIron,'6',Item.ingotIron,'7',Block.stone,'8',Item.ingotIron,'9',Block.stone);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.advNachineCasing,1,0),"123","456","789",'1',Block.obsidian,'2',Item.diamond,'3',Block.obsidian,'4',Item.diamond,'5',mod_RetroStorage.machineCasing,'6',Item.diamond,'7',Block.obsidian,'8',Item.diamond,'9',Block.obsidian);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipShell,1,0),"123","456","789",'1',Item.ingotGold,'3',Item.ingotGold,'5',mod_RetroStorage.ceramicPlate,'7',Item.ingotGold,'9',Item.ingotGold);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipShellFilled,1,0),"123","456","789",'1',Item.redstone,'2',Item.redstone,'3',Item.redstone,'4',Item.redstone,'5',mod_RetroStorage.chipShell,'6',Item.redstone,'7',Item.redstone,'8',Item.redstone,'9',Item.redstone);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDieRematerializer,1,0),"2","5","8",'2',Block.obsidian,'5',mod_RetroStorage.siliconWafer,'8',new ItemStack(Item.dyePowder,1,10));
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDieDematerializer,1,0),"2","5","8",'2',Item.bucketLava,'5',mod_RetroStorage.siliconWafer,'8',Item.redstone);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDieCrafting,1,0),"2","5","8",'2',mod_RetroStorage.recipeDisc,'5',mod_RetroStorage.siliconWafer,'8',new ItemStack(Item.dyePowder,1,5));
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDieDigitizer,1,0),"2","5","8",'2',Item.diamond,'5',mod_RetroStorage.siliconWafer,'8',new ItemStack(Item.dyePowder,1,4));
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDematerializer,1,0),"2","5","8",'2',mod_RetroStorage.ceramicPlate,'5',mod_RetroStorage.chipDieDematerializer,'8',mod_RetroStorage.chipShellFilled);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipRematerializer,1,0),"2","5","8",'2',mod_RetroStorage.ceramicPlate,'5',mod_RetroStorage.chipDieRematerializer,'8',mod_RetroStorage.chipShellFilled);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipCrafting,1,0),"2","5","8",'2',mod_RetroStorage.ceramicPlate,'5',mod_RetroStorage.chipDieCrafting,'8',mod_RetroStorage.chipShellFilled);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDigitizer,1,0),"2","5","8",'2',mod_RetroStorage.ceramicPlate,'5',mod_RetroStorage.chipDieDigitizer,'8',mod_RetroStorage.chipShellFilled);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipDieWireless,1,0),"123","456","789",'2',Item.diamond,'4',Block.blockLapis,'5',mod_RetroStorage.siliconWafer,'6',Block.blockLapis,'8',Item.diamond);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.chipWireless,1,0),"123","456","789",'2',mod_RetroStorage.ceramicPlate,'5',mod_RetroStorage.chipDieWireless,'8',mod_RetroStorage.chipShellFilled);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.wirelessAntenna,1,0),"123","456","789",'2',Block.blockLapis,'4',Block.blockLapis,'5',Item.diamond,'6',Block.blockLapis,'7',Item.ingotIron,'8',Item.stick,'9',Item.ingotIron);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.redstoneCore,1,0),"RRR","RRR","RRR",'R',Item.redstone);

        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,0),"123","456","789",'1',mod_RetroStorage.machineCasing,'2',mod_RetroStorage.cable,'3',Block.blockLapis,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.energyCore,'6',mod_RetroStorage.cable,'7',Block.blockLapis,'8',mod_RetroStorage.cable,'9',mod_RetroStorage.machineCasing);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,2),"123","456","789",'2',mod_RetroStorage.chipDigitizer,'4',mod_RetroStorage.blankDisc,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.blankDisc,'8',mod_RetroStorage.cable);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.digitalChest,1,0),"123","456","789",'2',mod_RetroStorage.blankDisc,'4',mod_RetroStorage.chipRematerializer,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.chipDematerializer,'8',Block.chest);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.storageContainer,1,0),"123","456","789",'1',mod_RetroStorage.machineCasing,'2',mod_RetroStorage.machineCasing,'3',mod_RetroStorage.machineCasing,'4',Item.ingotIron,'5',Block.chest,'6',Item.ingotIron,'7',mod_RetroStorage.machineCasing,'8',mod_RetroStorage.machineCasing,'9',mod_RetroStorage.machineCasing);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,1),"123","456","789",'2',mod_RetroStorage.chipDigitizer,'4',mod_RetroStorage.chipRematerializer,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.chipDematerializer,'7',mod_RetroStorage.cable,'8',Block.chest,'9',mod_RetroStorage.cable);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,3),"123","456","789",'1',mod_RetroStorage.machineCasing,'2',mod_RetroStorage.chipCrafting,'3',mod_RetroStorage.machineCasing,'4',mod_RetroStorage.chipCrafting,'5',recipeDisc,'6',mod_RetroStorage.chipCrafting,'7',mod_RetroStorage.machineCasing,'8',mod_RetroStorage.chipCrafting,'9',mod_RetroStorage.machineCasing);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,4),"123","456","789",'2',mod_RetroStorage.chipRematerializer,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.cable,'8',mod_RetroStorage.chipDigitizer);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,5),"123","456","789",'2',mod_RetroStorage.chipDematerializer,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.cable,'8',mod_RetroStorage.chipDigitizer);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,7),"123","456","789",'2',new ItemStack(mod_RetroStorage.multiID,1,5),'3',mod_RetroStorage.chipDigitizer,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.cable,'7',mod_RetroStorage.chipCrafting,'8',new ItemStack(mod_RetroStorage.multiID,1,4));
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,6),"123","456","789",'2',mod_RetroStorage.machineCasing,'4',mod_RetroStorage.chipCrafting,'5',new ItemStack(mod_RetroStorage.multiID,1,1),'6',mod_RetroStorage.chipCrafting,'8',mod_RetroStorage.cable);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,8),"123","456","789",'2',mod_RetroStorage.machineCasing,'4',recipeDisc,'5',Block.workbench,'6',recipeDisc,'8',mod_RetroStorage.chipCrafting);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,10),"123","456","789",'1',Block.workbench,'2',advRecipeDisc,'3',Block.workbench,'4',mod_RetroStorage.chipCrafting,'5',new ItemStack(mod_RetroStorage.multiID,1,8),'6',mod_RetroStorage.chipCrafting,'7',Block.workbench,'8',mod_RetroStorage.advNachineCasing,'9',Block.workbench);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,9),"123","456","789",'1',Block.obsidian,'2',advRecipeDisc,'3',Block.obsidian,'4',mod_RetroStorage.chipCrafting,'5',new ItemStack(mod_RetroStorage.multiID,1,7),'6',mod_RetroStorage.chipDigitizer,'7',Block.obsidian,'8',mod_RetroStorage.advNachineCasing,'9',Block.obsidian);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,11),"123","456","789",'2',mod_RetroStorage.chipWireless,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.machineCasing,'6',mod_RetroStorage.wirelessAntenna,'8',mod_RetroStorage.chipWireless);
        ModLoader.AddRecipe(new ItemStack(mod_RetroStorage.multiID,1,12),"123","456","789",'1',mod_RetroStorage.machineCasing,'2',Block.torchRedstoneActive,'3',mod_RetroStorage.machineCasing,'4',mod_RetroStorage.cable,'5',mod_RetroStorage.redstoneCore,'6',mod_RetroStorage.chipDigitizer,'7',mod_RetroStorage.machineCasing,'8',Item.redstoneRepeater,'9',mod_RetroStorage.machineCasing);

        ModLoader.AddRecipe(new ItemStack(mobileTerminal,1,0),"123","456","789",'1',Item.ingotIron,'2',mod_RetroStorage.wirelessAntenna,'3',Item.ingotIron,'4',Block.glass,'5',new ItemStack(mod_RetroStorage.multiID,1,1),'6',Block.glass,'7',Item.ingotIron,'8',mod_RetroStorage.chipWireless,'9',Item.ingotIron);
        ModLoader.AddRecipe(new ItemStack(mobileRequestTerminal,1,0),"123","456","789",'1',Item.ingotIron,'2',mod_RetroStorage.wirelessAntenna,'3',Item.ingotIron,'4',Block.glass,'5',new ItemStack(mod_RetroStorage.multiID,1,6),'6',Block.glass,'7',Item.ingotIron,'8',mod_RetroStorage.chipWireless,'9',Item.ingotIron);

        ModLoader.AddRecipe(new ItemStack(linkingCard,1,0),"123","456","789",'1',new ItemStack(Item.dyePowder,1,12),'2',new ItemStack(Item.dyePowder,1,4),'3',new ItemStack(Item.dyePowder,1,12),'4',new ItemStack(Item.dyePowder,1,4),'5',mod_RetroStorage.blankCard,'6',new ItemStack(Item.dyePowder,1,4),'7',new ItemStack(Item.dyePowder,1,12),'8',mod_RetroStorage.chipWireless,'9',new ItemStack(Item.dyePowder,1,12));
        //these were autogenerated

        ModLoader.AddSmelting(Block.glass.blockID,new ItemStack(silicon,1));
        ModLoader.AddSmelting(ceramicPlateUnfired.shiftedIndex,new ItemStack(ceramicPlate,1));

        cableRenderID = ModLoader.getUniqueBlockModelID(this, false);
        System.out.println("[RetroStorage] IC2 Installed: " + IC2Available());
    }

    public static int getId(String s, int base) {
        Integer id = Config.getFromConfig(s, base);
        System.out.println("Getting id for " + s + ": " + (id == null ? "null (using default: " + base + ")" : id.toString()));
        return id == null ? base : Config.getFromConfig(s, base);
    }


    public static Item blankDisc = (new Item(getId("blankDisc", 104))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "blankdisc.png")).setItemName("blankdisc");
    public static ItemStorageDisc storageDisc1 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc1", 137), 64)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc1.png")).setItemName("storagedisc1").setMaxStackSize(1);
    public static ItemStorageDisc storageDisc2 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc2", 138), 128)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc2.png")).setItemName("storagedisc2").setMaxStackSize(1);
    public static ItemStorageDisc storageDisc3 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc3", 139), 192)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc3.png")).setItemName("storagedisc3").setMaxStackSize(1);
    public static ItemStorageDisc storageDisc4 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc4", 140), 256)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc4.png")).setItemName("storagedisc4").setMaxStackSize(1);
    public static ItemStorageDisc storageDisc5 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc5", 141), 320)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc5.png")).setItemName("storagedisc5").setMaxStackSize(1);
    public static ItemStorageDisc storageDisc6 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc6", 142), 384)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "disc6.png")).setItemName("storagedisc6").setMaxStackSize(1);
    public static ItemStorageDisc virtualDisc = (ItemStorageDisc) (new ItemStorageDisc(getId("virtualDisc", 143), (Short.MAX_VALUE * 2) + 1)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "virtualdisc.png")).setItemName("virtualdisc").setMaxStackSize(1);
    public static ItemStorageDisc goldenDisc = (ItemStorageDisc) (new ItemStorageDisc(getId("goldenDisc", 144), 1024)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "goldendisc.png")).setItemName("goldendisc").setMaxStackSize(1);
    public static ItemRecipeDisc recipeDisc = (ItemRecipeDisc) (new ItemRecipeDisc(getId("recipeDisc", 145))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "recipedisc.png")).setItemName("recipedisc");
    public static ItemAdvRecipeDisc advRecipeDisc = (ItemAdvRecipeDisc) (new ItemAdvRecipeDisc(getId("advRecipeDisc", 153))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "advrecipedisc.png")).setItemName("advrecipedisc");
    public static ItemTerminal mobileTerminal = (ItemTerminal) (new ItemTerminal(getId("mobileTerminal", 147))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "mobileterminal.png")).setItemName("mobileterminal").setMaxStackSize(1);
    public static ItemRequestTerminal mobileRequestTerminal = (ItemRequestTerminal) (new ItemRequestTerminal(getId("mobileRequestTerminal", 148))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "mobilerequestterminal.png")).setItemName("mobilerequestterminal").setMaxStackSize(1);
    public static Item blankCard = (new Item(getId("blankCard", 149))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "blankcard.png")).setItemName("blankcard");
    public static Item boosterCard = (new Item(getId("boosterCard", 150))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "boostercard.png")).setItemName("boostercard");
    public static Item lockingCard = (new Item(getId("lockingCard", 151))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "lockingcard.png")).setItemName("lockingcard");
    public static ItemLinkingCard linkingCard = (ItemLinkingCard) (new ItemLinkingCard(getId("linkingCard", 152))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "linkingcard.png")).setItemName("linkingcard");

    public static Block digitalChest = (new BlockDigitalChest(getId("digitalChest", 165), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("digitalChest"));
    public static Block cable = (new BlockCable(getId("cable", 167)).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("cable"));
    public static Block storageContainer = (new BlockStorageContainer(getId("container", 172), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("container"));
    public static Block multiID = (new BlockDigitalMachine(getId("multiID", 173)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("multiID"));

    //public static Item itemCable = (new ItemReed(getId("itemCable", 146), cable).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "itemcable.png")).setItemName("itemCable"));

    public static Item machineCasing = addSimpleItem("machineCasing","machinecasing",154);
    public static Item advNachineCasing = addSimpleItem("advMachineCasing","advmachinecasing",155);
    public static Item energyCore = addSimpleItem("energyCore","energycore",156);
    public static Item chipShell = addSimpleItem("chipShell","chipshell",157);
    public static Item chipShellFilled = addSimpleItem("chipShellFilled","filledchipshell",158);
    public static Item chipDigitizer = addSimpleItem("chipDigitizer","digitizerchip",159);
    public static Item chipCrafting = addSimpleItem("chipCrafting","craftingprocessor",160);
    public static Item chipDematerializer = addSimpleItem("chupDematerializer","dematerializerchip",161);
    public static Item chipRematerializer = addSimpleItem("chipRematerializer","rematerializerchip",162);
    public static Item chipDieDigitizer = addSimpleItem("chipDieDigitizer","digitizerdie",163);
    public static Item chipDieCrafting = addSimpleItem("chipDieCraftng","craftingdie",164);
    public static Item chipDieRematerializer = addSimpleItem("chipDieRematerializer","rematerializerdie",165);
    public static Item chipDieDematerializer = addSimpleItem("chipDieDematerializer","dematerializerdie",166);
    public static Item silicon = addSimpleItem("silicon","silicon",167);
    public static Item siliconWafer = addSimpleItem("siliconWafer","siliconwafer",168);
    public static Item ceramicPlate = addSimpleItem("ceramicPlate","ceramicplate",169);
    public static Item ceramicPlateUnfired = addSimpleItem("ceramicPlateUnfired","ceramicplateunfired",170);
    public static Item chipDieWireless = addSimpleItem("chipDieWireless","wirelessnetworkingdie",171);
    public static Item chipWireless = addSimpleItem("chipWireless","wirelessnetworkingchip",172);
    public static Item wirelessAntenna = addSimpleItem("wirelessAntenna","wirelessantenna",173);
    public static Item redstoneCore = addSimpleItem("redstoneCore","redstonecore",174);

    public static int digitalControllerTex;
    public static int digitalChestFront;
    public static int digitalChestSide;
    public static int digitalChestTop;
    public static int digitalChestTop2;
    public static int discDriveFront;
    public static int cableTex;
    public static int relayOnTex;
    public static int relayOffTex;
    public static int assemblerSide;
    public static int recipeEncoderTop;
    public static int recipeEncoderFront;
    public static int virtualDiscTop;
    public static int interfaceSide;
    public static int requestTerminalFront;
    public static int storageContainerFront;
    public static int importerTex;
    public static int exporterTex;
    public static int advInterfaceSide;
    public static int processProgrammerFront;
    public static int processProgrammerTop;
    public static int advMachineSide;
    public static int wirelessLinkTex;
    public static int emitterActive;
    public static int emitterInactive;
    public static int cableRenderID;

    public static int[][] sprites;

    public enum machines {
        digitalController, digitalTerminal, discDrive, assembler, importer, exporter, requestTerminal, digitalInterface,
        recipeEncoder, advInterface, processProgrammer, wirelessLink, emitter
    }


    public void GenerateSurface(World world, Random random, int i, int j) {
    }

    public boolean RenderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l) {
        return block.getRenderType() == cableRenderID && RenderCable.render(renderblocks, iblockaccess, i, j, k, block, l);
    }


    public static boolean IC2Available() {
        return ((ModLoader.isModLoaded("mod_IC2") || ModLoader.isModLoaded("net.minecraft.src.mod_IC2")));
    }


    public static Item addSimpleItem(String internalName, String textureFile, int id){
        return (new Item(getId(internalName, id))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + textureFile + ".png")).setItemName(internalName);
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
            map = (HashMap<Object, Object>) ModLoader.getPrivateValue(NBTTagCompound.class, tag, 0);
            if (map != null) {
                int finalN = n;
                map.forEach((K, V) -> {
                    System.out.println(str.toString() + K + ": " + V);
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

    @Override
    public String Version() {
        return "b1.1.1";
    }

    public String Name() {
        return "Retro Storage";
    }

    public String Description() {
        return "A digital storage solution for b1.7.3!";
    }

    public String Icon() {
        return "/retrostorage/" + "virtualdisc.png";
    }

}
