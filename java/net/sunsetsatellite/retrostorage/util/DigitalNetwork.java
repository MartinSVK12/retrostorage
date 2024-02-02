package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;
import net.sunsetsatellite.retrostorage.tiles.*;
import net.sunsetsatellite.retrostorage.util.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for a digital storage network.
 */
public class DigitalNetwork extends Network {
    /**
     * Creates a new digital network with <i>controller</i> as its controller
     *
     * @param controller  Controller of the network
     */
    public InventoryDigital inventory;
    public ArrayDeque<Task> requestQueue = new ArrayDeque<>();
    public TileEntityDiscDrive drive;

    public DigitalNetwork(TileEntityDigitalController controller) {
        super(controller, TileEntityNetworkDevice.class, new int[]{mod_RetroStorage.networkCable.blockID});
        this.inventory = new InventoryDigital("Digital Network",controller);
    }

    @Override
    public void add(BlockInstance device) {
        super.add(device);
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = this;
        }
        if(device.tile instanceof TileEntityDiscDrive){
            DiscManipulator.loadDisc(((TileEntityDiscDrive) device.tile).virtualDisc,inventory);
        }
        if(device.tile instanceof TileEntityWirelessLink){
            if(((TileEntityWirelessLink) device.tile).remoteLink != null){
                HashMap<Direction, BlockInstance> candidates = scan(controller.worldObj, new Vec3i(((TileEntityWirelessLink) device.tile).remoteLink.xCoord,((TileEntityWirelessLink) device.tile).remoteLink.yCoord,((TileEntityWirelessLink) device.tile).remoteLink.zCoord));
                addRecursive(candidates);
            }
        }
    }

    @Override
    public void remove(BlockInstance device) {
        super.remove(device);
        if(device.tile == drive){
            drive = null;
        }
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = null;
        }
    }

    public ArrayList<BlockInstance> getAssemblers(){
        return searchAll(TileEntityAssembler.class);
    }

    public ArrayList<BlockInstance> getInterfaces(){
        return searchAll(TileEntityAdvInterface.class);
    }

    public HashMap<BlockInstance, ArrayList<ArrayList<NBTTagCompound>>> getAvailableProcessesWithSource(){
        HashMap<BlockInstance, ArrayList<ArrayList<NBTTagCompound>>> processes = new HashMap<>();
        ArrayList<BlockInstance> interfaces = getInterfaces();
        for(BlockInstance inf : interfaces){
            processes.put(inf,((TileEntityAdvInterface)inf.tile).getProcesses());
        }
        return processes;
    }

    public HashMap<BlockInstance, ArrayList<IRecipe>> getAvailableRecipesWithSource(){
        HashMap<BlockInstance, ArrayList<IRecipe>> recipes = new HashMap<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for(BlockInstance assembler : assemblers){
            ArrayList<IRecipe> assemblerRecipes = ((TileEntityAssembler)assembler.tile).getRecipes();
            if(assemblerRecipes != null){
                recipes.put(assembler,assemblerRecipes);
            }
        }
        return recipes;
    }

    public ArrayList<IRecipe> getAvailableRecipes(){
        ArrayList<IRecipe> recipes = new ArrayList<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for(BlockInstance assembler : assemblers){
            ArrayList<IRecipe> assemblerRecipes = ((TileEntityAssembler)assembler.tile).getRecipes();
            if(assemblerRecipes != null){
                recipes.addAll(assemblerRecipes);
            }
        }
        return recipes;
    }

    public ArrayList<ArrayList<NBTTagCompound>> getAvailableProcesses(){
        ArrayList<ArrayList<NBTTagCompound>> processes = new ArrayList<>();
        ArrayList<BlockInstance> interfaces = getInterfaces();
        for(BlockInstance inf : interfaces){
            ArrayList<ArrayList<NBTTagCompound>> interfaceProcesses = ((TileEntityAdvInterface)inf.tile).getProcesses();
            processes.addAll(interfaceProcesses);
        }
        return processes;
    }

    public void requestCrafting(IRecipe recipe) {
        if(recipe != null) {
            mod_RetroStorage.LOGGER.finest("Requesting: " + mod_RetroStorage.recipeToString(recipe));
            RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
    }

    public void requestProcessing(ArrayList<NBTTagCompound> tasks){
        if(tasks != null){
            mod_RetroStorage.LOGGER.finest("Requesting: " + mod_RetroStorage.getMainOutputOfProcess(tasks));
            ProcessTask task = new ProcessTask(tasks,null,null);
            //RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
    }


    public ArrayList<Task> getSubtask(Task task){
        mod_RetroStorage.LOGGER.finest("Getting subtasks for: "+task);
        if(task instanceof RecipeTask){
            IRecipe recipe = ((RecipeTask) task).recipe;
            ArrayList<Task> subtasks = new ArrayList<>();
            if(recipe != null) {
                ArrayList<ItemStack> inputs = mod_RetroStorage.condenseItemList(mod_RetroStorage.getRecipeItems(recipe));
                ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
                if(missing.size() == 0){
                    mod_RetroStorage.LOGGER.finest("No subtasks needed, all items available.");
                    return null;
                } else {
                    mod_RetroStorage.LOGGER.finest(String.format("Missing %d different items (%s) for %s, creating subtasks..",missing.size(),missing,mod_RetroStorage.recipeToString(recipe)));
                    for(ItemStack missingStack : missing) {
                        ArrayList<IRecipe> allRecipes = mod_RetroStorage.findRecipesByOutput(missingStack);
                        ArrayList<ArrayList<NBTTagCompound>> allProcesses = mod_RetroStorage.findProcessesByOutput(missingStack,this);
                        if (allRecipes.size() > 0) {
                            ArrayList<IRecipe> knownRecipes = getAvailableRecipes();
                            allRecipes.retainAll(knownRecipes);
                            if (allRecipes.size() > 0) {
                                IRecipe subtaskRecipe = allRecipes.get(0);
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                subtasks.add(subtask);
                            } else if(allProcesses.size() > 0) {
                                ArrayList<NBTTagCompound> subtaskProcess = allProcesses.get(0);
                                ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                                subtasks.add(subtask);
                            } else {
                                mod_RetroStorage.LOGGER.finest(String.format("Failed to create subtask: Network doesn't know how to craft or process %s!",missingStack));
                            }
                        } else if(allProcesses.size() > 0) {
                            ArrayList<NBTTagCompound> subtaskProcess = allProcesses.get(0);
                            ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                            subtasks.add(subtask);
                        } else {
                            mod_RetroStorage.LOGGER.finest(String.format("Failed to create subtask: %s can't be crafted nor processed.",missingStack));
                        }
                    }
                    mod_RetroStorage.LOGGER.finest(String.format("Got %d subtasks.",subtasks.size()));
                    return subtasks;
                }
            }
        } else if(task instanceof ProcessTask){
            ArrayList<Task> subtasks = new ArrayList<>();
            ArrayList<NBTTagCompound> steps = ((ProcessTask) task).tasks;
            if(steps != null){
                ArrayList<ItemStack> inputs = new ArrayList<>();
                for(NBTTagCompound step : steps){
                    ItemStack stack = ItemStack.loadItemStackFromNBT(step.getCompoundTag("stack"));//new ItemStack(step.getCompoundTag("stack"));
                    if(!step.getBoolean("isOutput")){
                        inputs.add(stack);
                    }
                }
                //TODO: items with meta like philo stone don't work
                inputs = mod_RetroStorage.condenseItemList(inputs);
                ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
                if(missing.size() == 0){
                    mod_RetroStorage.LOGGER.finest("No subtasks needed, all items available.");
                    return null;
                } else {
                    mod_RetroStorage.LOGGER.finest(String.format("Missing %d different items (%s), creating subtasks..",missing.size(),missing));
                    for(ItemStack missingStack : missing) {
                        ArrayList<IRecipe> allRecipes = mod_RetroStorage.findRecipesByOutput(missingStack);
                        ArrayList<ArrayList<NBTTagCompound>> allProcesses = mod_RetroStorage.findProcessesByOutput(missingStack,this);
                        if (allRecipes.size() > 0) {
                            ArrayList<IRecipe> knownRecipes = getAvailableRecipes();
                            allRecipes.retainAll(knownRecipes);
                            if (allRecipes.size() > 0) {
                                IRecipe subtaskRecipe = allRecipes.get(0);
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                subtasks.add(subtask);
                            } else if(allProcesses.size() > 0) {
                                ArrayList<NBTTagCompound> subtaskProcess = allProcesses.get(0);
                                ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                                subtasks.add(subtask);
                            } else {
                                mod_RetroStorage.LOGGER.finest(String.format("Failed to create subtask: Network doesn't know how to craft or process %s!",missingStack));
                            }
                        } else if(allProcesses.size() > 0) {
                            ArrayList<NBTTagCompound> subtaskProcess = allProcesses.get(0);
                            ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                            subtasks.add(subtask);
                        } else {
                            mod_RetroStorage.LOGGER.finest(String.format("Failed to create subtask: %s can't be crafted nor processed.",missingStack));
                        }
                    }
                    mod_RetroStorage.LOGGER.finest(String.format("Got %d subtasks.",subtasks.size()));
                    return subtasks;
                }
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getRequirements(ItemStack item){
        ArrayList<IRecipe> recipes = mod_RetroStorage.findRecipesByOutputUsingList(item,getAvailableRecipes());
        //HashMap<Integer,ArrayList<ItemStack>> req = new HashMap<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if(recipes.size() > 0){
            inputs = mod_RetroStorage.getRecipeItems(recipes.get(0));
            /*for(ItemStack input : (ArrayList<ItemStack>)inputs.clone()){
                int availableAmount = inventory.getItemCount(input.itemID,input.getMetadata());
                if(availableAmount < input.stackSize){
                    ArrayList<ItemStack> subInputs = getRequirements(input,depth++);
                    inputs.addAll(subInputs);
                }
            }*/
        }
        inputs = mod_RetroStorage.condenseItemList(inputs);
        return inputs;
    }

    /*public ArrayList<ItemStack> getRequirements(ItemStack item){
        return mod_RetroStorage.condenseItemList(mod_RetroStorage.getRecipeItems(mod_RetroStorage.findRecipesByOutputUsingList(item,getAvailableRecipes()).get(0)));
    }*/

    public void clearRequestQueue() {
        mod_RetroStorage.LOGGER.finest("Clearing request queue!");
        requestQueue = new ArrayDeque<>();
        for(BlockInstance assembler : getAssemblers()){
            TileEntityAssembler tile = (TileEntityAssembler) assembler.tile;
            if(tile.task != null){
                tile.task.processor = null;
                tile.task = null;
            }
        }
        for (BlockInstance anInterface : getInterfaces()) {
            TileEntityAdvInterface tile = (TileEntityAdvInterface) anInterface.tile;
            if(tile.request != null){
                tile.cancelProcessing();
            }
        }
    }
}
