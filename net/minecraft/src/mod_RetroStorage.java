package net.minecraft.src;

import net.sunsetsatellite.retrostorage.*;

import java.util.Random;


public class mod_RetroStorage extends BaseMod {

	public static Config config;

	public mod_RetroStorage() {
		Config.init();

		digitalChestFront = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("digitalchestfront.png").toString());
		digitalChestSide = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("digitalchestside.png").toString());
		digitalChestTop = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("digitalchesttop.png").toString());
		digitalChestTop2 = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("digitalchesttopfilled.png").toString());
		discDriveFront = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("discdrive.png").toString());
		cableTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("blockcable.png").toString());
		relayOnTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("relayon.png").toString());
		relayOffTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("relayoff.png").toString());
		assemblerSide = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("assemblerside.png").toString());
		recipeEncoderTop = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("recipeencodertopfilled.png").toString());
		recipeEncoderFront = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("recipeencoderfront.png").toString());
		virtualDiscTop = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("virtualdisctop.png").toString());
		interfaceSide = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("interfaceside.png").toString());
		requestTerminalFront = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("requestterminalfront.png").toString());
		storageContainerFront = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storagecontainer.png").toString());
		digitalControllerTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("digitalcontroller.png").toString());
		importerTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("importer.png").toString());
		exporterTex = ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("exporter.png").toString());

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
				}

		};

		BlockDigitalChest.loadSprites();
		/*BlockDiscDrive.loadSprites();
		BlockDigitalTerminal.loadSprites();*/
		BlockCable.loadSprites();
		/*BlockRelay.loadSprites();
		BlockRelayOff.loadSprites();
		BlockRecipeEncoder.loadSprites();
		BlockAssembler.loadSprites();
		BlockRequestTerminal.loadSprites();
		BlockInterface.loadSprites();*/
		BlockStorageContainer.loadSprites();

		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalChest.class, "Digital Chest");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalController.class, "Digital Controller");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDiscDrive.class, "Disc Drive");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityDigitalTerminal.class, "Digital Terminal");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityImporter.class, "Importer");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityExporter.class, "Exporter");
		//ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityStorageBlock.class, "Storage Block");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityRecipeEncoder.class, "Recipe Encoder");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityAssembler.class, "Assembler");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityRequestTerminal.class, "Request Terminal");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityInterface.class, "Interface");
		ModLoader.RegisterTileEntity(net.sunsetsatellite.retrostorage.TileEntityStorageContainer.class,"Storage Container",new TileEntityContainerRenderer());
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
		//ModLoader.AddName(itemCable, "Cable");
		ModLoader.AddLocalization("tile.discDrive.name", "Disc Drive");
		ModLoader.AddLocalization("tile.digitalTerminal.name", "Digital Terminal");
		ModLoader.AddName(virtualDisc, "Virtual Disc");
		ModLoader.AddLocalization("tile.importer.name", "Item Importer");
		ModLoader.AddLocalization("tile.exporter.name", "Item Exporter");
		ModLoader.AddName(goldenDisc, "Golden Disc");
		//ModLoader.AddName(goldenStorageBlock, "Golden Storage Block");
		/*ModLoader.AddName(storageBlock1, "Storage Block MK I");
		ModLoader.AddName(storageBlock2, "Storage Block MK II");
		ModLoader.AddName(storageBlock3, "Storage Block MK III");
		ModLoader.AddName(storageBlock4, "Storage Block MK IV");
		ModLoader.AddName(storageBlock5, "Storage Block MK V");
		ModLoader.AddName(storageBlock6, "Storage Block MK VI");*/
		/*ModLoader.AddName(relay, "Relay");
		ModLoader.AddName(relayOff, "Relay");*/
		//ModLoader.AddName(testingBlock, "Testing Block");
		ModLoader.AddLocalization("tile.recipeEncoder.name", "Recipe Encoder");
		ModLoader.AddLocalization("tile.assembler.name", "Assembler");
		ModLoader.AddLocalization("tile.requestTerminal.name", "Request Terminal");
		ModLoader.AddName(itemCable,"Network Cable");
		ModLoader.AddLocalization("tile.interface.name","Item Interface");
		ModLoader.AddName(mobileTerminal,"Mobile Terminal");
		ModLoader.AddName(mobileRequestTerminal,"Mobile Request Terminal");
		ModLoader.AddName(blankCard,"Blank Card");
		ModLoader.AddName(boosterCard,"Booster Card");
		ModLoader.AddName(lockingCard,"Locking Card");
		ModLoader.AddName(storageContainer,"Storage Container");

		ModLoader.RegisterBlock(digitalChest);
		//ModLoader.RegisterBlock(digitalController);
		ModLoader.RegisterBlock(cable);
		/*ModLoader.RegisterBlock(discDrive);
		ModLoader.RegisterBlock(digitalTerminal);
		ModLoader.RegisterBlock(importer);
		ModLoader.RegisterBlock(exporter);*/
		ModLoader.RegisterBlock(storageContainer,ItemBlockStorageContainer.class);
		ModLoader.RegisterBlock(multiID, ItemDigitalMachineBlock.class);
		//ModLoader.RegisterBlock(goldenStorageBlock);
		/*ModLoader.RegisterBlock(storageBlock1);
		ModLoader.RegisterBlock(storageBlock2);
		ModLoader.RegisterBlock(storageBlock3);
		ModLoader.RegisterBlock(storageBlock4);
		ModLoader.RegisterBlock(storageBlock5);
		ModLoader.RegisterBlock(storageBlock6);*/
		/*ModLoader.RegisterBlock(relay);
		ModLoader.RegisterBlock(relayOff);*/
		//ModLoader.RegisterBlock(testingBlock);
		/*ModLoader.RegisterBlock(recipeEncoder);
		ModLoader.RegisterBlock(assembler);
		ModLoader.RegisterBlock(requestTerminal);
		ModLoader.RegisterBlock(digitalInterface);*/


		ModLoader.AddRecipe(new ItemStack(digitalChest, 1), "ISI", "RCR", "IDI", 'S', blankDisc, 'I', Item.ingotIron, 'R', Item.redstone, 'C', Block.chest, 'D', Block.blockDiamond);
		ModLoader.AddRecipe(new ItemStack(blankDisc, 1), "GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.redstone);
		ModLoader.AddRecipe(new ItemStack(recipeDisc, 1), "GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.redstone, 'P', new ItemStack(Item.dyePowder,1,5));
		ModLoader.AddRecipe(new ItemStack(storageDisc1, 1), "RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.redstone);
		ModLoader.AddRecipe(new ItemStack(itemCable,16), "WLW","GGG","WLW", 'W', Block.cloth, 'G', Block.glass, 'L', new ItemStack(Item.dyePowder,1,4));
		//ModLoader.AddRecipe(new ItemStack(relay,1),"ILI","CRC","ILI",'I',Item.ingotIron,'L',new ItemStack(Item.dyePowder,1,4),'C',itemCable,'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(multiID,1, machines.digitalController.ordinal()),"ILI","LDL","ILI",'I',Block.blockSteel,'L',Block.blockLapis,'D',Block.blockDiamond);
		ModLoader.AddRecipe(new ItemStack(multiID,1,machines.discDrive.ordinal()),"III","DDD","III",'I',Item.ingotIron,'D',blankDisc);
		ModLoader.AddRecipe(new ItemStack(multiID, 1,machines.digitalTerminal.ordinal()), "III", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable);
		ModLoader.AddRecipe(new ItemStack(multiID, 1,machines.importer.ordinal()), "ILI", "GHG", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis,'G',new ItemStack(Item.dyePowder,1,10) ,'H', Block.chest, 'C', itemCable);
		ModLoader.AddRecipe(new ItemStack(multiID, 1,machines.exporter.ordinal()), "ILI", "RHR", "ICI", 'I', Item.ingotIron, 'L', Block.blockLapis,'R',new ItemStack(Item.dyePowder,1,1) ,'H', Block.chest, 'C', itemCable);
		ModLoader.AddRecipe(new ItemStack(multiID, 1,machines.digitalInterface.ordinal()), "IRI", "PHP", "ICI", 'I', Item.ingotIron, 'P', new ItemStack(Item.dyePowder,1,5),'R',recipeDisc ,'H', Block.chest, 'C', itemCable);
		ModLoader.AddRecipe(new ItemStack(multiID,1,machines.assembler.ordinal()),"ICI","CRC","ICI",'I',Block.blockSteel,'C',Block.workbench,'R',recipeDisc);
		ModLoader.AddRecipe(new ItemStack(multiID,1,machines.recipeEncoder.ordinal()),"IRI","PCP","IcI",'I',Item.ingotIron,'C',Block.workbench,'R',recipeDisc,'P', new ItemStack(Item.dyePowder,1,5),'c',itemCable);
		ModLoader.AddRecipe(new ItemStack(multiID, 1,6), "ITI", "RHR", "ICI", 'I', Item.ingotIron, 'R', Item.redstone, 'H', Block.chest, 'C', itemCable,'T',Block.workbench);
		ModLoader.AddRecipe(new ItemStack(storageDisc2, 1), "RgG", "X#X", "GgR", 'G', Block.glass,'g',Item.ingotGold,'X',storageDisc1,'#',new ItemStack(Item.dyePowder,1,14),'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(storageDisc3, 1), "RgG", "X#X", "GgR", 'G', Block.glass,'g',Item.ingotGold,'X',storageDisc2,'#',new ItemStack(Item.dyePowder,1,11),'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(storageDisc4, 1), "RgG", "X#X", "GgR", 'G', Block.glass,'g',Item.ingotGold,'X',storageDisc3,'#',new ItemStack(Item.dyePowder,1,10),'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(storageDisc5, 1), "RgG", "X#X", "GgR", 'G', Block.glass,'g',Item.ingotGold,'X',storageDisc4,'#',new ItemStack(Item.dyePowder,1,4),'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(storageDisc6, 1), "RgG", "X#X", "GgR", 'G', Block.glass,'g',Item.ingotGold,'X',storageDisc5,'#',new ItemStack(Item.dyePowder,1,5),'R',Item.redstone);
		ModLoader.AddRecipe(new ItemStack(mobileTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass,'I',Item.ingotIron,'D',Item.diamond,'T',new ItemStack(multiID,1,1));
		ModLoader.AddRecipe(new ItemStack(mobileRequestTerminal, 1), "IGI", "ITI", "IDI", 'G', Block.glass,'I',Item.ingotIron,'D',Item.diamond,'T',new ItemStack(multiID,1,6));
		ModLoader.AddRecipe(new ItemStack(storageContainer,1),"SSS","ICI","SSS", 'S', Block.stone, 'I', Item.ingotIron, 'C', Block.chest);
		ModLoader.AddRecipe(new ItemStack(blankCard, 4),"ISI","SPS","ISI",'I',Item.ingotIron,'S',Block.stone,'P',Block.pressurePlateStone);
		ModLoader.AddRecipe(new ItemStack(lockingCard, 1),"RTR","TCT","RTR",'R',Item.redstone,'T',Block.torchRedstoneActive,'C',blankCard);
		System.out.println("[RetroStorage] IC2 Installed: "+IC2Available());
	}

	public static int getId(String s, int base) {
		Integer id = Config.getFromConfig(s,base);
		System.out.println("Getting id for "+s+": "+ (id == null ? "null (using default: "+base+")" : id.toString()));
		return id == null ? base : Config.getFromConfig(s,base);
	}


	public static Item blankDisc = (new Item(getId("blankDisc",104))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("blankdisc.png").toString())).setItemName("blankdisc");
	public static ItemStorageDisc storageDisc1 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc1",137),64)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc1.png").toString())).setItemName("storagedisc1").setMaxStackSize(1);
	public static ItemStorageDisc storageDisc2 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc2",138),128)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc2.png").toString())).setItemName("storagedisc2").setMaxStackSize(1);
	public static ItemStorageDisc storageDisc3 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc3",139),192)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc3.png").toString())).setItemName("storagedisc3").setMaxStackSize(1);
	public static ItemStorageDisc storageDisc4 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc4",140),256)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc4.png").toString())).setItemName("storagedisc4").setMaxStackSize(1);
	public static ItemStorageDisc storageDisc5 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc5",141),320)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc5.png").toString())).setItemName("storagedisc5").setMaxStackSize(1);
	public static ItemStorageDisc storageDisc6 = (ItemStorageDisc) (new ItemStorageDisc(getId("storageDisc6",142),384)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("disc6.png").toString())).setItemName("storagedisc6").setMaxStackSize(1);
	public static ItemStorageDisc virtualDisc = (ItemStorageDisc) (new ItemStorageDisc(getId("virtualDisc",143), (Short.MAX_VALUE*2)+1)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("virtualdisc.png").toString())).setItemName("virtualdisc").setMaxStackSize(1);
	public static ItemStorageDisc goldenDisc = (ItemStorageDisc) (new ItemStorageDisc(getId("goldenDisc",144), 1024)).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("goldendisc.png").toString())).setItemName("goldendisc").setMaxStackSize(1);
	public static ItemRecipeDisc recipeDisc = (ItemRecipeDisc) (new ItemRecipeDisc(getId("recipeDisc",145))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("recipedisc.png").toString())).setItemName("recipedisc");
	public static ItemTerminal mobileTerminal = (ItemTerminal) (new ItemTerminal(getId("mobileTerminal",147))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("mobileterminal.png").toString())).setItemName("mobileterminal").setMaxStackSize(1);
	public static ItemRequestTerminal mobileRequestTerminal = (ItemRequestTerminal) (new ItemRequestTerminal(getId("mobileRequestTerminal",148))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("mobilerequestterminal.png").toString())).setItemName("mobilerequestterminal").setMaxStackSize(1);
	public static Item blankCard = (new Item(getId("blankCard",149))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("blankcard.png").toString())).setItemName("blankcard");
	public static Item boosterCard = (new Item(getId("boosterCard",150))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("boostercard.png").toString())).setItemName("boostercard");
	public static Item lockingCard = (new Item(getId("lockingCard",151))).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("lockingcard.png").toString())).setItemName("lockingcard");

	public static Block digitalChest = (new BlockDigitalChest(getId("digitalChest",165), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("digitalChest"));
	//public static Block digitalController = (new BlockDigitalController(getId("digitalController",166),mod_RetroStorage.digitalControllerTex).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("digitalController"));
	public static Block cable = (new BlockCable(getId("cable",167)).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("cable"));
	/*public static Block discDrive = (new BlockDiscDrive(getId("discDrive",168)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("discDrive"));
	public static Block digitalTerminal = (new BlockDigitalTerminal(getId("digitalTerminal",169), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("digitalTerminal"));
	public static Block importer = (new BlockImporter(getId("importer",170), mod_RetroStorage.importerTex, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("importer"));
	public static Block exporter = (new BlockExporter(getId("exporter",171), mod_RetroStorage.exporterTex, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("exporter"));*/
	public static Block storageContainer = (new BlockStorageContainer(getId("container",172),false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("container"));
	public static Block multiID = (new BlockDigitalMachine(getId("multiID",173)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("multiID"));
	//public static Block goldenStorageBlock = (new BlockStorage(getId("goldenStorageBlock",172), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("goldenstorageblock.png").toString()),mod_RetroStorage.goldenDisc, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("goldenStorageBlock"));
	/*public static Block storageBlock1 = (new BlockStorage(getId("storageBlock1",173), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock1.png").toString()),mod_RetroStorage.storageDisc1, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock1"));
	public static Block storageBlock2 = (new BlockStorage(getId("storageBlock2",139), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock2.png").toString()),mod_RetroStorage.storageDisc2, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock2"));
	public static Block storageBlock3 = (new BlockStorage(getId("storageBlock3",176), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock3.png").toString()),mod_RetroStorage.storageDisc3, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock3"));
	public static Block storageBlock4 = (new BlockStorage(getId("storageBlock4",177), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock4.png").toString()),mod_RetroStorage.storageDisc4, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock4"));
	public static Block storageBlock5 = (new BlockStorage(getId("storageBlock5",178), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock5.png").toString()),mod_RetroStorage.storageDisc5, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock5"));
	public static Block storageBlock6 = (new BlockStorage(getId("storageBlock6",179), ModLoader.addOverride("/terrain.png", (new StringBuilder()).append("/retrostorage/").append("storageblock6.png").toString()),mod_RetroStorage.storageDisc6, Material.rock).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("storageBlock6"));*/

	/*public static Block relay = (new BlockRelay(getId("relay",180)).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("relay"));
	public static Block relayOff = (new BlockRelayOff(getId("relayOff",181)).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("relayOff"));*/
	//public static Block testingBlock = (new BlockTesting(getId("testingBlock",182), Material.rock).setHardness(0.2F).setResistance(1F).setStepSound(Block.soundClothFootstep).setBlockName("testingBlock"));
	/*public static Block recipeEncoder = (new BlockRecipeEncoder(getId("recipeEncoder",183)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("recipeEncoder"));
	public static Block assembler = (new BlockAssembler(getId("assembler",184)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("assembler"));
	public static Block requestTerminal = (new BlockRequestTerminal(getId("requestTerminal",185), false).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("requestTerminal"));
	public static Block digitalInterface = (new BlockInterface(getId("digitalInterface",186)).setHardness(1F).setResistance(5F).setStepSound(Block.soundStoneFootstep).setBlockName("interface"));*/

	public static Item itemCable = (new ItemReed(getId("itemCable",146),cable).setIconIndex(ModLoader.addOverride("/gui/items.png", (new StringBuilder()).append("/retrostorage/").append("itemcable.png").toString())).setItemName("itemCable"));

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

	public static int[][] sprites;

	public enum machines {
		digitalController,digitalTerminal,discDrive,assembler,importer,exporter,requestTerminal,digitalInterface,
		recipeEncoder
	}

	
	public void GenerateSurface(World world, Random random, int i, int j){
    }


	public static boolean IC2Available(){
		return ((ModLoader.isModLoaded("mod_IC2")||ModLoader.isModLoaded("net.minecraft.src.mod_IC2")));
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
		return (new StringBuilder()).append("/retrostorage/").append("virtualdisc.png").toString();
	}

}
