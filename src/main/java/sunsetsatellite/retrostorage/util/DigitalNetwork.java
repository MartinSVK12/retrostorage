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
