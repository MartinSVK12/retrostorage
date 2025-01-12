package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.util.FluidStackList;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.IProcessor;
import sunsetsatellite.retrostorage.util.ItemStackList;

import java.util.List;

public class CraftingTask {
    public IProcessor processor;
    public final INetworkController network;
    private final int quantity;
    public final NodeList nodes;
    private final NetworkCraftable craftable;
    private int totalSteps;
    private int currentStep;
    private long startTime = -1;
    private int ticks;
    private boolean started = false;

    private final ItemStackList internalStorage = new ItemStackList();
    private final FluidStackList internalFluidStorage = new FluidStackList();
    private final ItemStackList initialRequirements;
    private final FluidStackList initialFluidRequirements;

    public CraftingTask(INetworkController network, int quantity, NodeList nodes, NetworkCraftable craftable, ItemStackList initialRequirements, FluidStackList initialFluidRequirements) {
        this.network = network;
        this.quantity = quantity;
        this.nodes = nodes;
        this.craftable = craftable;
        this.initialRequirements = initialRequirements;
        this.initialFluidRequirements = initialFluidRequirements;
    }

    public void start() {
        if (started) return;
        nodes.all().forEach(node -> {
            totalSteps += node.getQuantity();
            node.onCalculationFinished();
        });

        startTime = System.currentTimeMillis();

        List<ItemStack> leftovers = network.moveItems(initialRequirements, internalStorage);
        initialRequirements.clear();
        initialRequirements.addAll(leftovers);
        List<FluidStack> fluidLeftovers = network.moveFluids(initialFluidRequirements, internalFluidStorage);
        initialFluidRequirements.clear();
        initialFluidRequirements.addAll(fluidLeftovers);

        started = true;
    }

    public boolean update() {
        if (!started) return false;
        ticks++;

        //task finished
        if (nodes.isEmpty()) {
            network.addItemsToNetwork(internalStorage.getStacks());
            network.addFluidsToNetwork(internalFluidStorage.getStacks());
            return internalStorage.isEmpty() && internalFluidStorage.isEmpty();
        } else { //task not finished
            if (!initialRequirements.isEmpty()) {
                List<ItemStack> leftovers = network.moveItems(initialRequirements, internalStorage);
                initialRequirements.clear();
                initialRequirements.addAll(leftovers);
                if (!initialRequirements.isEmpty()) {
                    return false;
                }
            }

            if (!initialFluidRequirements.isEmpty()) {
                List<FluidStack> leftovers = network.moveFluids(initialFluidRequirements, internalFluidStorage);
                initialFluidRequirements.clear();
                initialFluidRequirements.addAll(leftovers);
                if (!initialFluidRequirements.isEmpty()) {
                    return false;
                }
            }

            for (Node node : nodes.all()) {
                node.update(network, nodes, internalStorage, internalFluidStorage, this);
            }

            nodes.removeMarkedForRemoval();

            return false;
        }
    }

    public int insertFromProcess(ItemStack stack) {
        int size = stack.stackSize;
        for (Node node : this.nodes.all()) {
            if (node instanceof ProcessNode) {
                ProcessNode processing = (ProcessNode) node;

                int needed = processing.getNeeded(stack);
                if (needed > 0) {
                    if (needed > size) {
                        needed = size;
                    }

                    processing.markReceived(stack);

                    size -= needed;

                    if (!processing.isRoot()) {
                        internalStorage.add(stack);
                    } else {
                        ItemStack remainder = network.addItemToNetwork(stack);

                        internalStorage.add(remainder);
                    }

                    if (size == 0) {
                        return 0;
                    }
                }
            }
        }

        return size;
    }

    public int insertFromProcess(FluidStack stack) {
        int size = stack.amount;
        for (Node node : this.nodes.all()) {
            if (node instanceof ProcessNode) {
                ProcessNode processing = (ProcessNode) node;

                int needed = processing.getNeeded(stack);
                if (needed > 0) {
                    if (needed > size) {
                        needed = size;
                    }

                    processing.markReceived(stack);

                    size -= needed;

                    if (!processing.isRoot()) {
                        internalFluidStorage.add(stack);
                    } else {
                        FluidStack remainder = network.addFluidToNetwork(stack);

                        internalFluidStorage.add(remainder);
                    }

                    if (size == 0) {
                        return 0;
                    }
                }
            }
        }

        return size;
    }

    public void onCancelled() {
        network.addItemsToNetwork(internalStorage.getStacks());
        network.addFluidsToNetwork(internalFluidStorage.getStacks());
        if (processor != null) {
            processor.setFocus(null, null);
        }
    }

    public int getCompletionPercentage() {
        if (totalSteps == 0) {
            return 0;
        }

        return (int) ((float) currentStep * 100 / totalSteps);
    }

    public long getStartTime() {
        return startTime;
    }

    public NetworkCraftable getCraftable() {
        return craftable;
    }

    public void onAllDone(Node node) {
        nodes.remove(node);
    }

    public void onSingleDone(Node node) {
        currentStep++;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isStarted() {
        return started;
    }
}
