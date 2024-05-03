package sunsetsatellite.retrostorage.util;


import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.crafting.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

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
    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<NetworkCraftable> knownCraftables = new ArrayList<>();
    public TileEntityDiscDrive drive;

    public DigitalNetwork(TileEntityDigitalController controller) {
        super(controller, TileEntityNetworkDevice.class, new int[]{RetroStorage.networkCable.id});
        this.inventory = new InventoryDigital(this);
    }

    @Override
    public void add(BlockInstance device) {
        super.add(device);
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = this;
        }
        if(device.tile instanceof TileEntityDiscDrive){
            inventory.clear();
            inventory.updateSizes((TileEntityDiscDrive) device.tile);
            DiscManipulator.loadDisc(((TileEntityDiscDrive) device.tile).virtualDisc,inventory);
        }
        if(device.tile instanceof TileEntityAssembler){
            for (RecipeEntryCrafting<?, ItemStack> recipe : ((TileEntityAssembler) device.tile).getRecipes()) {
                knownCraftables.add(new NetworkCraftable(recipe));
            }
        }
    }

    @Override
    public void remove(BlockInstance device) {
        super.remove(device);
        if(device.tile == drive){
            drive = null;
        }
        if(device.tile instanceof TileEntityDiscDrive) {
            inventory.clear();
            inventory.resetSizes();
        }
        if(device.tile instanceof TileEntityNetworkDevice){
            ((TileEntityNetworkDevice)device.tile).network = null;
        }
        if(device.tile instanceof TileEntityAssembler){
            for (RecipeEntryCrafting<?, ItemStack> recipe : ((TileEntityAssembler) device.tile).getRecipes()) {
                knownCraftables.remove(new NetworkCraftable(recipe));
            }
        }
    }

    public ArrayList<BlockInstance> getAssemblers(){
        return searchAll(TileEntityAssembler.class);
    }

    public int getMaxCraftables(){
        return getAssemblers().size()*9;
    }
    /*public ArrayList<BlockInstance> getInterfaces(){
        return searchAll(TileEntityAdvInterface.class);
    }*/

    /*

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
    }*/

    public ArrayList<RecipeEntryCrafting<?,ItemStack>> getAvailableRecipes(){
        ArrayList<RecipeEntryCrafting<?,ItemStack>> recipes = new ArrayList<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for(BlockInstance assembler : assemblers){
            ArrayList<RecipeEntryCrafting<?,ItemStack>> assemblerRecipes = ((TileEntityAssembler)assembler.tile).getRecipes();
            if(assemblerRecipes != null){
                recipes.addAll(assemblerRecipes);
            }
        }
        return recipes;
    }



    /*public ArrayList<ArrayList<CompoundTag>> getAvailableProcesses(){
        ArrayList<ArrayList<CompoundTag>> processes = new ArrayList<>();
        ArrayList<BlockInstance> interfaces = getInterfaces();
        for(BlockInstance inf : interfaces){
            ArrayList<ArrayList<CompoundTag>> interfaceProcesses = ((TileEntityAdvInterface)inf.tile).getProcesses();
            processes.addAll(interfaceProcesses);
        }
        return processes;
    }

    public boolean canMake(ItemStack stack){
        ArrayList<RecipeEntryCrafting<?, ?>> recipes = RetroStorage.findRecipesByOutput(stack, this);
        ArrayList<ArrayList<CompoundTag>> processes = RetroStorage.findProcessesByOutput(stack, this);
        return !recipes.isEmpty() || !processes.isEmpty();
    }*/

    public void requestCrafting(CraftingTask task) {
        if(task != null) {
            RetroStorage.LOGGER.debug("Requesting: " + task.getCraftable().getOutput());
            requestQueue.add(task);
        }
    }

    /*public void requestProcessing(ArrayList<CompoundTag> tasks){
        if(tasks != null){
            RetroStorage.LOGGER.debug("Requesting: " + RetroStorage.getMainOutputOfProcess(tasks));
            ProcessTask task = new ProcessTask(tasks,null,null);
            //RecipeTask task = new RecipeTask(recipe, null, null);
            requestQueue.add(task);
        }
    }
*/
    /*public List<ItemStack> getRequirements(RecipeEntryCrafting<?,?> recipe){
        RecipeTask task = new RecipeTask(recipe, null, null);
        //RecipeSimulator simulator = new RecipeSimulator(task,this);
        return RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
    }*/

    public void clearRequestQueue() {
        RetroStorage.LOGGER.debug("Clearing request queue!");
        requestQueue = new ArrayDeque<>();
        for (BlockInstance assembler : getAssemblers()) {
            ((TileEntityAssembler) assembler.tile).cancelTask();
        }
    }
}
