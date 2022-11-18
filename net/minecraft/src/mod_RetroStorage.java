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
        ModLoader.AddName(itemCable, "Network Cable");
        ModLoader.AddLocalization("tile.interface.name", "Item Interface");
        ModLoader.AddLocalization("tile.advInterface.name", "Adv. Item Interface");
        ModLoader.AddName(mobileTerminal, "Mobile Terminal");
        ModLoader.AddName(mobileRequestTerminal, "Mobile Request Terminal");
        ModLoader.AddName(blankCard, "Blank Card");
        ModLoader.AddName(boosterCard, "Booster Card");
        ModLoader.AddName(lockingCard, "Locking Card");
        ModLoader.AddName(storageContainer, "Storage Container");
        ModLoader.AddName(advRecipeDisc, "Adv. Recipe Disc");
        ModLoader.AddLocalization("tile.processProgrammer.name", "Process Programmer");

        ModLoader.RegisterBlock(digitalChest);
        ModLoader.RegisterBlock(cable);
        ModLoader.RegisterBlock(storageContainer, ItemBlockStorageContainer.class);
        ModLoader.RegisterBlock(multiID, ItemDigitalMachineBlock.class);


        ModLoader.AddRecipe(new ItemStack(digitalChest, 1), "ISI", "RCR", "IDI", 'S', blankDisc, 'I', Item.ingotIron, 'R', Item.redstone, 'C', Block.chest, 'D', Block.blockDiamond);
        ModLoader.AddRecipe(new ItemStack(blankDisc, 1), "GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(recipeDisc, 1), "GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.redstone, 'P', new ItemStack(Item.dyePowder, 1, 5));
        ModLoader.AddRecipe(new ItemStack(storageDisc1, 1), "RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(itemCable, 16), "WLW", "GGG", "WLW", 'W', Block.cloth, 'G', Block.glass, 'L', new ItemStack(Item.dyePowder, 1, 4));
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalController.ordinal()), "ILI", "LDL", "ILI", 'I', Block.blockSteel, 'L', Block.blockLapis, 'D', Block.blockDiamond);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.discDrive.ordinal()), "III", "DDD", "III", 'I', Item.ingotIron, 'D', blankDisc);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalTerminal.ordinal()), "III", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.importer.ordinal()), "ILI", "GHG", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis, 'G', new ItemStack(Item.dyePowder, 1, 10), 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.exporter.ordinal()), "ILI", "RHR", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis, 'R', new ItemStack(Item.dyePowder, 1, 1), 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.digitalInterface.ordinal()), "IRI", "PHP", "ICI", 'I', Item.ingotIron, 'P', new ItemStack(Item.dyePowder, 1, 5), 'R', recipeDisc, 'H', Block.chest, 'C', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.assembler.ordinal()), "ICI", "CRC", "ICI", 'I', Block.blockSteel, 'C', Block.workbench, 'R', recipeDisc);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.recipeEncoder.ordinal()), "IRI", "PCP", "IcI", 'I', Item.ingotIron, 'C', Block.workbench, 'R', recipeDisc, 'P', new ItemStack(Item.dyePowder, 1, 5), 'c', itemCable);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, 6), "ITI", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable, 'T', Block.workbench);
        ModLoader.AddRecipe(new ItemStack(storageDisc2, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc1, '#', new ItemStack(Item.dyePowder, 1, 14), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc3, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc2, '#', new ItemStack(Item.dyePowder, 1, 11), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc4, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc3, '#', new ItemStack(Item.dyePowder, 1, 10), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc5, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc4, '#', new ItemStack(Item.dyePowder, 1, 4), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(storageDisc6, 1), "RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc5, '#', new ItemStack(Item.dyePowder, 1, 5), 'R', Item.redstone);
        ModLoader.AddRecipe(new ItemStack(mobileTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass, 'I', Item.ingotIron, 'D', Item.diamond, 'T', new ItemStack(multiID, 1, 1));
        ModLoader.AddRecipe(new ItemStack(mobileRequestTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass, 'I', Item.ingotIron, 'D', Item.diamond, 'T', new ItemStack(multiID, 1, 6));
        ModLoader.AddRecipe(new ItemStack(storageContainer, 1), "SSS", "ICI", "SSS", 'S', Block.stone, 'I', Item.ingotIron, 'C', Block.chest);
        ModLoader.AddRecipe(new ItemStack(blankCard, 4), "ISI", "SPS", "ISI", 'I', Item.ingotIron, 'S', Block.stone, 'P', Block.pressurePlateStone);
        ModLoader.AddRecipe(new ItemStack(lockingCard, 1), "RTR", "TCT", "RTR", 'R', Item.redstone, 'T', Block.torchRedstoneActive, 'C', blankCard);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.advInterface.ordinal()), "ODO", "PIP", "OCO", 'O', Block.obsidian, 'D', advRecipeDisc, 'I', new ItemStack(multiID, 1, machines.digitalInterface.ordinal()), 'C', itemCable, 'P', new ItemStack(Item.dyePowder, 1, 5));
        ModLoader.AddRecipe(new ItemStack(advRecipeDisc, 1), "OPO", "RDR", "OPO", 'R', Item.redstone, 'D', recipeDisc, 'P', new ItemStack(Item.dyePowder, 1, 5), 'O', Block.obsidian);
        ModLoader.AddRecipe(new ItemStack(multiID, 1, machines.processProgrammer.ordinal()), "ODO", "CRC", "OIO", 'O', Block.obsidian, 'D', advRecipeDisc, 'C', Block.workbench, 'I', new ItemStack(multiID, 1, machines.advInterface.ordinal()), 'R', new ItemStack(multiID, 1, machines.recipeEncoder.ordinal()));

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
    public static ItemAdvRecipeDisc advRecipeDisc = (ItemAdvRecipeDisc) (new ItemAdvRecipeDisc(getId("advRecipeDisc", 152))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "advrecipedisc.png")).setItemName("advrecipedisc");
    public static ItemTerminal mobileTerminal = (ItemTerminal) (new ItemTerminal(getId("mobileTerminal", 147))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "mobileterminal.png")).setItemName("mobileterminal").setMaxStackSize(1);
    public static ItemRequestTerminal mobileRequestTerminal = (ItemRequestTerminal) (new ItemRequestTerminal(getId("mobileRequestTerminal", 148))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "mobilerequestterminal.png")).setItemName("mobilerequestterminal").setMaxStackSize(1);
    public static Item blankCard = (new Item(getId("blankCard", 149))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "blankcard.png")).setItemName("blankcard");
    public static Item boosterCard = (new Item(getId("boosterCard", 150))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "boostercard.png")).setItemName("boostercard");
    public static Item lockingCard = (new Item(getId("lockingCard", 151))).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "lockingcard.png")).setItemName("lockingcard");

    public static Block digitalChest = (new BlockDigitalChest(getId("digitalChest", 165), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("digitalChest"));
    public static Block cable = (new BlockCable(getId("cable", 167)).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("cable"));
    public static Block storageContainer = (new BlockStorageContainer(getId("container", 172), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("container"));
    public static Block multiID = (new BlockDigitalMachine(getId("multiID", 173)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("multiID"));

    public static Item itemCable = (new ItemReed(getId("itemCable", 146), cable).setIconIndex(ModLoader.addOverride("/gui/items.png", "/retrostorage/" + "itemcable.png")).setItemName("itemCable"));

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
    public static int cableRenderID;

    public static int[][] sprites;

    public enum machines {
        digitalController, digitalTerminal, discDrive, assembler, importer, exporter, requestTerminal, digitalInterface,
        recipeEncoder, advInterface, processProgrammer
    }


    public void GenerateSurface(World world, Random random, int i, int j) {
    }

    public boolean RenderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l) {
        return block.getRenderType() == cableRenderID && RenderCable.render(renderblocks, iblockaccess, i, j, k, block, l);
    }


    public static boolean IC2Available() {
        return ((ModLoader.isModLoaded("mod_IC2") || ModLoader.isModLoaded("net.minecraft.src.mod_IC2")));
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
        return "a1.5";
    }

    public String Name() {
        return "Retro Storage";
    }

    public String Description() {
        return "A digital storage solution for b1.7.3";
    }

    public String Icon() {
        return "/retrostorage/" + "virtualdisc.png";
    }

}
