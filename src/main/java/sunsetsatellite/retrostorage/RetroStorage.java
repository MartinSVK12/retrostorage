package sunsetsatellite.retrostorage;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.retrostorage.blocks.*;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.NBTEditCommand;
import sunsetsatellite.retrostorage.util.Vec3;
import turniplabs.halplibe.helper.BlockHelper;
import turniplabs.halplibe.helper.CommandHelper;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: Finish auto-crafting system.
public class RetroStorage implements ModInitializer {
    public static final String MOD_ID = "retrostorage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item blankDisc = ItemHelper.createItem(MOD_ID,new Item(300),"blankDisc","blankdisc.png");
    public static final Item storageDisc1 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(301,64),"storageDisc1","disc1.png").setMaxStackSize(1);
    public static final Item storageDisc2 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(302,128),"storageDisc2","disc2.png").setMaxStackSize(1);
    public static final Item storageDisc3 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(303,196),"storageDisc3","disc3.png").setMaxStackSize(1);
    public static final Item storageDisc4 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(304,256),"storageDisc4","disc4.png").setMaxStackSize(1);
    public static final Item storageDisc5 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(305,320),"storageDisc6","disc5.png").setMaxStackSize(1);
    public static final Item storageDisc6 = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(306,384),"storageDisc5","disc6.png").setMaxStackSize(1);
    public static final Item virtualDisc = ItemHelper.createItem(MOD_ID,new ItemStorageDisc(307,Short.MAX_VALUE*2),"virtualDisc","virtualdisc.png").setMaxStackSize(1).setNotInCreativeMenu();
    public static final Block digitalChest = BlockHelper.createBlock(MOD_ID,new BlockDigitalChest(900, Material.rock),"digitalChest","digitalchesttopfilled.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalController = BlockHelper.createBlock(MOD_ID,new BlockDigitalController(901, Material.rock),"digitalController","digitalcontroller.png",Block.soundStoneFootstep,2,5,1);
    public static final Block networkCable = BlockHelper.createBlock(MOD_ID,new Block(902, Material.rock),"networkCable","blockcable.png",Block.soundClothFootstep,1,1,0);
    public static final Block discDrive = BlockHelper.createBlock(MOD_ID,new BlockDiscDrive(903, Material.rock),"discDrive","digitalchestside.png","digitalchestside.png","discdrive.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block digitalTerminal = BlockHelper.createBlock(MOD_ID,new BlockDigitalTerminal(904, Material.rock),"digitalTerminal","digitalchestside.png","digitalchestside.png","digitalchestfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Item recipeDisc = ItemHelper.createItem(MOD_ID,new ItemRecipeDisc(308),"recipeDisc","recipedisc.png").setMaxStackSize(1);
    public static final Block recipeEncoder = BlockHelper.createBlock(MOD_ID,new BlockRecipeEncoder(905,Material.rock),"recipeEncoder","recipeencodertopfilled.png","digitalchestside.png","recipeencoderfront.png","digitalchestside.png","digitalchestside.png","digitalchestside.png",Block.soundStoneFootstep,2,5,1);
    public static final Block assembler = BlockHelper.createBlock(MOD_ID,new BlockAssembler(906,Material.rock),"assembler","recipeencodertopfilled.png","digitalchestside.png","assemblerside.png",Block.soundStoneFootstep,2,5,1);


    public static HashMap<String, Vec3> directions = new HashMap<>();

    public RetroStorage(){
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

        CommandHelper.createCommand(new NBTEditCommand());
        EntityHelper.createTileEntity(TileEntityDigitalChest.class, "Digital Chest");
        EntityHelper.createTileEntity(TileEntityDigitalTerminal.class, "Digital Terminal");
        EntityHelper.createTileEntity(TileEntityDigitalController.class, "Digital COntroller");
        EntityHelper.createTileEntity(TileEntityDiscDrive.class, "Disc Drive");
        EntityHelper.createTileEntity(TileEntityRecipeEncoder.class,"Recipe Encoder");
        EntityHelper.createTileEntity(TileEntityAssembler.class,"Assembler");
        LOGGER.info("RetroStorage: BTA Edition initialized.");
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

}
