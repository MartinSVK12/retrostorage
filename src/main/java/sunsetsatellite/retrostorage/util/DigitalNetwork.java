package sunsetsatellite.retrostorage.util;


import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

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
    public InventoryFluidDigital fluidInventory;
    public ArrayDeque<CraftingTask> requestQueue = new ArrayDeque<>();
    public ArrayList<NetworkCraftable> knownCraftables = new ArrayList<>();
    public ArrayList<CraftingTask> currentTasks = new ArrayList<>();
    public TileEntityDiscDrive drive;
    public TileEntityFluidDiscDrive fluidDrive;

    public DigitalNetwork(TileEntityDigitalController controller) {
        super(controller, TileEntityNetworkDevice.class, new int[]{RetroStorage.networkCable.id});
        this.inventory = new InventoryDigital(this);
        this.fluidInventory = new InventoryFluidDigital(this);
    }

    @Override
    public void add(BlockInstance device) {
        super.add(device);
        if (device.tile instanceof TileEntityNetworkDevice) {
            ((TileEntityNetworkDevice) device.tile).network = this;
        }
        if (device.tile instanceof TileEntityDiscDrive) {
            inventory.clear();
            inventory.updateSizes((TileEntityDiscDrive) device.tile);
            DiscManipulator.loadDisc(((TileEntityDiscDrive) device.tile).virtualDisc, inventory);
        }
        if (device.tile instanceof TileEntityFluidDiscDrive) {
            fluidInventory.clear();
            fluidInventory.updateSizes((TileEntityFluidDiscDrive) device.tile);
            DiscManipulator.loadDisc(((TileEntityFluidDiscDrive) device.tile).virtualDisc, fluidInventory);
        }
        if (device.tile instanceof TileEntityAssembler) {
            for (RecipeEntryCrafting<?, ItemStack> recipe : ((TileEntityAssembler) device.tile).getRecipes()) {
                knownCraftables.add(new NetworkCraftable(recipe));
            }
        }
        if (device.tile instanceof TileEntityAdvInterface) {
            for (CraftingProcess process : ((TileEntityAdvInterface) device.tile).getProcesses()) {
                knownCraftables.add(new NetworkCraftable(process));
            }
        }
        if (device.tile instanceof TileEntityWirelessLink) {
            if (((TileEntityWirelessLink) device.tile).remoteLink != null) {
                HashMap<String, BlockInstance> candidates = scan(controller.worldObj, new Vec3i(((TileEntityWirelessLink) device.tile).remoteLink.x, ((TileEntityWirelessLink) device.tile).remoteLink.y, ((TileEntityWirelessLink) device.tile).remoteLink.z));
                addRecursive(candidates);
            }
        }
    }

    @Override
    public void remove(BlockInstance device) {
        super.remove(device);
        if (device.tile == drive) {
            drive = null;
        }
        if (device.tile == fluidDrive) {
            fluidDrive = null;
        }
        if (device.tile instanceof TileEntityDiscDrive) {
            inventory.clear();
            inventory.resetSizes();
        }
        if (device.tile instanceof TileEntityFluidDiscDrive) {
            fluidInventory.clear();
            fluidInventory.resetSizes();
        }
        if (device.tile instanceof TileEntityNetworkDevice) {
            ((TileEntityNetworkDevice) device.tile).network = null;
        }
        if (device.tile instanceof TileEntityAssembler) {
            for (RecipeEntryCrafting<?, ItemStack> recipe : ((TileEntityAssembler) device.tile).getRecipes()) {
                knownCraftables.remove(new NetworkCraftable(recipe));
            }
        }
        if (device.tile instanceof TileEntityAdvInterface) {
            for (CraftingProcess process : ((TileEntityAdvInterface) device.tile).getProcesses()) {
                knownCraftables.remove(new NetworkCraftable(process));
            }
        }
    }

    public ArrayList<BlockInstance> getAssemblers() {
        return searchAll(TileEntityAssembler.class);
    }

    public ArrayList<BlockInstance> getAdvInterfaces() {
        return searchAll(TileEntityAdvInterface.class);
    }

    public ArrayList<BlockInstance> getCoprocessors() {
        return searchAll(TileEntityCoprocessor.class);
    }

    public int getMaxCraftables() {
        return (getAssemblers().size() * 9) + (getAdvInterfaces().size() * 9);
    }

    public ArrayList<RecipeEntryCrafting<?, ItemStack>> getAvailableRecipes() {
        ArrayList<RecipeEntryCrafting<?, ItemStack>> recipes = new ArrayList<>();
        ArrayList<BlockInstance> assemblers = getAssemblers();
        for (BlockInstance assembler : assemblers) {
            ArrayList<RecipeEntryCrafting<?, ItemStack>> assemblerRecipes = ((TileEntityAssembler) assembler.tile).getRecipes();
            if (assemblerRecipes != null) {
                recipes.addAll(assemblerRecipes);
            }
        }
        return recipes;
    }

    public ArrayList<CraftingProcess> getAvailableProcesses() {
        ArrayList<CraftingProcess> processes = new ArrayList<>();
        ArrayList<BlockInstance> interfaces = getAdvInterfaces();
        for (BlockInstance intf : interfaces) {
            ArrayList<CraftingProcess> interfaceProcesses = ((TileEntityAdvInterface) intf.tile).getProcesses();
            if (interfaceProcesses != null) {
                processes.addAll(interfaceProcesses);
            }
        }
        return processes;
    }

    public IProcessor findProcessor(NetworkCraftable craftable) {
        ArrayList<BlockInstance> instances = new ArrayList<>();
        instances.addAll(getAssemblers());
        instances.addAll(getAdvInterfaces());
        for (BlockInstance instance : instances) {
            IProcessor processor = (IProcessor) instance.tile;
            if (processor.getCraftables().contains(craftable)) {
                return processor;
            }
        }
        return null;
    }

    public IProcessor findProcessorWithNode(ProcessNode node) {
        for (BlockInstance advInterface : getAdvInterfaces()) {
            if (((TileEntityAdvInterface) advInterface.tile).workingNode == node) {
                return (IProcessor) advInterface.tile;
            }
        }
        return null;
    }

    public void requestCrafting(CraftingTask task) {
        if (task != null) {
            RetroStorage.LOGGER.debug("Requesting: " + task.getCraftable().getOutput());
            requestQueue.add(task);
        }
    }

    public void clearRequestQueue() {
        RetroStorage.LOGGER.debug("Clearing request queue!");
        for (CraftingTask task : requestQueue) {
            task.onCancelled();
        }
        for (BlockInstance advInterface : getAdvInterfaces()) {
            ((TileEntityAdvInterface) advInterface.tile).setFocus(null, null);
        }
        requestQueue = new ArrayDeque<>();
        currentTasks.clear();
    }

    @Override
    public void tick() {
        super.tick();
        if (currentTasks.size() < getCoprocessors().size() + 1) {
            for (CraftingTask task : requestQueue) {
                if (!task.isStarted()) {
                    currentTasks.add(task);
                    task.start();
                    break;
                }
            }
        }
        if (!currentTasks.isEmpty()) {
            ArrayList<CraftingTask> removing = new ArrayList<>();
            for (CraftingTask task : currentTasks) {
                if (task.update()) {
                    requestQueue.remove(task);
                    removing.add(task);
                }
            }
            for (CraftingTask task : removing) {
                currentTasks.remove(task);
            }
        }
    }
}
