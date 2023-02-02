package sunsetsatellite.retrostorage;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.*;
import turniplabs.halplibe.helper.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

//TODO: Finish auto-crafting system.
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

    public static Item machineCasing = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("machineCasing",309)),"machineCasing","machinecasing.png");
    public static Item advNachineCasing = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("advMachineCasing",310)),"advMachineCasing","advmachinecasing.png");
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
    
    
    public static final Block digitalChest = BlockHelper.createBlock(MOD_ID,new BlockDigitalChest(Config.getFromConfig("digitalChest",900), Material.rock),"digitalChest","digitalchesttopfilled.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalController = BlockHelper.createBlock(MOD_ID,new BlockDigitalController(Config.getFromConfig("digitalController",901), Material.rock),"digitalController","digitalcontroller.png",Block.soundStoneFootstep,2,5,1);
    public static final Block networkCable = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("networkCable",902), Material.rock),"networkCable","blockcable.png",Block.soundClothFootstep,1,1,0);
    public static final Block discDrive = BlockHelper.createBlock(MOD_ID,new BlockDiscDrive(Config.getFromConfig("discDrive",903), Material.rock),"discDrive","digitalchestside.png","digitalchestside.png","discdrive.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalTerminal = BlockHelper.createBlock(MOD_ID,new BlockDigitalTerminal(Config.getFromConfig("digitalTerminal",904), Material.rock),"digitalTerminal","digitalchestside.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block recipeEncoder = BlockHelper.createBlock(MOD_ID,new BlockRecipeEncoder(Config.getFromConfig("recipeEncoder",905),Material.rock),"recipeEncoder","recipeencodertopfilled.png","digitalchestside.png","recipeencoderfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block assembler = BlockHelper.createBlock(MOD_ID,new BlockAssembler(Config.getFromConfig("assembler",906),Material.rock),"assembler","recipeencodertopfilled.png","digitalchestside.png","assemblerside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block requestTerminal = BlockHelper.createBlock(MOD_ID,new BlockRequestTerminal(Config.getFromConfig("requestTerminal",907),Material.rock),"requestTerminal","digitalchestside.png","digitalchestside.png","requestterminalfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block importer = BlockHelper.createBlock(MOD_ID,new BlockImporter(Config.getFromConfig("importer",908),Material.rock),"importer","importer.png",Block.soundStoneFootstep,2,5,1);
    public static final Block exporter = BlockHelper.createBlock(MOD_ID,new BlockExporter(Config.getFromConfig("exporter",909),Material.rock),"exporter","exporter.png",Block.soundStoneFootstep,2,5,1);

    public static HashMap<String, Vec3> directions = new HashMap<>();

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
        //NBT -> Recipe -> Result test: PASSED!!
        /*NBTTagCompound recipe = new NBTTagCompound();
        NBTTagCompound stackNBT = new NBTTagCompound();
        new ItemStack(Block.cobbleStone, 1).writeToNBT(stackNBT);
        recipe.setCompoundTag("0", stackNBT);
        stackNBT = new NBTTagCompound();
        new ItemStack(Block.cobbleStone, 1).writeToNBT(stackNBT);
        recipe.setCompoundTag("1", stackNBT);
        ItemStack result = findRecipeFromNBT(recipe);
        if (result.itemID != 0) {
            RetroStorage.LOGGER.info("Result: " + result);
        } else {
            RetroStorage.LOGGER.info("Result: null");
        }*/

        RecipeHelper.Crafting.createRecipe(blankDisc, 1, new Object[]{"GGG", "GRG", "GGG", 'G', Block.glass, 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(recipeDisc, 1,  new Object[]{"GPG", "PRP", "GPG", 'G', Block.glass, 'R', Item.dustRedstone, 'P', new ItemStack(Item.dye, 1, 5)});
        RecipeHelper.Crafting.createRecipe(storageDisc1, 1, new Object[]{"RRR", "RDR", "RRR", 'D', blankDisc, 'R', Item.dustRedstone});

        RecipeHelper.Crafting.createRecipe(storageDisc2,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc1, '#', new ItemStack(Item.dye, 1, 14), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc3,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc2, '#', new ItemStack(Item.dye, 1, 11), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc4,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc3, '#', new ItemStack(Item.dye, 1, 10), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc5,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc4, '#', new ItemStack(Item.dye, 1, 4), 'R', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(storageDisc6,1,new Object[]{"RgG", "X#X", "GgR", 'G', Block.glass, 'g', Item.ingotGold, 'X', storageDisc5, '#', new ItemStack(Item.dye, 1, 5), 'R', Item.dustRedstone});

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
        LOGGER.info("RetroStorage: BTA Edition initialized.");
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

}
