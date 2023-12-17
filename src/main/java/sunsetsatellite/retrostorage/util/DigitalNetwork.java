package sunsetsatellite.retrostorage.util;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.*;

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
        super(controller, TileEntityNetworkDevice.class, new int[]{RetroStorage.networkCable.id});
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
                HashMap<String, BlockInstance> candidates = scan(controller.worldObj, new Vec3i(((TileEntityWirelessLink) device.tile).remoteLink.x,((TileEntityWirelessLink) device.tile).remoteLink.y,((TileEntityWirelessLink) device.tile).remoteLink.z));
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

    public HashMap<BlockInstance, ArrayList<ArrayList<CompoundTag>>> getAvailableProcessesWithSource(){
        HashMap<BlockInstance, ArrayList<ArrayList<CompoundTag>>> processes = new HashMap<>();
        ArrayList<BlockInstance> interfaces = getInterfaces();
        for(BlockInstance inf : interfaces){
            processes.put(inf,((TileEntityAdvInterface)inf.tile).getProcesses());
        }
        return processes;
    }

    public HashMap<BlockInstance, ArrayList<RecipeEntryCrafting<?,?>>> getAvailableRecipesWithSource(){
        HashMap<BlockInstance, ArrayList<RecipeEntryCrafting<?,?>>> recipes = new HashMap<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for(BlockInstance assembler : assemblers){
            ArrayList<RecipeEntryCrafting<?,?>> assemblerRecipes = ((TileEntityAssembler)assembler.tile).getRecipes();
            if(assemblerRecipes != null){
                recipes.put(assembler,assemblerRecipes);
            }
        }
        return recipes;
    }

    public ArrayList<RecipeEntryCrafting<?,?>> getAvailableRecipes(){
        ArrayList<RecipeEntryCrafting<?,?>> recipes = new ArrayList<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for(BlockInstance assembler : assemblers){
            ArrayList<RecipeEntryCrafting<?,?>> assemblerRecipes = ((TileEntityAssembler)assembler.tile).getRecipes();
            if(assemblerRecipes != null){
                recipes.addAll(assemblerRecipes);
            }
        }
        return recipes;
    }

    public ArrayList<ArrayList<CompoundTag>> getAvailableProcesses(){
        ArrayList<ArrayList<CompoundTag>> processes = new ArrayList<>();
        ArrayList<BlockInstance> interfaces = getInterfaces();
        for(BlockInstance inf : interfaces){
            ArrayList<ArrayList<CompoundTag>> interfaceProcesses = ((TileEntityAdvInterface)inf.tile).getProcesses();
            processes.addAll(interfaceProcesses);
        }
        return processes;
    }

    public void requestCrafting(RecipeEntryCrafting<?,?> recipe) {
        if(recipe != null) {
            RetroStorage.LOGGER.debug("Requesting: " + RetroStorage.recipeToString(recipe));
            RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
    }

    public void requestProcessing(ArrayList<CompoundTag> tasks){
        if(tasks != null){
            RetroStorage.LOGGER.debug("Requesting: " + RetroStorage.getMainOutputOfProcess(tasks));
            ProcessTask task = new ProcessTask(tasks,null,null);
            //RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
    }


    public ArrayList<Task> getSubtask(Task task){
        RetroStorage.LOGGER.debug("Getting subtasks for: "+task);
        if(task instanceof RecipeTask){
            RecipeEntryCrafting<?,?> recipe = ((RecipeTask) task).recipe;
            ArrayList<Task> subtasks = new ArrayList<>();
            if(recipe != null) {
                ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
                ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
                if(missing.isEmpty()){
                    RetroStorage.LOGGER.debug("No subtasks needed, all items available.");
                    return null;
                } else {
                    RetroStorage.LOGGER.debug(String.format("Missing %d different items (%s) for %s, creating subtasks..",missing.size(),missing,RetroStorage.recipeToString(recipe)));
                    for(ItemStack missingStack : missing) {
                        ArrayList<RecipeEntryCrafting<?,?>> allRecipes = RetroStorage.findRecipesByOutput(missingStack);
                        ArrayList<ArrayList<CompoundTag>> allProcesses = RetroStorage.findProcessesByOutput(missingStack,this);
                        if (!allRecipes.isEmpty()) {
                            ArrayList<RecipeEntryCrafting<?,?>> knownRecipes = getAvailableRecipes();
                            allRecipes.retainAll(knownRecipes);
                            if (!allRecipes.isEmpty()) {
                                RecipeEntryCrafting<?,?> subtaskRecipe = allRecipes.get(0);
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                subtasks.add(subtask);
                            } else if(!allProcesses.isEmpty()) {
                                ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                                ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                                subtasks.add(subtask);
                            } else {
                                RetroStorage.LOGGER.error(String.format("Failed to create subtask: Network doesn't know how to craft or process %s!",missingStack));
                            }
                        } else if(!allProcesses.isEmpty()) {
                            ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                            ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                            subtasks.add(subtask);
                        } else {
                            RetroStorage.LOGGER.error(String.format("Failed to create subtask: %s can't be crafted nor processed.",missingStack));
                        }
                    }
                    RetroStorage.LOGGER.debug(String.format("Got %d subtasks.",subtasks.size()));
                    return subtasks;
                }
            }
        } else if(task instanceof ProcessTask){
            ArrayList<Task> subtasks = new ArrayList<>();
            ArrayList<CompoundTag> steps = ((ProcessTask) task).tasks;
            if(steps != null){
                ArrayList<ItemStack> inputs = new ArrayList<>();
                for(CompoundTag step : steps){
                    ItemStack stack = ItemStack.readItemStackFromNbt(step.getCompound("stack"));
                    if(stack == null) continue;
                    if(!step.getBoolean("isOutput")){
                        inputs.add(stack);
                    }
                }
                inputs = RetroStorage.condenseItemList(inputs);
                ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
                if(missing.isEmpty()){
                    RetroStorage.LOGGER.debug("No subtasks needed, all items available.");
                    return null;
                } else {
                    RetroStorage.LOGGER.debug(String.format("Missing %d different items (%s), creating subtasks..",missing.size(),missing));
                    for(ItemStack missingStack : missing) {
                        ArrayList<RecipeEntryCrafting<?,?>> allRecipes = RetroStorage.findRecipesByOutput(missingStack);
                        ArrayList<ArrayList<CompoundTag>> allProcesses = RetroStorage.findProcessesByOutput(missingStack,this);
                        if (!allRecipes.isEmpty()) {
                            ArrayList<RecipeEntryCrafting<?,?>> knownRecipes = getAvailableRecipes();
                            allRecipes.retainAll(knownRecipes);
                            if (!allRecipes.isEmpty()) {
                                RecipeEntryCrafting<?,?> subtaskRecipe = allRecipes.get(0);
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                subtasks.add(subtask);
                            } else if(!allProcesses.isEmpty()) {
                                ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                                ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                                subtasks.add(subtask);
                            } else {
                                RetroStorage.LOGGER.error(String.format("Failed to create subtask: Network doesn't know how to craft or process %s!",missingStack));
                            }
                        } else if(!allProcesses.isEmpty()) {
                            ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                            ProcessTask subtask = new ProcessTask(subtaskProcess,task,null);
                            subtasks.add(subtask);
                        } else {
                            RetroStorage.LOGGER.error(String.format("Failed to create subtask: %s can't be crafted nor processed.",missingStack));
                        }
                    }
                    RetroStorage.LOGGER.debug(String.format("Got %d subtasks.",subtasks.size()));
                    return subtasks;
                }
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getRequirements(ItemStack item){
        ArrayList<RecipeEntryCrafting<?,?>> recipes = RetroStorage.findRecipesByOutputUsingList(item,getAvailableRecipes());
        //HashMap<Integer,ArrayList<ItemStack>> req = new HashMap<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        if(!recipes.isEmpty()){
            inputs = RetroStorage.getRecipeItems(recipes.get(0));
            /*for(ItemStack input : (ArrayList<ItemStack>)inputs.clone()){
                int availableAmount = inventory.getItemCount(input.itemID,input.getMetadata());
                if(availableAmount < input.stackSize){
                    ArrayList<ItemStack> subInputs = getRequirements(input,depth++);
                    inputs.addAll(subInputs);
                }
            }*/
        }
        inputs = RetroStorage.condenseItemList(inputs);
        return inputs;
    }

    /*public ArrayList<ItemStack> getRequirements(ItemStack item){
        return RetroStorage.condenseItemList(RetroStorage.getRecipeItems(RetroStorage.findRecipesByOutputUsingList(item,getAvailableRecipes()).get(0)));
    }*/

    public void clearRequestQueue() {
        RetroStorage.LOGGER.debug("Clearing request queue!");
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
