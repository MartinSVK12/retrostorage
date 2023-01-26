package sunsetsatellite.retrostorage.util;

import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityAssembler;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

import java.util.*;

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
    //autocrafting stuff
    private boolean lastRequestFailed = false;
    private ArrayList<ItemStack> previousTaskCredit = new ArrayList<>();
    private int taskIndex = 0;

    public DigitalNetwork(TileEntityDigitalController controller) {
        super(controller, TileEntityNetworkDevice.class, new int[]{RetroStorage.networkCable.blockID});
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

    public void requestCrafting(IRecipe recipe) {
        if(recipe != null) {
            RetroStorage.LOGGER.info("Requesting: " + RetroStorage.recipeToString(recipe));
            RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
        /*ArrayList<Task> tasks = new ArrayList<>();
        if(recipe != null){
            RetroStorage.LOGGER.info("Requesting: "+RetroStorage.recipeToString(recipe));
            RecipeTask task = new RecipeTask(recipe,null,null);
            taskIndex = 0;
            task.priority = taskIndex;
            ArrayList<Task> subtasks = createSubtasks(task, 0);
            task.requires = subtasks;
            RetroStorage.LOGGER.info("Task: "+ task);
            RetroStorage.LOGGER.info("Subtasks: "+ subtasks);
            RetroStorage.LOGGER.info("Credit: " + previousTaskCredit);
            if(lastRequestFailed){
                RetroStorage.LOGGER.error("Request failed!");
                lastRequestFailed = false;
                previousTaskCredit = new ArrayList<>();
                return;
            }
            tasks.addAll(subtasks);
            tasks.add(task);
        }
        Task rootTask = null;
        for(Task task : tasks){
            if(task instanceof RecipeTask){
                if(task.parent == null){
                    rootTask = task;
                }
            }
        }
        if(rootTask != null){
            RetroStorage.LOGGER.info("Request successful! Requesting tasks: ");
            RetroStorage.LOGGER.info("");
            RetroStorage.LOGGER.info(tasks.size()+" TASKS");
            RetroStorage.printTaskTree(rootTask);
        }
        tasks.sort((T,T2)-> Integer.compare(T2.priority, T.priority));
        requestQueue.addAll(tasks);
        previousTaskCredit = new ArrayList<>();*/
    }


    public ArrayList<Task> getSubtask(Task task){
        RetroStorage.LOGGER.info("Getting subtasks for: "+task);
        if(task instanceof RecipeTask){
            IRecipe recipe = ((RecipeTask) task).recipe;
            ArrayList<Task> subtasks = new ArrayList<>();
            if(recipe != null) {
                ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
                ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
                if(missing.size() == 0){
                    RetroStorage.LOGGER.info("No subtasks needed, all items available.");
                    return null;
                } else {
                    RetroStorage.LOGGER.info(String.format("Missing %d different items (%s) for %s, creating subtasks..",missing.size(),missing,RetroStorage.recipeToString(recipe)));
                    for(ItemStack missingStack : missing) {
                        ArrayList<IRecipe> allRecipes = RetroStorage.findRecipesByOutput(missingStack);
                        if (allRecipes.size() > 0) {
                            ArrayList<IRecipe> knownRecipes = getAvailableRecipes();
                            allRecipes.retainAll(knownRecipes);
                            if (allRecipes.size() > 0) {
                                IRecipe subtaskRecipe = allRecipes.get(0);
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                subtasks.add(subtask);
                            } else {
                                RetroStorage.LOGGER.error(String.format("Failed to create subtask: Network doesn't know how to craft %s!",missingStack));
                            }
                        } else {
                            RetroStorage.LOGGER.error(String.format("Failed to create subtask: %s can't be crafted.",missingStack));
                        }
                    }
                    RetroStorage.LOGGER.info(String.format("Got %d subtasks.",subtasks.size()));
                    return subtasks;
                }
            }
        } else if(task instanceof ProcessTask){
            RetroStorage.LOGGER.error("Work in progress.");
            return null;
        }
        return null;
    }

    /*public ArrayList<Task> createSubtasks(RecipeTask task, int depth){
        RetroStorage.LOGGER.info("-BEGIN DEPTH:"+depth+" INDEX:"+taskIndex+"-");
        IRecipe recipe = task.recipe;
        ArrayList<Task> tasks = new ArrayList<>();
        if(recipe != null) {
            ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
            ArrayList<ItemStack> missing = inventory.hasItemsReturnMissing(inputs);
            previousTaskCredit = RetroStorage.condenseItemList(previousTaskCredit);
            if (missing.size() == 0) {
                RetroStorage.LOGGER.info(String.format("No missing items for recipe %s, no need to add any more subtasks.",RetroStorage.recipeToString(recipe)));
            } else {
                RetroStorage.LOGGER.info(String.format("Missing %d items (%s) for %s, creating subtasks..",missing.size(),missing,RetroStorage.recipeToString(recipe)));
                for(ItemStack missingStack : missing){
                    ArrayList<ItemStack> creditClone = (ArrayList<ItemStack>) previousTaskCredit.clone();
                    boolean creditUsed = false;
                    for(ItemStack credit : creditClone){
                        if(credit.isItemEqual(missingStack)){
                            if(credit.stackSize >= missingStack.stackSize){
                                RetroStorage.LOGGER.info("Credit from previous tasks used, no need to add any more subtasks.");
                                credit.stackSize -= missingStack.stackSize;
                                if(credit.stackSize <= 0){
                                    previousTaskCredit.remove(credit);
                                }
                                creditUsed = true;
                            }
                        }
                    }
                    if(creditUsed){
                        continue;
                    }
                    ArrayList<IRecipe> allRecipes = RetroStorage.findRecipesByOutput(missingStack);
                    if(allRecipes.size() > 0){
                        ArrayList<IRecipe> knownRecipes = getAvailableRecipes();
                        allRecipes.retainAll(knownRecipes);
                        if(allRecipes.size() > 0){
                            IRecipe subtaskRecipe = allRecipes.get(0);
                            int multiplier = (int) Math.ceil((float)missingStack.stackSize/(float)subtaskRecipe.getRecipeOutput().stackSize);
                            RetroStorage.LOGGER.info(String.format("Need %d %s and recipe makes %d %s per craft",missingStack.stackSize,missingStack.getItem().getItemName(),subtaskRecipe.getRecipeOutput().stackSize,missingStack.getItem().getItemName()));
                            RetroStorage.LOGGER.info(String.format("Current recipe needs to be done %d times",multiplier));
                            if(multiplier > 1){
                                ItemStack creditStack = null;
                                for (int i = 0; i < multiplier; i++) {
                                    if(creditStack == null){
                                        creditStack = subtaskRecipe.getRecipeOutput().copy();
                                    } else {
                                        creditStack.stackSize += subtaskRecipe.getRecipeOutput().copy().stackSize;
                                    }
                                    RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                    taskIndex++;
                                    subtask.attempts = taskIndex;
                                    ArrayList<Task> subtaskSubtasks = createSubtasks(subtask,depth+1);
                                    ArrayList<Task> subtaskSubtasksWithoutSelf = (ArrayList<Task>) subtaskSubtasks.clone();
                                    subtaskSubtasksWithoutSelf.remove(subtask);
                                    subtask.requires = subtaskSubtasksWithoutSelf;
                                    subtaskSubtasks.add(subtask);
                                    RetroStorage.LOGGER.info(String.format("Adding %d subtasks.",subtaskSubtasks.size()));
                                    tasks.addAll(subtaskSubtasks);
                                }
                                creditStack.stackSize -= missingStack.stackSize;
                                if(creditStack.stackSize <= 0){
                                    creditStack = null;
                                }
                                if(creditStack != null){
                                    previousTaskCredit.add(creditStack);
                                }
                            } else {
                                ItemStack creditStack = subtaskRecipe.getRecipeOutput().copy();;
                                creditStack.stackSize -= missingStack.stackSize;
                                if(creditStack.stackSize <= 0){
                                    creditStack = null;
                                }
                                if(creditStack != null){
                                    previousTaskCredit.add(creditStack);
                                }
                                RecipeTask subtask = new RecipeTask(subtaskRecipe,task,null);
                                taskIndex++;
                                subtask.attempts = taskIndex;
                                ArrayList<Task> subtaskSubtasks = createSubtasks(subtask,depth+1);
                                ArrayList<Task> subtaskSubtasksWithoutSelf = (ArrayList<Task>) subtaskSubtasks.clone();
                                subtaskSubtasksWithoutSelf.remove(subtask);
                                subtask.requires = subtaskSubtasksWithoutSelf;
                                subtaskSubtasks.add(subtask);
                                RetroStorage.LOGGER.info(String.format("Adding %d subtasks.",subtaskSubtasks.size()));
                                tasks.addAll(subtaskSubtasks);
                            }
                        } else {
                            RetroStorage.LOGGER.error(String.format("Failed to create subtask: Network doesn't know how to craft %s!",missingStack));
                            lastRequestFailed = true;
                        }
                    } else {
                        RetroStorage.LOGGER.error(String.format("Failed to create subtask: %s can't be crafted.",missingStack));
                        lastRequestFailed = true;
                    }
                }
            }
        }
        RetroStorage.LOGGER.info("Credit: "+previousTaskCredit);
        RetroStorage.LOGGER.info("-END DEPTH:"+depth+"-");
        return tasks;
    }*/

    public void clearRequestQueue() {
        RetroStorage.LOGGER.info("Clearing request queue!");
        requestQueue = new ArrayDeque<>();
        for(BlockInstance assembler : getAssemblers()){
            TileEntityAssembler tile = (TileEntityAssembler) assembler.tile;
            if(tile.task != null){
                tile.task.processor = null;
                tile.task = null;
            }
        }
    }
}
